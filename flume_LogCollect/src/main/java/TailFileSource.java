import org.apache.commons.io.FileUtils;
import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zx on 2017/10/26.
 * flume source 的生命周期：构造器 -> configure -> start -> processor.process
 * 1.读取配置文件：（配置文件的内容：读取哪个文件、编码集、偏移量写到哪个文件、多长时间检查一下文件是否有新内容）
 */
public class TailFileSource extends AbstractSource implements EventDrivenSource, Configurable {

    private static final Logger logger = LoggerFactory.getLogger(TailFileSource.class);

    private String filePath;
    private String charset;
    private String posiFile;
    private long interval;
    private ExecutorService executor;
    private FileRunnable fileRunnable;

    @Override
    public void configure(Context context) {
        filePath = context.getString("filePath");
        charset = context.getString("charset", "UTF-8");
        posiFile = context.getString("posiFile");
        interval = context.getLong("interval", 1000L);
    }


    @Override
    public synchronized void start() {
        //创建一个单线程的线程池
        executor = Executors.newSingleThreadExecutor();
        //定义一个实现runnable接口的类
        fileRunnable = new FileRunnable(filePath, posiFile, interval, charset, getChannelProcessor());
        //实现runnable接口的类提交到线程池
        executor.submit(fileRunnable);
        //调用父类的start方法
        super.start();
    }

    @Override
    public synchronized void stop() {
        fileRunnable.setFlag(false);
        executor.shutdown();
        while (!executor.isTerminated()) {
            logger.debug("Waiting for filer executor service to stop");
            try {
                executor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.debug("Interrupted while waiting for exec executor service "
                        + "to stop. Just exiting.");
                Thread.currentThread().interrupt();
            }
        }
        super.stop();
    }


    private static class FileRunnable implements Runnable {

        private long interval;
        private String charset;
        private ChannelProcessor channelProcessor;
        private long offset = 0L;
        private RandomAccessFile raf;
        private boolean flag = true;
        private File positionFile;

        /**
         * @param filePath
         * @param posiFile
         * @param interval
         * @param charset
         * @param channelProcessor
         */
        private FileRunnable(String filePath, String posiFile, long interval, String charset, ChannelProcessor channelProcessor) {
            this.interval = interval;
            this.charset = charset;
            this.channelProcessor = channelProcessor;


            //读取偏移量，如果有，就接着读，没有就从头读
            positionFile = new File(posiFile);
            if (!positionFile.exists()) {
                //不存在就创建一个位置文件
                try {
                    positionFile.createNewFile();
                } catch (IOException e) {
                    //e.printStackTrace();
                    logger.error("create position file error", e);
                }
            }
            //读取偏移量
            try {
                String offsetString = FileUtils.readFileToString(positionFile);
                //如果以前记录过偏移量
                if (offsetString != null && !"".equals(offsetString)) {
                    //将当前的偏移量转换成long
                    offset = Long.parseLong(offsetString);
                }
                //读取log文件是从指定的位置读取数据
                raf = new RandomAccessFile(filePath, "r");
                //按照指定的偏移量读取
                raf.seek(offset);
            } catch (IOException e) {
                //e.printStackTrace();
                logger.error("read position file error", e);
            }

        }

        @Override
        public void run() {

            while (flag) {
                try {
                    //读取log件中的新数据

                    String line = raf.readLine();

                    if (line != null) {
                        line = new String(line.getBytes("ISO-8859-1"), charset);
                        //将数据发送给Channel
                        channelProcessor.processEvent(EventBuilder.withBody(line.getBytes()));
                        //获取最新的偏移量，然后更新偏移量
                        offset = raf.getFilePointer();
                        //将偏移量写入到位置文件中
                        FileUtils.writeStringToFile(positionFile, offset + "");
                    } else {
                        Thread.sleep(interval);
                    }
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    logger.error("read file thread interrupted", e);
                } catch (IOException e) {
                    logger.error("read log file error", e);
                }

            }
        }

        private void setFlag(boolean flag) {
            this.flag = flag;
        }
    }


}
