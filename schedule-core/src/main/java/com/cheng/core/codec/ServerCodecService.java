package com.cheng.core.codec;

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

public class ServerCodecService implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware, EnvironmentAware {

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",ServerCodecService.class);

    Map<String,MessageCodec> serverCodecMap= Maps.newConcurrentMap();


    BeanFactory beanFactory;

    Environment environment;



    /**
     * get server transport by code
     */
    public MessageCodec getCodec(String code) {
        if(serverCodecMap.isEmpty()) {
            synchronized (ServerCodecService.class) {
                Map<String, MessageCodec> beansOfType = ((DefaultListableBeanFactory) beanFactory).getBeansOfType(MessageCodec.class);
                if(MapUtils.isNotEmpty(beansOfType))
                {
                    beansOfType.values().stream().forEach(bean->{
                        serverCodecMap.put(bean.getCoderName(),bean);
                        logger.info("init server codec [{}] ",bean.getCoderName());
                    });
                }
            }
        }
        return serverCodecMap.get(code);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, environment);
        //扫描 序列化包
        String basePackage = "com.cheng.core.codec";
        TypeFilter typeFilter = new AssignableTypeFilter(MessageCodec.class);
        scanner.addIncludeFilter(typeFilter);
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
        if (CollectionUtils.isNotEmpty(candidateComponents)){
            candidateComponents.stream().forEach(component -> {
                String beanClassName = component.getBeanClassName();
                beanDefinitionRegistry.registerBeanDefinition(beanClassName, component);
                MessageCodec codec = beanFactory.getBean(beanClassName, MessageCodec.class);
                logger.info("load codec [{}] success",codec.getCoderName());
                serverCodecMap.put(codec.getCoderName(), codec);

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
