package com.lxm.esaydataconnection.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * ProjectName: esay-data-connection-com.lxm.esaydataconnection.utils
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:17 2020/5/15
 * @Description:
 */
@Component
public class BeanUtils implements ApplicationContextAware {

    private BeanUtils(){}

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //通过name获取 Bean.
    public Object getBean(String name){
        return this.applicationContext.getBean(name);
    }

    //通过name,以及Clazz返回指定的Bean
    public <T> T getBean(String name,Class<T> clazz){
        return this.applicationContext.getBean(name, clazz);
    }

    public DruidDataSource  registDataSource(String beanName){
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource.class);
        // 设置属性userService,此属性引用已经定义的bean:userService,这里userService已经被spring容器管理了.
        //beanDefinitionBuilder.addPropertyReference("userService", "userService");
        // 注册bean
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
        return (DruidDataSource) this.getBean(beanName);
    }
}