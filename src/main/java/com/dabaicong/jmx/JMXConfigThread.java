package com.dabaicong.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;

/**
 * Created by liuchong on 2018/3/16.
 * Copyright(c) 2018 WISDOM LETOUR Co., Ltd.
 * All right reserved.
 */
public class JMXConfigThread implements JMXConfigBean {

    private String name;
    private int maxConsumer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxConsumer() {
        return maxConsumer;
    }

    public void setMaxConsumer(int maxConsumer) {
        this.maxConsumer = maxConsumer;
    }

    @Override
    public Object getAttribute(String attribute) {
        return null;
    }

    @Override
    public void setAttribute(Attribute attribute) {

    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return null;
    }
}
