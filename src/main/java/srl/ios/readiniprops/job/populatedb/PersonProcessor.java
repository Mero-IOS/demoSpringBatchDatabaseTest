package srl.ios.readiniprops.job.populatedb;

import org.springframework.batch.item.ItemProcessor;
import srl.ios.readiniprops.model.Person;
import srl.ios.readiniprops.parsingexceptions.ParsePhoneException;

public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person item) throws ParsePhoneException {
        var parsedPhone = ParseUtils.parsePhone(item.getPhoneNumber());
        if (parsedPhone.length()<=3){
            throw new ParsePhoneException(item);
        }
        return Person.builder()
                .name(item.getName())
                .mail(item.getMail())
                .phoneNumber(parsedPhone)
                .birthDate(item.getBirthDate())
                .build();
    }
}
