package com.quartzdemo.quartz_emailsender.web;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import com.quartzdemo.quartz_emailsender.payload.EmailRequest;
import com.quartzdemo.quartz_emailsender.payload.EmailResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@EnableScheduling
@RequestMapping("/api")
public class EmailSchedulerController {
    
    @Autowired
    private Scheduler scheduler;

    @PostMapping("/schedule/email")
    public ResponseEntity<EmailResponse> scheduleMail( @Valid @RequestBody EmailRequest emailRequest ){
        try {
            ZonedDateTime dateTime = ZonedDateTime.of( emailRequest.getDateTime(), emailRequest.getTimeZone() );

            if( dateTime.isBefore(ZonedDateTime.now()) ){
                EmailResponse emailResponse = new EmailResponse(false, "Please schedule the mail to future");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emailResponse);
            }
            JobDetail jobDetail = buildJobDetail(emailRequest);
            Trigger trigger = buildTrigger(jobDetail, dateTime);

            scheduler.scheduleJob(jobDetail, trigger);
            EmailResponse emailResponse = new EmailResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Mail scheduled Successfullly!!!");

            return ResponseEntity.ok(emailResponse);

        } catch (SchedulerException e) {
            System.out.printf("Schedule Error: ", e);
            EmailResponse emailResponse = new EmailResponse(false, "Email not Scheduled");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emailResponse);
        }
        
    }
    
    @GetMapping("/reaching")
        public ResponseEntity<String> onReacher(){
            return ResponseEntity.ok("I'm here buddy");
    }     

    //  Job Listener
    private JobDetail buildJobDetail( EmailRequest scheduleRequest ){
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleRequest.getEmail());
        jobDataMap.put("subject", scheduleRequest.getSubject());
        jobDataMap.put("body", scheduleRequest.getBody());

        return JobBuilder.newJob()
        .withIdentity(UUID.randomUUID().toString())
        .withDescription("my job is to send")
        .usingJobData(jobDataMap)
        .storeDurably()
        .build();
                                
    }

    // Trigger Listener
    private Trigger buildTrigger( JobDetail jobDetail, ZonedDateTime triggerStart ){
        return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(jobDetail.getKey().getName(), "email-triggers")
        .withDescription(" I'm going to trigger an email ")
        .withSchedule(CronScheduleBuilder.cronSchedule("10 0 0 ? * * *"))
        .build();
// .startAt(Date.from(triggerStart.toInstant()))
// SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()
        
    }
}
