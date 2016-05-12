package com.evnica.interop.test;

import com.evnica.interop.main.DataProcessor;
import com.evnica.interop.main.DataReader;
import com.evnica.interop.main.Station;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: DataProcessorTest
 * Version: 0.1
 * Created on 11.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DataProcessorTest
{
    @Test
    public void convertDay() throws Exception
    {

        List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/WEB-INF/resources" );
        List<List<String>> data = new ArrayList<>( resources.size() );
        for (File source: resources)
        {
            data.add( DataReader.readData( source ) );
        }

        Station s = DataProcessor.convertTextIntoStation( data.get( 0 ) );
        System.out.println(s);



    }

}