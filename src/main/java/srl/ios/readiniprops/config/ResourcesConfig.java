package srl.ios.readiniprops.config;

import org.ini4j.Ini;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ResourcesConfig {

    @Bean
    Resource csvData(@Value("${csv.path}") Resource csvData){
        return csvData;
    }
    @Bean
    Ini iniData(@Value("${ini.path}") Resource iniData) throws IOException {
        try (InputStream inputStream = iniData.getInputStream()){
            return new Ini(inputStream);
        }
    }
}
