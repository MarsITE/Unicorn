package com.academy.workSearch.configuration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineUtils;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class LiquibaseGenerateChangelogConfig {
    @Autowired
    private Environment env;

    @Bean
    public void generateChangeLog() {
        final String pathToChangeLog = System.getProperty("user.dir") + "/src/main/resources/db/db.changelog-master.xml";
        Database database1;
        Database database2;
        try {
            database1 = CommandLineUtils.createDatabaseObject(org.postgresql.Driver.class.getClassLoader(), env.getProperty("POSTGRES_URL"), env.getProperty("POSTGRES_LOGIN"),
                    env.getProperty("POSTGRES_PASSWORD"), "org.postgresql.Driver", null, null, false, false, null, null, null, null, null, null, null);


            database2 = CommandLineUtils.createDatabaseObject(org.postgresql.Driver.class.getClassLoader(), env.getProperty("POSTGRES_URL"), env.getProperty("POSTGRES_LOGIN"),
                    env.getProperty("POSTGRES_PASSWORD"), "org.postgresql.Driver", null, null, false, false, null, null, null, null, null, null, null);

            File sql = new File(System.getProperty("user.dir") + "/src/main/resources/db/output.sql");
            Liquibase liquibase = new Liquibase(pathToChangeLog, new FileSystemResourceAccessor(), database1);
            liquibase.update("Update", new FileWriter(sql));

            CommandLineUtils.doDiffToChangeLog(pathToChangeLog, database1, database2, new DiffOutputControl(), null, null);
        } catch (LiquibaseException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
