package com.springCloud.test.config;

import com.github.pagehelper.PageHelper;
import com.springCloud.test.util.Serializer;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Properties;

//@Component
//@EnableTransactionManagement
public class DataSourceConfig {

//    @Value("mybatis.datasource.driver")
//    private String driver;
//    @Value("mybatis.datasource.url")
//    private String url;
//    @Value("mybatis.datasource.username")
//    private String username;
//    @Value("mybatis.datasource.password")
//    private String password;
//
//    @Bean
//    public PooledDataSource getPooledDataSource() {
//        PooledDataSource pds = new PooledDataSource();
//        pds.setDriver(driver);
//        pds.setUrl(url);
//        pds.setUsername(username);
//        pds.setPassword(password);
//        pds.setPoolPingEnabled(true);
//        pds.setPoolPingQuery("SELECT 1");
//        pds.setPoolPingConnectionsNotUsedFor(900000);
//        return pds;
//    }
//
//    @Bean
//    PageHelper getPageHelper(){
//        //分页插件
//        PageHelper pageHelper = new PageHelper();
//        Properties properties = new Properties();
//        properties.setProperty("helperDialect", "mysql");
////        properties.setProperty("reasonable", "true");
////        properties.setProperty("supportMethodsArguments", "true");
////        properties.setProperty("returnPageInfo", "check");
////        properties.setProperty("params", "count=countSql");
//        pageHelper.setProperties(properties);
//        return pageHelper;
//    }
//
//    @Bean
//    public Resource[] getResource() {
//        Resource[] resources = new Resource[0];
//        try {
//            resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*Mapper.xml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        扫描sql配置文件:mapper需要的xml文件
////        ssf.setMapperLocations(resources);
//        return resources;
//    }
//
//    @Bean
//    @Primary
//    public SqlSessionFactoryBean getSqlSessionFactoryBean() throws IOException {
//        SqlSessionFactoryBean ssf = new SqlSessionFactoryBean();
//        Configuration configuration = new Configuration();
//        configuration.setMapUnderscoreToCamelCase(true);
//        ssf.setConfiguration(configuration);
//        ssf.setDataSource(getPooledDataSource());
//        ssf.setTypeAliasesPackage("com.springCloud.test.entity");
//        //添加插件
//        ssf.setPlugins(new Interceptor[]{getPageHelper()});
//        return ssf;
//    }
//
//    @Bean
//    public DataSourceTransactionManager getDataSourceTransactionManager() {
//        DataSourceTransactionManager dtm = new DataSourceTransactionManager(getPooledDataSource());
//        dtm.setRollbackOnCommitFailure(true);
//        return dtm;
//    }
}
