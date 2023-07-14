package srl.ios.readiniprops.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder(toBuilder = true)
public class Person {
    private String  name;
    private String mail;
    private String phoneNumber;
    private Date birthDate;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mail='" + mail + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
