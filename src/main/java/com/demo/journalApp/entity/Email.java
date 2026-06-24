package com.demo.journalApp.entity;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class Email {
    private String to;
    private String subject;
    private String body;
    @Nullable
    private String cc;
    @Nullable
    private String bcc;
}
