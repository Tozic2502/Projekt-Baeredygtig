package org.example.projektbaeredygtig;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DB
{
    private static Connection con;
    private static PreparedStatement ps;

    private static String port;
    private static String databaseName;
    private static String userName;
    private static String password;


    static{
        Properties prop = new Properties();

        try(InputStream input = DB.class.getClassLoader().getResourceAsStream("db.properties")){
            if(input == null){
                throw new RuntimeException("db.properties not found");
            }
            prop.load(input);

            port = prop.getProperty("port", "1433");
            databaseName = prop.getProperty("databaseName");
            userName = prop.getProperty("userName", "sa");
            password = prop.getProperty("password");

        } catch (IOException e)
        {
            System.err.println("Error loading properties file");
        }
    }
}
