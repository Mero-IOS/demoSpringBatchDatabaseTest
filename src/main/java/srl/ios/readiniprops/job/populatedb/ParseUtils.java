package srl.ios.readiniprops.job.populatedb;

import lombok.experimental.UtilityClass;
import org.springframework.batch.item.ParseException;

import java.sql.Date;

@UtilityClass
public class ParseUtils {
    static Date parseDate(String birthdate){
        try {
            return Date.valueOf(birthdate);
        } catch (ParseException e){
            return null;
        }
    }
    static String parsePhone(String phoneNumber){
        String parsed = phoneNumber.replaceAll("[^\\d]","");
        return (parsed.length()<3)?null:parsed;
    }
}
