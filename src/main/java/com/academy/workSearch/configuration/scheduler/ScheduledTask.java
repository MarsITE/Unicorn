package com.academy.workSearch.configuration.scheduler;

import com.academy.workSearch.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    private final static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 0 * * ?")//
    public void removeNotImproveUser() {
        logger.info("Delete all not active users with expired JWT token by cron");
        userService.removeAllNotActiveUsersWithExpiredJWTToken();
    }
}
