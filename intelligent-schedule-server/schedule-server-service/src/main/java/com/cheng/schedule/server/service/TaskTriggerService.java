package com.cheng.schedule.server.service;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.schedule.server.trigger.Trigger;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;


@Component
public class TaskTriggerService implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware, EnvironmentAware {

	private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE-SERVER",TaskTriggerService.class);

	Map<String, Trigger> triggerMap = Maps.newConcurrentMap();

	BeanFactory beanFactory;
	Environment environment;

	/**
	 * get Trigger by code
	 */
	public Trigger getTrigger(String code) {
		if (triggerMap.isEmpty()) {
			synchronized (TaskTriggerService.class) {
				Map<String, Trigger> beansOfType = ((DefaultListableBeanFactory) beanFactory)
						.getBeansOfType(Trigger.class);
				if (MapUtils.isNotEmpty(beansOfType)) {
					beansOfType.values().stream().forEach(bean -> {
						triggerMap.put(bean.getTrigerName(), bean);
						logger.info("init trigger info {} ", bean.getTrigerName());
					});
				}
			}
		}
		return triggerMap.get(code);
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false,
				environment);
		String basePackage = "com.webank.shopos.schedule.center.trigger";
		TypeFilter typeFilter = new AssignableTypeFilter(Trigger.class);
		scanner.addIncludeFilter(typeFilter);
		Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
		if (CollectionUtils.isNotEmpty(candidateComponents)) {
			candidateComponents.stream().forEach(component -> {
				String beanClassName = component.getBeanClassName();
				beanDefinitionRegistry.registerBeanDefinition(beanClassName, component);
				Trigger trigger = beanFactory.getBean(beanClassName, Trigger.class);
				logger.info("load trigger [{}] success", trigger.getTrigerName());
				triggerMap.put(trigger.getTrigerName(), trigger);
			});
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
			throws BeansException {
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
