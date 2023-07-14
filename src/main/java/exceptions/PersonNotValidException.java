package exceptions;

import srl.ios.readiniprops.model.Person;

public class PersonNotValidException extends Exception{
    public PersonNotValidException(Person processed) {
        super(processed.toString() +" contains too many errors and will not be inserted.");
    }
}
