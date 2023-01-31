package io.example.demospringbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileReadingMessageSource.WatchEventType;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

import java.io.File;

@Slf4j
@Configuration
public class IntegrationConfig {

    private static final String INPUT_DIR = "resources";

    private static final String FILE_PATTERN = "*.txt";

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job sampleJob;


    /**
     * Integration processes are constructed by composing endpoints into one or more message flows.
     * <p/>
     * By default, endpoints are automatically wired together with DirectChannel instances where the bean name
     * is based on the following pattern: <b>[IntegrationFlow.beanName].channel#[channelNameIndex]</b>.
     */
    @Bean
    public IntegrationFlow sampleFlow() {
        return IntegrationFlows
                .from(fileReadingMessageSource(), c -> c.poller(Pollers.fixedDelay(5000)))
                .transform(fileMessageToJobRequest())
                .handle(jobLaunchingMessageHandler())
                .handle(jobExecution -> System.out.println(jobExecution.getPayload()))
                .get();
    }

    @Bean
    public MessageSource<File> fileReadingMessageSource() {
        log.info("IntegrationConfig::fileReadingMessageSource");

        FileReadingMessageSource source = new FileReadingMessageSource();

        log.info("MessageSource Directory: {}", INPUT_DIR);
        source.setDirectory(new File(INPUT_DIR));
        log.info("File Pattern: {}", FILE_PATTERN);
        source.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));

        source.setUseWatchService(true);
        source.setWatchEvents(WatchEventType.CREATE);

        return source;
    }

    @Bean
    FileMessageToJobRequest fileMessageToJobRequest() {
        log.info("IntegrationConfig::fileMessageToJobRequest");

        FileMessageToJobRequest transformer = new FileMessageToJobRequest();

        log.info("{}", sampleJob);
        transformer.setJob(sampleJob);
        transformer.setFileParameterName("file_path");

        return transformer;
    }

    @Bean
    JobLaunchingMessageHandler jobLaunchingMessageHandler() {
        log.info("IntegrationConfig::jobLaunchingMessageHandler");
        // Message handler which uses strategies to convert a Message
        // into a job and a set of job parameters
        return new JobLaunchingMessageHandler(jobLauncher);
    }

}
