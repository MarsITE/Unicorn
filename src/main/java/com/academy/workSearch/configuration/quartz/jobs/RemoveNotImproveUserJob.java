package com.academy.workSearch.configuration.quartz.jobs;

import com.academy.workSearch.configuration.quartz.QuartzConfig;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
//@DisallowConcurrentExecution
public class RemoveNotImproveUserJob implements Job {
    private final static Logger logger = LoggerFactory.getLogger(QuartzConfig.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
        // TODO:
        logger.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());

    }
}
