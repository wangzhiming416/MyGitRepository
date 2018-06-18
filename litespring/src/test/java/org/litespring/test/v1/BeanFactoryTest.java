package org.litespring.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.service.v1.PetStoreService;



public class BeanFactoryTest {
	DefaultBeanFactory factory = null;
	XmlBeanDefinitionReader reader = null;
	@Before
	public void setUp(){
		factory = new DefaultBeanFactory();
		reader = new XmlBeanDefinitionReader(factory);
	}
	
	@Test
	public void testGetBean() {
		/*首先获取 bean 的定义   定义一个BeanFactory接口。
			DefaultBeanFactory作为BeanFactory的实例，可以解析xml配置文件,从而返回 bean 的定义BeanDefinition 
			BeanDefinition也是一个接口
			获取Bean的定义以后，要进行判断，是否和xml中的bean相同    要判断就需要Bean的定义中包含获取bean的类名的方法
		*/
		Resource resource = new ClassPathResource("petstore-v1.xml");
		reader.loadBeanDefinitions(resource);
		BeanDefinition bd = factory.getBeanDefinition("petStore");
		 
		assertTrue(bd.isSingleton());
		assertFalse(bd.isPrototype());
		assertEquals(BeanDefinition.SCOPE_DEFAULT,bd.getScope());
		
		assertEquals("org.litespring.service.v1.PetStoreService",bd.getBeanClassName());
		//测试一下实例能否从 factory 中取出  
		PetStoreService petStore =  (PetStoreService)factory.getBean("petStore");
		assertNotNull(petStore);
		//再取一次，如果相等，说明是单例的
		PetStoreService petStore1 =  (PetStoreService)factory.getBean("petStore");
		assertTrue(petStore.equals(petStore1));
	}
	/**
	 * 测试无效的bean
	 */
	@Test
	public void testInvalidBean(){
		Resource resource = new ClassPathResource("petstore-v1.xml");
		reader.loadBeanDefinitions(resource);
		try {
			factory.getBean("invalidBean");
		} catch (BeanCreationException e) {
			return;
		}
		Assert.fail("Expect BeanCreationException");
	}
	
	/**
	 * 测试无效的xml文件
	 */
	@Test
	public void testInvalidXML(){
		try {
			Resource resource = new ClassPathResource("xxx-v1.xml");
			reader.loadBeanDefinitions(resource);
		} catch (BeanDefinitionStoreException e) {
			return;
		}
		Assert.fail("expect BeanDefinitionStoreException");
	}
	
}
