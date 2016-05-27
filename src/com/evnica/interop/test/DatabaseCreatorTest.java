package com.evnica.interop.test;

import com.evnica.interop.main.DataStorage;
import com.evnica.interop.main.DatabaseCreator;
import com.evnica.interop.main.Station;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import static org.junit.Assert.*;

/**
 * Class: DatabaseCreatorTest
 * Version: 0.1
 * Created on 16.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Tests connection to embedded DB and basic operations on in (connect, create a table, insert values,
 *              delete values, drop table, disconnect)
 */
public class DatabaseCreatorTest
{
    @Test
    public void connect() throws Exception
    {
        assertTrue( DatabaseCreator.connect() );
        DatabaseCreator.closeConnection();
    }

    @Test
    public void test() throws Exception
    {
        DataStorage.fillTheStorage("../WaterLevelServer/app/resources");
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
        System.out.println("Values inserted into the table");

        PreparedStatement statement = DatabaseCreator.getConnection().prepareStatement( "SELECT * FROM measurements;" );
        ResultSet resultSet = statement.executeQuery();
        assertNotNull( resultSet );
        int numOfColumns = resultSet.getMetaData().getColumnCount();
        while ( resultSet.next() )
        {
            for (int i = 1; i <= numOfColumns; i++) {
                if (i > 1) System.out.print(" | ");
                System.out.print(resultSet.getString(i));
            }
            System.out.println("");
        }
        System.out.println("Deleting data from the table...");
        assertTrue( DatabaseCreator.deleteFromTable() );
        System.out.println("Done. Dropping the table...");
        assertTrue( DatabaseCreator.dropTable() );
        System.out.println("Done");
        assertTrue( DatabaseCreator.closeConnection() );
        System.out.println("Connection closed");
    }


}