package io.example.demospringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    Job sampleJob() {
        return jobBuilderFactory.get("sampleJob")
                .incrementer(new RunIdIncrementer())
                .start(sampleStep())
                .build();
    }

    @Bean
    Step sampleStep() {
        return stepBuilderFactory.get("sampleStep")
                .<String, String>chunk(5)
                .reader(itemReader(null))
                .writer(i -> i.forEach(System.out::println))
                .build();
    }

    @Bean
    @StepScope
    FlatFileItemReader<String> itemReader(@Value("#{jobParameters['file_path']}") String filePath) {
        final FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        final FileSystemResource fileResource = new FileSystemResource(filePath);
        reader.setResource(fileResource);
        reader.setLineMapper(new PassThroughLineMapper());
        return reader;
    }

}
