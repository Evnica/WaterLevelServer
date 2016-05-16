package com.evnica.interop.test;

import com.evnica.interop.main.DataReader;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class: DataReaderTest
 * Version: 0.1
 * Created on 22.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Tests local file reading
 */
public class DataReaderTest
{
    @Test
    public void readData() throws Exception
    {
        List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/resources" );
        List<List<String>> data = new ArrayList<>( resources.size() );
        for (File source: resources)
        {
            data.add( DataReader.readData( source ) );
        }
        assertEquals( data.size(), 3 );
        data.get( 2 ).forEach( System.out::println );
    }

    @Test
    public void listAllFilesInResources() throws Exception
    {
        List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/resources" );
        assertEquals( resources.size(), 3 );
    }

}