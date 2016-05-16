package com.evnica.interop.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.format.DateTimeFormat;

import java.sql.*;

/**
 * Class: DatabaseCreator
 * Version: 0.1
 * Created on 16.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DatabaseCreator
{
    private static Connection connection;
    private static final String DRIVER_CLASS = "org.h2.Driver";
    private static final Logger LOGGER = LogManager.getLogger( DatabaseCreator.class );
    private static boolean tableOfMeasurementsExists = false;
    private static boolean tableFull = false;

    public static boolean connect()
    {
        boolean connected;
        try
        {
            Class.forName( DRIVER_CLASS );
            connection = DriverManager.getConnection( "jdbc:h2:~/test", "sa", "" );
            connected = true;
        }
        catch ( ClassNotFoundException e )
        {
            LOGGER.error( "Can't load driver for the embedded DB, the report will contain a table only", e );
            connected = false;
        }
        catch ( SQLException ex )
        {
            LOGGER.error( "Can't connect to embedded DB, the report will contain a table only", ex );
            connected = false;
        }
        return connected;
    }

    public static boolean insert(Station station)
    {
        Statement statement;
        boolean success = false;
        boolean connected = true;
        String timestamp;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            try
            {
                if ( !tableOfMeasurementsExists )
                {
                    createTable();
                }
                if ( tableOfMeasurementsExists )
                {
                    statement = connection.createStatement();
                    for (DayMeasurement day: station.measurements)
                    {
                        for (Measurement pair: day.hourlyMeasurementValues)
                        {
                            //The format is yyyy-MM-dd hh:mm:ss[.nnnnnnnnn] // http://www.h2database.com/html/datatypes.html#timestamp_type
                            timestamp = day.date.toString( DateTimeFormat.forPattern("yyyy-MM-dd")) + " "
                                                         + pair.timestamp.toString( DateTimeFormat.forPattern( "hh:mm:ss" ));
                            statement.execute( "INSERT INTO measurements VALUES" +
                                "('"+ timestamp + "', " + pair.value + ")" );
                        }
                    }
                    success = true;
                    tableFull = true;
                }
                else
                {
                    success = false;
                }
            }
            catch ( SQLException e )
            {
                LOGGER.error( "Failed to insert data ", e );
                success = false;
            }
        }
        return success;
    }

    public static boolean deleteFromTable() throws Exception
    {
        boolean success = false;
        boolean connected = true;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( tableOfMeasurementsExists )
            {
                try
                {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM measurements;");
                    statement.executeUpdate();
                    success = true;
                    tableFull = false;
                }
                catch ( SQLException e )
                {
                    LOGGER.error( "Can't delete from measurements table", e );
                    success = false;
                }
            }
        }
        return success;
    }

    public static boolean createTable()
    {
        boolean success = false;
        boolean connected = true;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( !tableOfMeasurementsExists )
            {
                try
                {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate( "CREATE TABLE measurements (" +
                            "timestamp TIMESTAMP," +
                            "value DOUBLE" +
                            ")" );
                    tableOfMeasurementsExists = true;
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.error( "Can't create measurements table", e );
                    success = false;
                }
            }
        }
        return success;
    }

    public static boolean dropTable()
    {
        boolean connected = true;
        boolean success = false;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( tableOfMeasurementsExists )
            {
                try
                {
                    Statement statement = connection.createStatement();
                    statement.execute( "DROP TABLE measurements;" );
                    tableOfMeasurementsExists = false;
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.error( "Can't drop measurements table", e );
                    success = false;
                }
            }
        }
        return success;
    }

    public static Connection getConnection()
    {
        return connection;
    }

    public static boolean tableOfMeasurementsExists()
    {
        return tableOfMeasurementsExists;
    }

    public static boolean tableIsFull()
    {
        return tableFull;
    }
}
