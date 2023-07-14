package srl.ios.readiniprops.job;

import exceptions.PersonNotValidException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import srl.ios.readiniprops.model.Person;

import java.sql.Date;

@Configuration
public class PopulateDbConfig {
    @Bean
    Job populateDbJob(JobRepository jobRepository, Step populateDbStep) {
        return new JobBuilder("populateDbJob", jobRepository).start(populateDbStep).incrementer(new RunIdIncrementer()).build();
    }

    @Bean
    Step populateDbStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                        FlatFileItemReader<Person> csvRowFlatFileItemReader, Personwriter personwriter) {
        return new StepBuilder("populateDbStep", jobRepository)
                .<Person, Person>chunk(10, platformTransactionManager)
                .reader(csvRowFlatFileItemReader)
                .writer(personwriter)
                .allowStartIfComplete(true)
                .build();
    }
    @Bean
    FlatFileItemReader<Person> csvRowFlatFileItemReader(Resource csvData) {
        return new FlatFileItemReaderBuilder<Person>()
                .resource(csvData)
                .name("csvRowFlatFileItemReader")
                .delimited()
                .delimiter(",")
                .names("name", "mail", "phone", "birth")
                .linesToSkip(1)
                .fieldSetMapper(this::parsePerson)
                .build();
    }

    Person parsePerson(FieldSet fieldSet) {
        return Person.builder()
                .name(  fieldSet.readString("name")   )
                .mail(fieldSet.readString("mail"))
                .phoneNumber(parsePhone(fieldSet.readString( "phone")))
                .birthDate(parseDate(fieldSet.readString("birth")))
                .build();
    }
    Date parseDate(String text) {
        try {
            return Date.valueOf(text);
        } catch (Exception e) {
            System.err.println(text + " not a valid date.");
            return null;
        }
    }
    String parsePhone(String text){
        String parsed = text.replaceAll("\\D+", "");
        if (parsed.isEmpty()) {
            System.err.println(text + " not a valid phoneNumber.");
            return null;
        }
        return parsed;
    }

    @Component
    public class Personwriter implements ItemWriter<Person>{
        @Override
        public void write(Chunk<? extends Person> chunk) throws Exception {
            System.out.println(chunk.getItems());
        }
    }

    public static class PersonProcessor implements ItemProcessor<FieldSet, Person>{
        private int errors = 0;

        @Override
        public Person process(FieldSet fieldSet) throws PersonNotValidException{
            Person processed = Person.builder()
                    .name(  fieldSet.readString("name")   )
                    .mail(fieldSet.readString("mail"))
                    .phoneNumber(parsePhone(fieldSet.readString( "phone")))
                    .birthDate(parseDate(fieldSet.readString("birth")))
                    .build();
            if (errors >1){
                errors =0;
                throw new PersonNotValidException(processed);
            }
            errors = 0;
            return processed;
        }
        Date parseDate(String text) {
            try {
                return Date.valueOf(text);
            } catch (Exception e) {
                System.err.println(text + " not a valid date.");
                errors++;
                return null;
            }
        }
        String parsePhone(String text){
            String parsed = text.replaceAll("\\D+", "");
            if (parsed.isEmpty()) {
                System.err.println(text + " not a valid phoneNumber.");
                errors++;
                return null;
            }
            return parsed;
        }
    }
}
