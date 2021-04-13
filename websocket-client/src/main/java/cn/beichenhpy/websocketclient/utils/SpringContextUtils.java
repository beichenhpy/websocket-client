package cn.beichenhpy.websocketclient.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContextUtils implements ApplicationContextAware {

	/**
	 * 上下文对象实例
	 */
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return 返回上下文
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}



	/**
	 * 通过name获取 Bean.
	 *
	 * @param name 名字
	 * @return bean对象
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 *
	 * @param clazz 类
	 * @param <T> 为了让静态方法可以使用泛型
	 * @return 返回bean
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param name bean名字
	 * @param clazz 类
	 * @param  <T> 为了让静态方法可以使用泛型
	 * @return 返回bean
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}
}
