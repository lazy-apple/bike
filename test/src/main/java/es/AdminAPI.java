package es;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by zx on 2017/9/6.
 */
public class AdminAPI {

    private TransportClient client = null;

    //在所有的测试方法之前执行
    @Before
    public void init() throws Exception {
        //设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "my-es").build();
        //创建client
        client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName("192.168.100.211"), 9300),
                new InetSocketTransportAddress(InetAddress.getByName("192.168.100.212"), 9300),
                new InetSocketTransportAddress(InetAddress.getByName("192.168.100.213"), 9300));

    }

    //创建索引，并配置一些参数
    @Test
    public void createIndexWithSettings() {
        //获取Admin的API
        AdminClient admin = client.admin();
        //使用Admin API对索引进行操作
        IndicesAdminClient indices = admin.indices();
        //准备创建索引
        indices.prepareCreate("gamelog")
                //配置索引参数
                .setSettings(
                        //参数配置器
                        Settings.builder()//指定索引分区的数量
                        .put("index.number_of_shards", 4)
                                //指定索引副本的数量（注意：不包括本身，如果设置数据存储副本为2，实际上数据存储了3份）
                        .put("index.number_of_replicas", 2)
                )
                //真正执行
                .get();
    }

    //跟索引添加mapping信息（给表添加schema信息）
    @Test
    public void putMapping() {
        //创建索引
        client.admin().indices().prepareCreate("twitter")
                //创建一个type，并指定type中属性的名字和类型
                .addMapping("tweet",
                        "{\n" +
                                "    \"tweet\": {\n" +
                                "      \"properties\": {\n" +
                                "        \"message\": {\n" +
                                "          \"type\": \"string\"\n" +
                                "        }\n" +
                                "      }\n" +
                                "    }\n" +
                                "  }")
                .get();
    }

    /**
     * 你可以通过dynamic设置来控制这一行为，它能够接受以下的选项：
     * true：默认值。动态添加字段
     * false：忽略新字段
     * strict：如果碰到陌生字段，抛出异常
     * @throws IOException
     */
    @Test
    public void testSettingsMappings() throws IOException {
        //1:settings
        HashMap<String, Object> settings_map = new HashMap<String, Object>(2);
        settings_map.put("number_of_shards", 3);
        settings_map.put("number_of_replicas", 2);

        //2:mappings（映射、schema）
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .field("dynamic", "true")
                    //设置type中的属性
                    .startObject("properties")
                        //id属性
                        .startObject("num")
                            //类型是integer
                            .field("type", "integer")
                            //不分词，但是建索引
                            .field("index", "not_analyzed")
                            //在文档中存储
                            .field("store", "yes")
                        .endObject()
                        //name属性
                        .startObject("name")
                            //string类型
                            .field("type", "string")
                            //在文档中存储
                            .field("store", "yes")
                            //建立索引
                            .field("index", "analyzed")
                            //使用ik_smart进行分词
                            .field("analyzer", "ik_smart")
                        .endObject()
                    .endObject()
                .endObject();

        CreateIndexRequestBuilder prepareCreate = client.admin().indices().prepareCreate("user_info");
        //管理索引（user_info）然后关联type（user）
        prepareCreate.setSettings(settings_map).addMapping("user", builder).get();
    }

    /**
     * XContentBuilder mapping = jsonBuilder()
     .startObject()
     .startObject("productIndex")
     .startObject("properties")
     .startObject("title").field("type", "string").field("store", "yes").endObject()
     .startObject("description").field("type", "string").field("index", "not_analyzed").endObject()
     .startObject("price").field("type", "double").endObject()
     .startObject("onSale").field("type", "boolean").endObject()
     .startObject("type").field("type", "integer").endObject()
     .startObject("createDate").field("type", "date").endObject()
     .endObject()
     .endObject()
     .endObject();
     PutMappingRequest mappingRequest = Requests.putMappingRequest("productIndex").type("productIndex").source(mapping);
     client.admin().indices().putMapping(mappingRequest).actionGet();
     */


    /**
     * index这个属性，no代表不建索引
     * not_analyzed，建索引不分词
     * analyzed 即分词，又建立索引
     * expected [no], [not_analyzed] or [analyzed]
     * @throws IOException
     */

    @Test
    public void testSettingsPlayerMappings() throws IOException {
        //1:settings
        HashMap<String, Object> settings_map = new HashMap<String, Object>(2);
        settings_map.put("number_of_shards", 3);
        settings_map.put("number_of_replicas", 1);

        //2:mappings
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()//
                    .field("dynamic", "true")
                    .startObject("properties")
                        .startObject("id")
                            .field("type", "integer")
                            .field("store", "yes")
                        .endObject()
                        .startObject("name")
                            .field("type", "string")
                            .field("index", "not_analyzed")
                        .endObject()
                        .startObject("age")
                            .field("type", "integer")
                        .endObject()
                        .startObject("salary")
                            .field("type", "integer")
                        .endObject()
                        .startObject("team")
                            .field("type", "string")
                            .field("index", "not_analyzed")
                        .endObject()
                        .startObject("position")
                            .field("type", "string")
                            .field("index", "not_analyzed")
                        .endObject()
                        .startObject("description")
                            .field("type", "string")
                            .field("store", "no")
                            .field("index", "analyzed")
                            .field("analyzer", "ik_smart")
                        .endObject()
                        .startObject("addr")
                            .field("type", "string")
                            .field("store", "yes")
                            .field("index", "analyzed")
                            .field("analyzer", "ik_smart")
                        .endObject()
                    .endObject()
                .endObject();

        CreateIndexRequestBuilder prepareCreate = client.admin().indices().prepareCreate("player_info");
        prepareCreate.setSettings(settings_map).addMapping("player", builder).get();

    }
}
