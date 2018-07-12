package org.ljx.listener;

import org.ljx.quartz.QuartzManager;
import org.ljx.quartz.SendCheck;
import org.ljx.quartz.Settlement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by ljx on 2018/6/22.
 */
@WebListener
public class StartListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        QuartzManager.addJob("sendCheck",SendCheck.class,"0 40 23 * * ?");   //添加定时统计日志定时器
        QuartzManager.addJob("settlement",Settlement.class,"5 0 0 * * ?");   //添加定时结算定时器
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        QuartzManager.shutdownJobs();
    }
}
