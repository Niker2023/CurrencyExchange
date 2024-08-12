package org.petproject;

import org.petproject.servlet.FirstServlet;

import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String pathToDB = "/home/nikita/IdeaProjects/CurrencyExchange/currency.db";
        try
                (
                        // create a database connection
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathToDB);
                        Statement statement = connection.createStatement();
                )
        {
            ResultSet rs = statement.executeQuery("select * from currencies");
            while(rs.next())
            {
                // read the result set
                System.out.println("Code = " + rs.getString("Code"));
                System.out.println("FullName = " + rs.getString("FullName"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        }

    }
}
