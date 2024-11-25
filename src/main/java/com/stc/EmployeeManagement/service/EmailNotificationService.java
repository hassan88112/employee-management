package com.stc.EmployeeManagement.service;

import com.stc.EmployeeManagement.models.Mail;
import org.springframework.scheduling.annotation.Async;

public interface EmailNotificationService {
    @Async
    public void sendNotification(Mail mail, String from);
}
