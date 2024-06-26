package com.springboot.examplework.example.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    // 每5秒執行一次
    @Scheduled(fixedRate = 10000)
    public void performTaskWithFixedRate() {
        System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
    }

    // 任務完成後延遲2秒再執行
    @Scheduled(fixedDelay = 5000)
    public void performTaskWithFixedDelay() {
        System.out.println("Fixed delay task - " + System.currentTimeMillis() / 1000);
    }

    // 使用Cron表達式，每分鐘的第10秒執行一次
    @Scheduled(cron = "10 * * * * *")
    public void performTaskWithCronExpression() {
        System.out.println("Cron expression task - " + System.currentTimeMillis() / 1000);
    }
}
