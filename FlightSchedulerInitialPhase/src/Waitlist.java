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
import java.util.Calendar;
import java.sql.Date;


public class Waitlist
{
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBMauricioLitvakMML5604";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    
    private Connection connection; //manages the connection to the database
    private PreparedStatement insertWaitList;
    private PreparedStatement statusByDay;
    
    //constructor
    public Waitlist()
    {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            //create insert that adds a new entry into the bookings database
            insertWaitList = connection.prepareStatement("INSERT INTO WAITLIST"
                    + "(CUSTOMER, FLIGHT, DAY, TIMESTAMP)"
                    + "VALUES (?,?,?,?)");
            //create query that selects customers booked on a specific day and flight
            statusByDay = connection.prepareStatement("SELECT * FROM WAITLIST WHERE DAY = ?");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public void addWaitlist(String CUSTOMER, String FLIGHT, Date DAY)
    {
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        int count = 0;
        
        
        ResultSet resultSet = null;
        
        try
        {
            //Als code
            //GET seatsBooked here
            insertWaitList.setString(1, CUSTOMER);
            insertWaitList.setString(2, FLIGHT);
            insertWaitList.setDate(3, DAY);
            insertWaitList.setTimestamp(4, currentTimestamp);
            count = insertWaitList.executeUpdate(); 
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
            close();
        }
        
    }
    
public List< String > getStatusByDay(/*String FLIGHT, */Date DAY)
    {
        List< String > results = null;
        ResultSet resultSet = null;
        
        try
        {
            //statusByDay.setString(1, FLIGHT);
            //statusByDay.setDate(2, DAY);
            statusByDay.setDate(1, DAY);
            
            //executeQuery returns ResultSet containing matching entries
            resultSet = statusByDay.executeQuery();
            
            results = new ArrayList< String >();
            
            while (resultSet.next())
            {
                results.add(new String(resultSet.getString("CUSTOMER")));
                results.add(new String(resultSet.getString("FLIGHT")));
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
}