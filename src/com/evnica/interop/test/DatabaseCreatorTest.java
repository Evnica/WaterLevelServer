package com.evnica.interop.test;

import com.evnica.interop.main.DataStorage;
import com.evnica.interop.main.DatabaseCreator;
import com.evnica.interop.main.Station;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;

/**
 * Class: DatabaseCreatorTest
 * Version: 0.1
 * Created on 16.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DatabaseCreatorTest
{
    @Test
    public void connect() throws Exception
    {
        assertTrue( DatabaseCreator.connect() );
    }

    @Test
    public void test() throws Exception
    {
        DataStorage.fillTheStorage();
        Station s = DataStorage.findStation( "Riedenburg" );
        assertNotNull( s );
        System.out.println("Connected to DB: " + DatabaseCreator.connect());

        if (DatabaseCreator.tableOfMeasurementsExists())
        {
            System.out.println("Deleted from table: " + DatabaseCreator.deleteFromTable());
        }
        else
        {
            System.out.println("Table created: " + DatabaseCreator.createTable());
        }

        assertTrue( DatabaseCreator.insert( s ) );

        PreparedStatement statement = DatabaseCreator.getConnection().prepareStatement( "SELECT * FROM measurements;" );
        ResultSet resultSet = statement.executeQuery();
        assertNotNull( resultSet );
        /*assertTrue( DatabaseCreator.deleteFromTable() );
        assertTrue( DatabaseCreator.dropTable() );*/
        assertTrue( DatabaseCreator.closeConnection() );
    }


}