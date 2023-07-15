package srl.ios.readiniprops.config;

import org.ini4j.Ini;
import org.ini4j.Profile;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(Ini iniData) {
        Profile.Section section = iniData.get("mysql");
        return DataSourceBuilder.create()
                .url(section.get("jdbcUrl"))
                .username(section.get("username"))
                .password(section.get("password"))
                .driverClassName(section.get("driverclass"))
                .build();
    }


}
