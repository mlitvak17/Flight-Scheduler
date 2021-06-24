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
import java.sql.Date;


public class Day
{
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBMauricioLitvakMML5604";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    
    private Connection connection; //manages the connection to the database
    private PreparedStatement selectAllDays;
    private PreparedStatement insertDate;
    
    private boolean dayAddStatus;
    
    //constructor
    public Day()
    {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        
            //create query that selects all entries in the Day Table
            selectAllDays = connection.prepareStatement("SELECT * FROM DAY");
            
            //create query that inserts a date in the Day Table
            insertDate = connection.prepareStatement("INSERT INTO DAY (DATE) VALUES (?)");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    //select all the customers in the Customer Databse
    public List< String > getAllDays()
    {
        List< String > results = null;
        ResultSet resultSet = null;
        
        try
        {
            //executeQuery returns ResultSet containing matching entries
            resultSet = selectAllDays.executeQuery();
            results = new ArrayList< String >();
            
            while (resultSet.next())
            {
                results.add(new String(resultSet.getString("DATE")));
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
    
    public int addDay(Date DATE)
    {
        int result = 0;
        
        //set parameters, then execute insertDay
        try
        {
            insertDate.setDate(1, DATE);
            
            //insert the new entry; returns # of rows updated
            result = insertDate.executeUpdate();
            
            dayAddStatus = true;//used to check status of addition of Date
            
        }
        catch (SQLException sqlException)
        {
            
            sqlException.printStackTrace();
            //close();
            dayAddStatus = false;
        }
        System.out.print(result);
        return result;
    }
    
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
    
    public boolean getDayAddStatus()
    {
        return dayAddStatus;
    }
}