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


public class Flight
{
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBMauricioLitvakMML5604";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    
    private Connection connection; //manages the connection to the database
    private PreparedStatement selectAllFlights;
    private PreparedStatement selectAllSeats;
    private PreparedStatement insertFlight;
    private PreparedStatement dropFlight;
    
    private boolean flightAddStatus;
    private boolean flightDropStatus;
    
    //constructor
    public Flight()
    {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            //create query that selects the NAMES in the FLIGHT Table
            selectAllFlights = connection.prepareStatement("SELECT NAME FROM FLIGHT");
            //create query that selects all seats from FLIGHT TABLES
            selectAllSeats = connection.prepareStatement("SELECT * FROM FLIGHT WHERE  NAME = ?");
            //create insert that adds a new entry into the database
            insertFlight = connection.prepareStatement("INSERT INTO FLIGHT (NAME, SEATS) VALUES (?, ?)");
            //remove flight from database
            dropFlight = connection.prepareStatement("DELETE FROM FLIGHT WHERE NAME = ?");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public List< String > getAllFlights()
    {
        List< String > results = null;
        ResultSet resultSet = null;
        
        try
        {
            //executeQuery returns ResultSet containing mathcing entries
            resultSet = selectAllFlights.executeQuery();
            results = new ArrayList< String >();
            
            while (resultSet.next())
            {
                results.add(new String(resultSet.getString("NAME")));
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
    
    public int getAllSeats(String FLIGHT)
    {
        List< String >results = null;
        ResultSet resultSet = null;
        int result = 0;
        
        try
        {
            selectAllSeats.setString(1, FLIGHT);
            resultSet = selectAllSeats.executeQuery();
            results = new ArrayList< String >();
            
            while (resultSet.next())
            {
                result = resultSet.getInt("SEATS");
            }
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            close();
        }
        
        return result;
    }
    
    public int addFlight(String FLIGHT, int SEATS)
    {
        int result = 0;
        
        //set parameters, then execute insertFlight
        try
        {
            insertFlight.setString(1, FLIGHT);
            insertFlight.setInt(2, SEATS);
            //insert the new entry; returns # of rows updated
            result = insertFlight.executeUpdate();
            
            flightAddStatus = true;//used to check status of addition of the flight            
            
        }
        catch(SQLException sqlException)
        {
            
            sqlException.printStackTrace();
            flightAddStatus = false;
        }
        System.out.print(result);
        return result;
        
    }
    
    public int dropFlight(String FLIGHT)
    {
        int result = 0;
        
        //set parameters, then execute dropFlight
        try
        {
            dropFlight.setString(1, FLIGHT);
            //remove the new entry;  returns # of rows deleted
            result = dropFlight.executeUpdate();
            
            flightDropStatus = true;//used to check status of dropping a flight
            
        }
        catch (SQLException sqlException)
        {
            flightDropStatus = false;
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
    
    public boolean getFlightAddStatus()
    {
        return flightAddStatus;
    }
    
    public boolean getFlightDropStatus()
    {
        return flightDropStatus;
    }

}//end class Flight
