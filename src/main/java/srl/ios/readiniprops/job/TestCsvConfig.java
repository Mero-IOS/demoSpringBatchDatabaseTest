package srl.ios.readiniprops.job;

import jakarta.annotation.Nonnull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

@Configuration
public class TestCsvConfig {

    @Bean
    Job testCsvJob(JobRepository jobRepository, Step testCsvStep){
        return new JobBuilder("testCsvJob", jobRepository).start(testCsvStep).build();
    }

    @Bean
    Step testCsvStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Resource csvData) throws IOException {
        String[] lines  = lines(csvData);
        return new StepBuilder("testCsvData", jobRepository)
                .<String,String>chunk(10, platformTransactionManager)
                .reader( new ListItemReader<>(Arrays.asList(lines)))
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(@Nonnull Chunk<? extends String> chunk) throws Exception {
                        var tenRows = chunk.getItems();
                        System.out.println(tenRows);
                    }
                })
                .build();
    }

    private static String[] lines(Resource csvData) throws IOException {
            try( var reader = new InputStreamReader(csvData.getInputStream())){
                var string = FileCopyUtils.copyToString(reader);
                var lines =string.split((System.lineSeparator()));
                System.out.println("there are "+lines.length +"rows");
                return lines;
            }
    }

}
