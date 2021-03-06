package com.evnica.interop.test;

import com.evnica.interop.main.*;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: DataProcessorTest
 * Version: 0.1
 * Created on 22.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: A test for the DataProcessor class and its convertTextIntoStation() method
 */
public class DataProcessorTest
{
    @Test
    public void convertTextIntoStation() throws Exception
    {
        List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/resources" );
        List<List<String>> data = new ArrayList<>( resources.size() );

        resources.forEach( source -> data.add( DataReader.readData( source ) ) );

        List<Station> stations = new ArrayList<>(  );

        data.forEach(st -> stations.add( DataProcessor.convertTextIntoStation( st ) ) );

        assert (stations.size() == 3);
        LocalTime time = Formatter.getTimeFormatter().parseLocalTime( "01:00" );
        assert (stations.get(0).measurements.get( 0 ).getHourlyMeasurementValues().get( 0 ).getTimestamp().equals( time ));
        assert (stations.get(0).measurements.get( 0 ).getHourlyMeasurementValues().get( 1 ).getValue().equals( 402.25 ));
        assert (stations.get(0).measurements.get( stations.get(0).measurements.size() - 1 ).getHourlyMeasurementValues().get( 13 ).getValue() != null);

    }


}