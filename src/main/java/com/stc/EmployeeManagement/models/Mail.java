package com.stc.EmployeeManagement.models;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Mail {

    private String mailFrom;
    private String[] mailTo;
    private String[] mailCc;
    private String mailBcc;
    private String mailSubject;
    private String mailContent;
    private String contentType;

    public Mail() {
        contentType = "text/html";
    }

}