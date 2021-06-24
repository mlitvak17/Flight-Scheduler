/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MauriLitvak
 */
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;


public class Customer
{
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBMauricioLitvakMML5604";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    
    private Connection connection; //manages the connection to the database
    private PreparedStatement insertCustomer;
    private PreparedStatement selectAllCustomers;
    
    private boolean customerAddStatus;
    
    //constructor
    public Customer()
    {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            //create insert that adds a new entry in the database
            insertCustomer = connection.prepareStatement("INSERT INTO CUSTOMER (CUSTOMERNAME) VALUES (?)");
            
            //create query that selects all entries in the Customers database
            selectAllCustomers = connection.prepareStatement("SELECT * FROM CUSTOMER");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    //select all the customers in the Customer Databse
    public List< String > getAllCustomers()
    {
        List< String > results = null;
        ResultSet resultSet = null;
        
        try
        {
            //executeQuery returns ResultSet containing matching entries
            resultSet = selectAllCustomers.executeQuery();
            results = new ArrayList< String >();
            
            while (resultSet.next())
            {
                results.add(new String(resultSet.getString("CUSTOMERNAME")));
            }
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        finally
        {
            try
            {
                resultSet.close();
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
                close();
            }
        }
        return results;
    }
    
    public int addCustomer(String name)
    {
        int result = 0;
        
        //set parameters, then execute insertCustomer
        try
        {
            insertCustomer.setString(1, name);
            
            //insert the new entry; returns # of rows updated
            result = insertCustomer.executeUpdate();
            
            customerAddStatus = true;//used to check status of addition of customer
            
        }
        catch (SQLException sqlException)
        {
            
            sqlException.printStackTrace();
            //close();
            customerAddStatus = false;
        }
        System.out.print(result);
        return result;
    }

    //close database connection
    public void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public boolean getCustomerAddStatus()
    {
        return customerAddStatus;
    }
    
}//end class Customer