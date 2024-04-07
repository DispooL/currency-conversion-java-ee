package com.conversioncurrency.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionFactory {
    //singleton instance
    private static final DatabaseConnectionFactory connectionFactory = new
            DatabaseConnectionFactory();

    private DataSource dataSource = null;

    //Make the construction private
    private DatabaseConnectionFactory() {}

    /**
     * Must be called before any other method in this class.
     * Initializes the data source and saves it in an instance
     variable
     */
    public synchronized void init() throws IOException {
        //Check if init was already called
        if (dataSource != null) {
            return;
        }

        //load db.properties file first
        InputStream inStream =
                this.getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties dbProperties = new Properties();
        dbProperties.load(inStream);
        inStream.close();

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(dbProperties.getProperty("jdbc_url"));
        config.setUsername(dbProperties.getProperty("db_username"));
        config.setPassword(dbProperties.getProperty("db_password"));
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        this.dataSource = new HikariDataSource(config);
    }

    //Provides access to singleton instance
    public static DatabaseConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    //returns database connection object
    public Connection getConnection () throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Error initializing datasource");
        }
        return dataSource.getConnection();
    }
}
