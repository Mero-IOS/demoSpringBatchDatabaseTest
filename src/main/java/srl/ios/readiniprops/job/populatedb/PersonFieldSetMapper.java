package srl.ios.readiniprops.job.populatedb;

import jakarta.annotation.Nonnull;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import srl.ios.readiniprops.model.Person;

@Component
public class PersonFieldSetMapper implements FieldSetMapper<Person> {
    @Override
    @Nonnull
    public Person mapFieldSet(@Nonnull FieldSet fieldSet) {
        return Person.builder().
                name(fieldSet.readString("name"))
                .mail(fieldSet.readString("mail"))
                .phoneNumber(fieldSet.readString("phonenumber"))
                .birthDate(ParseUtils.parseDate(fieldSet.readString("birth")))
                .build();
    }
}
