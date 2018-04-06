package com.dabaicong.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by liuchong on 2018/3/16.
 * Copyright(c) 2018 WISDOM LETOUR Co., Ltd.
 * All right reserved.
 */
public class JMXTest {

    public static void main(String[] args) {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName helloName = new ObjectName("jmxBean:name=JMXConfig");
            //create mbean and register mbean
            server.registerMBean(new JMXConfigThread(), helloName);
            Thread.sleep(60 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }
}
