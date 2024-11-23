package com.stc.EmployeeManagement.Service;

import com.stc.EmployeeManagement.models.Mail;
import org.springframework.scheduling.annotation.Async;

public interface EmailNotificationService {
    @Async
    public void sendNotification(Mail mail, String from);
}
