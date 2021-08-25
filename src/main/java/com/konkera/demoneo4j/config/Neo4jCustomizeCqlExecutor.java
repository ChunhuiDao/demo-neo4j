package com.konkera.demoneo4j.config;

import com.konkera.demoneo4j.utils.BeanLocator;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;
import org.neo4j.driver.internal.InternalResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author konkera
 * @date 2021/8/25
 */
@Slf4j
@Component
public class Neo4jCustomizeCqlExecutor implements InitializingBean {
    @Value("${spring.neo4j.uri}")
    private String uri;
    @Value("${spring.neo4j.authentication.username}")
    private String username;
    @Value("${spring.neo4j.authentication.password}")
    private String password;

    private Driver driver;

    @Override
    public void afterPropertiesSet() {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }

    /**
     * 无返回值数据库操作cql，一般情况下不使用此方式操作数据库
     * 查询数据请勿使用该方法
     *
     * @param cql
     */
    public static void executeCql(String cql) {
        log.info(cql);
        Session session = BeanLocator.getBean(Neo4jCustomizeCqlExecutor.class).driver.session();
        session.run(cql);
        session.close();
    }

}
