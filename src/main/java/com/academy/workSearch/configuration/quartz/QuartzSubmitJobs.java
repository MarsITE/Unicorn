package com.academy.workSearch.configuration.quartz;

import com.academy.workSearch.configuration.quartz.jobs.RemoveNotImproveUserJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

//@Configuration
public class QuartzSubmitJobs {
    private static final String CRON_EVERY_DAY = "0 0 0 * * ?";
    private static final String CRON_EVERY_SECOND = "* * * ? * *";

    @Bean(name = "removeNotImproveUser")
    public JobDetailFactoryBean jobMemberClassStats() {
        return QuartzConfig.createJobDetail(RemoveNotImproveUserJob.class, "Class Statistics Job");
    }

    @Bean(name = "memberClassStatsTrigger")
    public CronTriggerFactoryBean triggerMemberClassStats(@Qualifier("removeNotImproveUser") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_SECOND, "Class Statistics Trigger");
    }


}
