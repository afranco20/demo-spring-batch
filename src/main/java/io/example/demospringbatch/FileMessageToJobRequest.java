package io.example.demospringbatch;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;
import java.util.Date;

// See http://docs.spring.io/spring-batch/trunk/reference/html/springBatchIntegration.html#launching-batch-jobs-through-messages
@Slf4j
@Setter
public class FileMessageToJobRequest {

    private Job job;
    private String fileParameterName = "input.file.name";

    @Transformer
    public JobLaunchRequest toRequest(Message<File> message) {
        log.info("FileMessageToJobRequest::toRequest");

        log.info("{}", message);
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        jobParametersBuilder.addString(fileParameterName, message.getPayload().getAbsolutePath());
        // We add a dummy value to make job params unique, or else spring batch
        // will only run it the first time
        jobParametersBuilder.addDate("dummy", new Date());

        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }

}
