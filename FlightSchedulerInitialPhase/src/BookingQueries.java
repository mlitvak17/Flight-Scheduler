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


public class BookingQueries
{
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBMauricioLitvakMML5604";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    
    private Connection connection; //manages the connection to the database
    private PreparedStatement insertBooking;
    private PreparedStatement statusByFlightDay;
    //private PreparedStatement cancelBooking;//PART OF FINAL PROJECT NOT USED IN PA6
    private PreparedStatement selectFlightSeats;
    
    private boolean bookingStatus;
    private boolean waitlistStatus;
    
    //constructor
    public BookingQueries()
    {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            //create insert that adds a new entry into the bookings database
            insertBooking = connection.prepareStatement("INSERT INTO BOOKINGS"
                    + "(CUSTOMER, FLIGHT, DAY, TIMESTAMP)"
                    + "VALUES (?,?,?,?)");
            //create query that selects all entries in the Customers database
            selectFlightSeats = connection.prepareStatement("select count(flight) from bookings where flight = ? and day = ?");
            //create query that selects customers booked on a specific day and flight
            statusByFlightDay = connection.prepareStatement("SELECT * FROM BOOKINGS WHERE FLIGHT = ? AND DAY = ?");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public void addBooking(String CUSTOMER, String FLIGHT, Date DAY)
    {
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        int count = 0;
        int seatsBooked = 0;
        int maxSeats = 0;
        
        
        ResultSet resultSet = null;
        
        //Grabbing total seats available on flight
        Flight flight = new Flight();
        maxSeats = flight.getAllSeats(FLIGHT);
        
        try
        {
            //Als code
            //GET seatsBooked here
            selectFlightSeats = connection.prepareStatement("SELECT COUNT(FLIGHT) FROM BOOKINGS WHERE FLIGHT = ? AND DAY = ?"); 
            selectFlightSeats.setString(1, FLIGHT); selectFlightSeats.setDate(2, DAY); 
            resultSet = selectFlightSeats.executeQuery(); 
            resultSet.next(); 
            seatsBooked = resultSet.getInt(1);  
        }
        catch(SQLException sqlException)
        {
            System.out.print("it occurs here");
            sqlException.printStackTrace();
            close();
        }
        
        if (seatsBooked == maxSeats)
        {
        
                Waitlist waitlist = new Waitlist();
                waitlist.addWaitlist(CUSTOMER, FLIGHT, DAY);
                bookingStatus = false;
                waitlistStatus = true;
        }
        else
        {
            try
            {
                insertBooking.setString(1, CUSTOMER);
                insertBooking.setString(2,FLIGHT);
                insertBooking.setDate(3, DAY);
                insertBooking.setTimestamp(4, currentTimestamp);
                bookingStatus = true;
                waitlistStatus = false;
                
                //insert the new entry; returns # of rows updated 
                count = insertBooking.executeUpdate();
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
                close();
            }
        }
        
        
        
    }
    
    public void getFlightSeats(String FLIGHT, Date DAY)
    {
        ResultSet resultSet = null;
        int seatsBooked = 0;
        
        try
        {
        selectFlightSeats.setString(1, FLIGHT);
        selectFlightSeats.setDate(2, (java.sql.Date) DAY); 
        resultSet = selectFlightSeats.executeQuery(); 
        resultSet.next(); 
        seatsBooked = resultSet.getInt(1);
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            close();
        }
    }
    
    public List< String > getStatusByFlightDay(String FLIGHT, Date DAY)
    {
        List< String > results = null;
        ResultSet resultSet = null;
        
        try
        {
            statusByFlightDay.setString(1, FLIGHT);
            statusByFlightDay.setDate(2, DAY);
            
            //executeQuery returns ResultSet containing matching entries
            resultSet = statusByFlightDay.executeQuery();
            
            results = new ArrayList< String >();
            
            while (resultSet.next())
            {
                results.add(new String(resultSet.getString("CUSTOMER")));
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
    
    public boolean getBookingStatus()
    {
        return this.bookingStatus = bookingStatus;
    }
    
    public boolean getWaitlistStatus()
    {
        return this.waitlistStatus = waitlistStatus;
    }
}