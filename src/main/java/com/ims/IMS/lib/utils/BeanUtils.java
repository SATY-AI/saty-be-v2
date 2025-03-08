package com.ims.IMS.lib.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public final class BeanUtils {
    private static ApplicationContext context;

    private BeanUtils(ApplicationContext context) {
        BeanUtils.context = context;
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return context.getBean(clazz);
    }
}