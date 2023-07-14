package srl.ios.readiniprops.config;

import org.ini4j.Ini;
import org.ini4j.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws IOException{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.ini")) {
            Ini ini = new Ini(inputStream);
            Profile.Section section = ini.get("mysql");
            return DataSourceBuilder.create()
                    .url(section.get("jdbcUrl"))
                    .username(section.get("username"))
                    .password(section.get("password"))
                    .driverClassName(section.get("driverclass"))
                    .build();
        }
    }

    @Bean
    Resource csvData(@Value("${csv.path}") Resource csvData){
        return csvData;
    }

}
