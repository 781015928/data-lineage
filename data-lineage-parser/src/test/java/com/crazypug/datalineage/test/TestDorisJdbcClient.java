
package com.crazypug.datalineage.test;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.crazypug.data.v1.jdbc.JdbcClientImpl;
import com.crazypug.datalineage.core.Bean;

import javax.sql.DataSource;
import java.util.Properties;


@Bean
public class TestDorisJdbcClient extends JdbcClientImpl {

    protected TestDorisJdbcClient(DataSource dataSource) {
        super(dataSource);
    }

    private static DataSource dataSource;

    static {
        Properties properties = new Properties();

        properties.setProperty(DruidDataSourceFactory.PROP_URL, "jdbc:mysql://172.24.28.17:9030/edw_klw?tinyInt1isBit=false&rewriteBatchedStatements=true&useSSL=false&allowPublicKeyRetrieval=true");
        properties.setProperty(DruidDataSourceFactory.PROP_USERNAME, "edw");
        properties.setProperty(DruidDataSourceFactory.PROP_PASSWORD, "edw123456");
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("doris据库链接初始化失败", e);
        }
    }

    public TestDorisJdbcClient() {
        super(dataSource);
    }

}
