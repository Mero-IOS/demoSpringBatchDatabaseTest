package srl.ios.readiniprops.job.populatedb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import srl.ios.readiniprops.model.Person;
import srl.ios.readiniprops.parsingexceptions.ParsePhoneException;

import java.net.BindException;
import java.sql.Date;

import static srl.ios.readiniprops.constants.BatchConstants.CHUNK_SIZE;

@Configuration
public class PopulateDbConfig {
    @Bean
    Job populateDbJob(JobRepository jobRepository, Step populateDbStep) {
        return new JobBuilder("populateDbJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(populateDbStep)
                .end()
                .build();
    }

    @Bean
    Step populateDbStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                        FlatFileItemReader<Person> csvRowFlatFileItemReader, Personwriter personwriter) {
        return new StepBuilder("populateDbStep", jobRepository)
                .<Person, Person>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(csvRowFlatFileItemReader)
                .processor(personProcessor())
                .writer(personwriter)
                .faultTolerant()
                .skipLimit(10)
                .skip(FlatFileParseException.class)  //implement custom exceptions.
                .skip(BindException.class)  //implement custom exceptions.
                .skip(ParsePhoneException.class)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    PersonProcessor personProcessor() {
        return new PersonProcessor();
    }

    @Bean
    @StepScope
    FlatFileItemReader<Person> csvFlatFileItemReader(Resource csvData) {
        return new FlatFileItemReaderBuilder<Person>()
                .name("flatFilePersonReader")
                .resource(csvData)
                .linesToSkip(1)
                .lineMapper(new DefaultLineMapper<>() {{
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                        {
                            setTargetType(Person.class);
                            setConversionService(new DefaultConversionService());
                        }
                    });
                    setLineTokenizer(
                            new DelimitedLineTokenizer() {
                                {
                                    setNames("name", "mail", "phoneNumber", "birthDate");
                                }
                            }
                    );
                }})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                    {
                        setTargetType(Person.class);
                    }
                })
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

    String parsePhone(String text) {
        String parsed = text.replaceAll("\\D+", "");
        if (parsed.isEmpty()) {
            System.err.println(text + " not a valid phoneNumber.");
            return null;
        }
        return parsed;
    }

    @Component
    public class Personwriter implements ItemWriter<Person> {
        @Override
        public void write(Chunk<? extends Person> chunk) throws Exception {
            System.out.println(chunk.getItems());
        }
    }
}
