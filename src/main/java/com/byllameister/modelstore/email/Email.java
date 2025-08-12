package com.byllameister.modelstore.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String recipientEmail;
    private String subject;
    private String body;
}
