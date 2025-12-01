package com.doctums.banner.paymentgateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


@Component
//@ConditionalOnProperty(name = "student.attribute-source", havingValue = "banner")
public class OracleTestConnector implements CommandLineRunner {

    private final DataSource dataSource;

    public OracleTestConnector(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Successfully connected to Oracle DB!");
            System.out.println("URL: " + conn.getMetaData().getURL());
            System.out.println("User: " + conn.getMetaData().getUserName());
        } catch (Exception e) {
            System.err.println("Failed to connect to Oracle DB");
            e.printStackTrace();
        }
    }
}
