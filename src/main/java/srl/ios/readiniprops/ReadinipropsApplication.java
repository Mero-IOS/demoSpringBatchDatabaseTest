package srl.ios.readiniprops;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class ReadinipropsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadinipropsApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(JobLauncher jobLauncher, Job createTableJob, Job testCsvJob, Job populateDbJob) {
        return args -> {
            JobParameters params = new JobParameters();
            var run = jobLauncher.run(createTableJob, params);
            System.out.println("I said hello with this id: " + run.getJobId());
            run = jobLauncher.run(testCsvJob, params);
            System.out.println("Csv content shown with id: " + run.getJobId());
            run = jobLauncher.run(populateDbJob, params);
            System.out.println("Db populated with id: " + run.getJobId());
        };
    }
}
