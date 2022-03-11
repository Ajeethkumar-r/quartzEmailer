package com.quartzdemo.quartz_emailsender.job;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class EmailJob extends QuartzJobBean {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    // this method wokrs when the trigger start to trigger the job
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String reciptentemail = jobDataMap.getString("email");
        
        sendMail(mailProperties.getUsername(), reciptentemail, subject, body);
    }

    public void sendMail( String fromEmail, String toEmail, String subject, String body ){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);

            mailSender.send(mimeMessage);

        } catch (MessagingException me) {
            System.out.println(me);
        }
    }
    
}