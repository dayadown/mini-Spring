package com.my.test.ioc;

import cn.hutool.core.io.IoUtil;
import com.my.beans.PropertyValue;
import com.my.beans.PropertyValues;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanReference;
import com.my.beans.factory.support.DefaultListableBeanFactory;
import com.my.beans.factory.xml.XmlBeanDefinitionReader;
import com.my.context.support.ClassPathXmlApplicationContext;
import com.my.test.ioc.common.event.CustomEvent;
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

    /**
     * 测试ApplicationContext通过xml自动配置bean信息后处理和bean后处理
     * @throws Exception
     */
    @Test
    public void testApplicationContext() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
        //name属性在CustomBeanFactoryPostProcessor中被修改为ivy

        Car car = applicationContext.getBean("car", Car.class);
        System.out.println(car);
        //brand属性在CustomerBeanPostProcessor中被修改为lamborghini
    }

    /**
     * 测试初始化销毁方法和自定义初始化销毁方法
     * @throws Exception
     */
    @Test
    public void testInitAndDestroyMethod() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");
        //注册销毁钩子函数，在JVM正式关闭之前（也就是bean被销毁之前）执行销毁方法
        applicationContext.registerShutdownHook();
    }


    /**
     * 测试Aware接口
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        HelloServiceAware helloServiceAware = applicationContext.getBean("helloServiceAware", HelloServiceAware.class);
    }

    /**
     * 测试原型bean和单例bean
     */
    @Test
    public void testPrototype() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:prototype-bean.xml");

        Car car1 = applicationContext.getBean("car", Car.class);
        Car car2 = applicationContext.getBean("car", Car.class);
        System.out.println(car1==car2);
        //将prototype-bean.xml中的car的scope属性改为singleton再次测试观察输出
    }

    /**
     * 测试FactoryBean的创建，单例及原型
     * @throws Exception
     */
    @Test
    public void testFactoryBean() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");

        // 明明配置的bean是com.my.test.ioc.CarFactoryBean，但是返回的是Car
        // 单例池中存在com.my.test.ioc.CarFactoryBean的实例，在查询的返回处做了手脚，如果是FactoryBean，不返回查到的这个bean
        // 而是返回其内部方法创造的bean
        Car car = (Car)applicationContext.getBean("carFactoryBean");
        System.out.println(car);

        Car car1=(Car)applicationContext.getBean("carFactoryBean");
        System.out.println(car1==car);
        //将CarFactoryBean中isSingleton返回值改为false，再次测试


        //测试FactoryBean为原型时，是否影响内部创建bean的单例性,factory-bean.xml中加入scope="prototype"，再次测试
        //不影响，因为是根据<beanName，内部创建bean>来缓存的，根据beanName查，而不是bean的实例

    }

    @Test
    public void testEventListener() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event-and-event-listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));

        applicationContext.registerShutdownHook();//或者applicationContext.close()主动关闭容器;
    }


}