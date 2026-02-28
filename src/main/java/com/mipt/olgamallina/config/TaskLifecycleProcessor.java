package com.mipt.olgamallina.config;

import com.mipt.olgamallina.repository.TaskRepository;
import com.mipt.olgamallina.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor that logs creation/initialization of TaskService and TaskRepository beans.
 */
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(TaskLifecycleProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TaskService || bean instanceof TaskRepository) {
            log.info("BEFORE_INIT beanName={} beanType={}", beanName, bean.getClass().getName());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TaskService || bean instanceof TaskRepository) {
            log.info("AFTER_INIT  beanName={} beanType={}", beanName, bean.getClass().getName());
        }
        return bean;
    }
}