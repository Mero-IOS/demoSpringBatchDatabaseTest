package srl.ios.readiniprops.model;

import lombok.*;

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
    public Person(){}

    public Person(String name, String mail, String phoneNumber, Date birthDate) {
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }
}
