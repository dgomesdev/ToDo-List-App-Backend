package com.dgomesdev.javaToDoApp.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {

            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = src.getPropertyDescriptors();
        Set<String> emptyFields = new HashSet<>();

        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Object srcValue = src.getPropertyValue(descriptor.getName());
            if (srcValue == null) emptyFields.add(descriptor.getName());
        }

        String[] result = new String[emptyFields.size()];
        return emptyFields.toArray(result);
    }
}
