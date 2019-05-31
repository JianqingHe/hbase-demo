package hbasedemo.hbasedemo.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;


/**
 * 引入HBaseTemplate
 *
 * @author hejq
 * @date 2019/5/30 15:26
 */
@Configuration
public class HBaseConfig {

    @Bean
    public HbaseTemplate hbaseTemplate(@Value("${hbase.zookeeper.quorum}") String quorum,
                                       @Value("${hbase.zookeeper.property.clientPort}") String port) {
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", quorum);
        conf.set("hbase.zookeeper.port", port);
        hbaseTemplate.setConfiguration(conf);
        hbaseTemplate.setAutoFlush(true);
        return hbaseTemplate;
    }

}
