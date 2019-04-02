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
 * @author LaZY(李志一)
 * @create 2019-04-02 21:10
 */
public class MySource extends AbstractSource implements EventDrivenSource, Configurable {
    private static final Logger logger = LoggerFactory.getLogger(TailFileSource.class);
    private String filePath;//读取文件的路径
    private String charset;//编码集
    private String posiFile;//偏移量保存文件
    private long interval;//读取间隔
    private ExecutorService executor;
    private FileRunnable fileRunnable;//内部类（线程）

    @Override
    public synchronized void start() {
        executor = Executors.newSingleThreadExecutor();
        fileRunnable = new FileRunnable(filePath, posiFile, interval, charset, getChannelProcessor());
        executor.submit(fileRunnable);
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

    @Override
    public void configure(Context context) {
        filePath = context.getString("filePath");
        charset = context.getString("charset", "UTF-8");
        posiFile = context.getString("posiFile");
        interval = context.getLong("interval", 1000L);
    }

    private static class FileRunnable implements Runnable {
        private long interval;
        private String charset;
        private ChannelProcessor channelProcessor;
        private long offset = 0L;//偏移量
        private RandomAccessFile raf;//读的位置
        private boolean flag = true;
        private File positionFile;

        public FileRunnable(String filePath, String posiFile, long interval, String charset, ChannelProcessor channelProcessor) {
            this.interval = interval;
            this.charset = charset;
            this.channelProcessor = channelProcessor;
            //从文件读偏移量，没有，创建新文件，有，获取当前偏移量的值，准备开始读文件。
            positionFile = new File(posiFile);
            try {
                if (!positionFile.exists()) {
                    positionFile.createNewFile();
                }
                String offsetString = FileUtils.readFileToString(positionFile);//读取整个文件转换成字符串
                if (offsetString != null && !"".equals(offsetString)) {
                    offset = Long.parseLong(offsetString);
                }
                raf = new RandomAccessFile(filePath, "r");
                raf.seek(offset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            //读一行数据，存日志，记录偏移量
            //休眠
            try {
                while (flag) {
                    String line = raf.readLine();
                    if (line != null){
                        line = new String(line.getBytes("ISO-8859-1"), charset);
                        channelProcessor.processEvent(EventBuilder.withBody(line.getBytes()));
                        offset = raf.getFilePointer();//返回当前偏移量
                        FileUtils.writeStringToFile(positionFile,offset+"");
                    }else {
                        Thread.sleep(interval);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
