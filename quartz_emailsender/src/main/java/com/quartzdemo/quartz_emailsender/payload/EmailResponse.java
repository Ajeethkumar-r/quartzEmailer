package com.quartzdemo.quartz_emailsender.payload;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude( JsonInclude.Include.NON_NULL )
public class EmailResponse {
    
    private boolean success;
    
    private String jobId;
    
    private String jobGroup;
    
    private String message;  

    // this constructor helps us to get only the response from the server
    public EmailResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // This construtor helps us to access all the data which need as a response and regarding to the job
    public EmailResponse(boolean success, String jobId, String jobGroup, String message) {
        this.success = success;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;
    }
    
    

    /**
     * @return boolean return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return String return the jobId
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * @return String return the jobGroup
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * @param jobGroup the jobGroup to set
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * @return String return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
