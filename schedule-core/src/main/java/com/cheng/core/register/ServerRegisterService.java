package com.cheng.core.register;

import com.cheng.logger.BusinessLoggerFactory;
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

import java.util.Map;
import java.util.Set;

public class ServerRegisterService implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, BeanFactoryAware {
    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",ServerRegisterService.class);

    Map<String, ServerRegister> serverRegisterMap = Maps.newConcurrentMap();

    BeanFactory beanFactory;
    Environment environment;

    /**
     * 根据注册中心code码获取注册中心
     * @param registryCode
     * @return
     */
    public ServerRegister getServerRegisterByCode(String registryCode){
        if(serverRegisterMap.isEmpty()) {
            synchronized (ServerRegisterService.class) {
                Map<String, ServerRegister> beansOfType = ((DefaultListableBeanFactory) beanFactory).getBeansOfType(ServerRegister.class);
                if(MapUtils.isNotEmpty(beansOfType))
                {
                    beansOfType.values().stream().forEach(bean->{
                        serverRegisterMap.put(bean.registerCode(),bean);
                        logger.info("init server register [{}] ",bean.registerCode());
                    });
                }
            }
        }
        return serverRegisterMap.get(registryCode);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, environment);
        String basePackage = "com.cheng.registry";
        TypeFilter typeFilter = new AssignableTypeFilter(ServerRegister.class);
        scanner.addIncludeFilter(typeFilter);
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
        if (CollectionUtils.isNotEmpty(candidateComponents)){
            candidateComponents.stream().forEach(component->{
                String beanClassName = component.getBeanClassName();
                beanDefinitionRegistry.registerBeanDefinition(beanClassName,component);
        });
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }





}
