package com.my.test.ioc;

import cn.hutool.core.io.IoUtil;
import com.my.beans.PropertyValue;
import com.my.beans.PropertyValues;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanReference;
import com.my.beans.factory.support.DefaultListableBeanFactory;
import com.my.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.Test;
import com.my.core.io.*;
import com.my.test.ioc.common.*;

import java.io.InputStream;

/**
 * ioc测试
 */
public class BeanTest {

    /**
     *测试添加bean信息到bean容器中，并获取该bean
     */
    @Test
    public void testBeanFactory() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        helloService.sayHello();
    }

    /**
     * 测试属性注入
     * @throws Exception
     */
    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "derek"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
    }

    /**
     * 测试注入的属性为bean
     * @throws Exception
     */
    @Test
    public void testPopulateBeanWithBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //注册Car实例
        PropertyValues propertyValuesForCar = new PropertyValues();
        propertyValuesForCar.addPropertyValue(new PropertyValue("brand", "porsche"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, propertyValuesForCar);
        beanFactory.registerBeanDefinition("car", carBeanDefinition);

        //注册Person实例
        PropertyValues propertyValuesForPerson = new PropertyValues();
        propertyValuesForPerson.addPropertyValue(new PropertyValue("name", "derek"));
        propertyValuesForPerson.addPropertyValue(new PropertyValue("age", 18));
        //Person实例依赖Car实例
        propertyValuesForPerson.addPropertyValue(new PropertyValue("car", new BeanReference("car")));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValuesForPerson);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        //直接get名为person的bean会先去创建名为car的bean
        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        System.out.println(person.getCar());
    }


    /**
     * 资源加载测试
     * @throws Exception
     */
    @Test
    public void testResourceLoader() throws Exception {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

        //加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);

        //加载文件系统资源
        resource = resourceLoader.getResource("src/main/resources/hello.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);

        //加载url资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    /**
     * 测试从xml中读取bean信息放入bean容器中
     */
    @Test
    public void testXmlFile() throws Exception {
        //创建bean容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //创建xml读取器，绑定读取的bean信息由bean容器注册
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        //读取信息
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);


        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);
    }

    /**
     * 测试BeanFactoryPostProcessor的更改bean信息的功能
     */
    @Test
    public void testBeanFactoryPostProcessor() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //在所有BeanDefintion加载完成后，但在bean实例化之前，修改BeanDefinition的属性值
        CustomBeanFactoryPostProcessor beanFactoryPostProcessor = new CustomBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);


        //name属性在CustomBeanFactoryPostProcessor中被修改为ivy
        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
    }

    /**
     * 测试BeanPostProcessor的更改bean实例对象的功能
     * @throws Exception
     */
    @Test
    public void testBeanPostProcessor() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //添加bean实例化后的处理器
        CustomerBeanPostProcessor customerBeanPostProcessor = new CustomerBeanPostProcessor();
        beanFactory.addBeanPostProcessor(customerBeanPostProcessor);

        //brand属性在CustomerBeanPostProcessor中被修改为lamborghini
        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);
    }
}