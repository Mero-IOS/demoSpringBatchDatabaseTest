package srl.ios.readiniprops.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class CreateTableConfig {
    @Bean
    Job createTableJob(JobRepository jobRepository, Step createTableStep) {
        return new JobBuilder("createTableJob", jobRepository).start(createTableStep).build();
    }

    @Bean
    Step createTableStep(JobRepository jobRepository, Tasklet createTableTask, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createTableStep", jobRepository).tasklet(createTableTask, transactionManager).build();
    }

    @Bean
    Tasklet createTableTask(@Autowired DataSource dataSource) {
        return (contribution, context) -> {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                String createTable1Sql = """
                            CREATE TABLE people (
                            name VARCHAR(50),
                            mail VARCHAR(50),
                            phoneNumber VARCHAR(20),
                            birthDate DATE)
                        """;
                statement.executeUpdate(createTable1Sql);
                System.out.println("DataBase was Initialized.");
            }

            return RepeatStatus.FINISHED;
        };
    }

}
