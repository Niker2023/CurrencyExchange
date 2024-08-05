package org.petproject;

import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try
                (
                        // create a database connection
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:currency.db");
                        Statement statement = connection.createStatement();
                )
        {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

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
