package com.springboot.workflow.config;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by zx
 */
@Configuration
public class WorkflowConfig {

    @Bean
    public WorkflowIdGenerator idGenerator() {
        return new WorkflowIdGenerator();
    }

    @Bean
    public ProcessEngine processEngine(DataSourceTransactionManager transactionManager, DataSource dataSource) throws IOException {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        //自动部署已有的流程文件
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "processes/*.bpmn20.xml");
        configuration.setTransactionManager(transactionManager);
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setDeploymentResources(resources);
        configuration.setDbIdentityUsed(false);
        return configuration.buildProcessEngine();
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfigurationImpl, WorkflowIdGenerator workflowIdGenerator) {
        processEngineConfigurationImpl.setIdGenerator(workflowIdGenerator);
        processEngineConfigurationImpl.getDbSqlSessionFactory().setIdGenerator(workflowIdGenerator);
        return processEngineConfigurationImpl;
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }


//    <!-- 配置事务管理器 -->
//    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
//        <property name="entityManagerFactory" ref="entityManagerFactory" />
//        <property name="jpaDialect">
//            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaDialect" />
//        </property>
//    </bean>
//    <!-- 启用 annotation事务 -->
//    <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true" />

    //RepositoryService	管理流程定义
    //RuntimeService	执行管理，包括启动、推进、删除流程实例等操作
    //TaskService	任务管理
    //HistoryService	历史管理(执行完的数据的管理)
    //IdentityService	组织机构管理
    //FormService	一个可选服务，任务表单管理
    //ManagerService

    //3.3.1：资源库流程规则表
    //1)act_re_deployment 	部署信息表
    //2)act_re_model  		流程设计模型部署表
    //3)act_re_procdef  		流程定义数据表
    //3.3.2：运行时数据库表
    //1)act_ru_execution		运行时流程执行实例表
    //2)act_ru_identitylink		运行时流程人员表，主要存储任务节点与参与者的相关信息
    //3)act_ru_task			运行时任务节点表
    //4)act_ru_variable		运行时流程变量数据表
    //3.3.3：历史数据库表
    //1)act_hi_actinst 		历史节点表
    //2)act_hi_attachment		历史附件表
    //3)act_hi_comment		历史意见表
    //4)act_hi_identitylink		历史流程人员表
    //5)act_hi_detail			历史详情表，提供历史变量的查询
    //6)act_hi_procinst		历史流程实例表
    //7)act_hi_taskinst		历史任务实例表
    //8)act_hi_varinst			历史变量表
    //3.3.4：组织机构表
    //1)act_id_group		用户组信息表
    //2)act_id_info			用户扩展信息表
    //3)act_id_membership	用户与用户组对应信息表
    //4)act_id_user			用户信息表
}
