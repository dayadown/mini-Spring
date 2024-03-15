package com.my.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.my.beans.BeansException;
import com.my.beans.PropertyValue;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanReference;
import com.my.beans.factory.support.AbstractBeanDefinitionReader;
import com.my.beans.factory.support.BeanDefinitionRegistry;
import com.my.core.io.Resource;
import com.my.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * 定义在xml中的bean的读取器
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public static final String BEAN_ELEMENT = "bean";
	public static final String PROPERTY_ELEMENT = "property";
	public static final String ID_ATTRIBUTE = "id";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String CLASS_ATTRIBUTE = "class";
	public static final String VALUE_ATTRIBUTE = "value";
	public static final String REF_ATTRIBUTE = "ref";
	static final String INIT_METHOD_ATTRIBUTE = "init-method";
	public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

	/**
	 * 注册bean信息注册器，这里只有DefaultListableBeanFactory实现了BeanDefinitionRegistry
	 * 所以其实这里就是把bean容器的方法关于bean信息容器的方法暴露给了这个读取器
	 * @param registry
	 */
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}


	/**
	 * 注册bean信息注册器和资源加载器
	 * @param registry
	 * @param resourceLoader
	 */
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		super(registry, resourceLoader);
	}

	/**
	 * 递归解析资源地址字符串
	 * @param location
	 * @throws BeansException
	 */
	@Override
	public void loadBeanDefinitions(String location) throws BeansException {
		ResourceLoader resourceLoader = getResourceLoader();
		//获取资源
		Resource resource = resourceLoader.getResource(location);
		//解析资源
		loadBeanDefinitions(resource);
	}

	/**
	 * 解析资源
	 * @param resource
	 * @throws BeansException
	 */
	@Override
	public void loadBeanDefinitions(Resource resource) throws BeansException {
		try {
			InputStream inputStream = resource.getInputStream();
			try {
				doLoadBeanDefinitions(inputStream);
			} finally {
				inputStream.close();
			}
		} catch (IOException ex) {
			throw new BeansException("IOException parsing XML document from " + resource, ex);
		}
	}

	/**
	 * 加载资源中的bean信息到bean信息容器中
	 * @param inputStream
	 */
	protected void doLoadBeanDefinitions(InputStream inputStream) {
		Document document = XmlUtil.readXML(inputStream);
		//解析XML文件，读取根标签
		Element root = document.getDocumentElement();
		//读取所有子标签
		NodeList childNodes = root.getChildNodes();
		//遍历根标签的所有子标签
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i) instanceof Element) {
				if (BEAN_ELEMENT.equals(((Element) childNodes.item(i)).getNodeName())) {
					//解析bean标签
					Element bean = (Element) childNodes.item(i);
					//从标签内容中读取字段，即bean信息
					String id = bean.getAttribute(ID_ATTRIBUTE);
					String name = bean.getAttribute(NAME_ATTRIBUTE);
					String className = bean.getAttribute(CLASS_ATTRIBUTE);
					String initMethodName = bean.getAttribute(INIT_METHOD_ATTRIBUTE);
					String destroyMethodName = bean.getAttribute(DESTROY_METHOD_ATTRIBUTE);

					Class<?> clazz = null;
					try {
						clazz = Class.forName(className);
					} catch (ClassNotFoundException e) {
						throw new BeansException("Cannot find class [" + className + "]");
					}
					//解析bean名称信息，id优先于name
					String beanName = StrUtil.isNotEmpty(id) ? id : name;
					if (StrUtil.isEmpty(beanName)) {
						//如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
						beanName = StrUtil.lowerFirst(clazz.getSimpleName());
					}

					//创建bean信息实例，并将Class放入
					BeanDefinition beanDefinition = new BeanDefinition(clazz);
					//将自定义初始和销毁方法的方法名放入
					beanDefinition.setInitMethodName(initMethodName);
					beanDefinition.setDestroyMethodName(destroyMethodName);

					//解析属于这个bean的<property>,遍历bean标签的子标签
					for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
						if (bean.getChildNodes().item(j) instanceof Element) {
							if (PROPERTY_ELEMENT.equals(((Element) bean.getChildNodes().item(j)).getNodeName())) {
								//解析property标签
								Element property = (Element) bean.getChildNodes().item(j);
								//属性名
								String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
								//属性值
								String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
								//如果是另一个bean则该处为另一个bean的名字
								String refAttribute = property.getAttribute(REF_ATTRIBUTE);

								if (StrUtil.isEmpty(nameAttribute)) {
									throw new BeansException("The name attribute cannot be null or empty");
								}

								Object value = valueAttribute;
								if (StrUtil.isNotEmpty(refAttribute)) {
									value = new BeanReference(refAttribute);
								}
								//创建属性信息
								PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
								//bean信息实例中加入该属性信息
								beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
							}
						}
					}
					if (getRegistry().containsBeanDefinition(beanName)) {
						//beanName不能重名
						throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
					}
					//注册BeanDefinition
					getRegistry().registerBeanDefinition(beanName, beanDefinition);
				}
			}
		}
	}
}
