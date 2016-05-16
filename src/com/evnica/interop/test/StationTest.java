package com.evnica.interop.test;

import com.evnica.interop.main.*;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class: StationTest
 * Version: 0.1
 * Created on 22.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Tests Station class and its methods (getting measurements within interval
 */
public class StationTest
{
    @Test
    public void test() throws Exception
    {
        List<File> allResources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/resources" );
        List<String> firstStationData = DataReader.readData( allResources.get( 0 ) );
        Station station = DataProcessor.convertTextIntoStation( firstStationData );

        // from 2015-01-30 00:00 to 2015-01-31 10:30

        List<DayMeasurement> measurementsWithin = station.getMeasurementsWithinInterval
                ( TestData.dates[0], TestData.timestamps[0], TestData.dates[2], TestData.timestamps[1] );

        assertEquals( measurementsWithin.size(), 2 );

        assertEquals( measurementsWithin.get( 0 ).getHourlyMeasurementValues().size() +
                      measurementsWithin.get( 1 ).getHourlyMeasurementValues().size(), 34 );

        // from 2015-01-29 00:00 to 2015-01-30 10:30

        measurementsWithin = station.getMeasurementsWithinInterval
                ( TestData.dates[1], TestData.timestamps[0], TestData.dates[0], TestData.timestamps[1] );
        assertEquals( measurementsWithin.size(), 1 );
        assertEquals( measurementsWithin.get( 0 ).getHourlyMeasurementValues().size(), 10 );

        measurementsWithin = station.getMeasurementsWithinInterval
                ( TestData.dates[0], TestData.timestamps[0], TestData.dates[1], TestData.timestamps[1] );
        assertEquals( measurementsWithin.size(), 0 );

        // from 2015-03-02 00:00 to 2015-03-03 20:00
        measurementsWithin = station.getMeasurementsWithinInterval
                ( TestData.dates[4], TestData.timestamps[0], TestData.dates[5], TestData.timestamps[3] );
        assertEquals( measurementsWithin.size(), 1 );

        assertEquals( measurementsWithin.get( 0 ).getHourlyMeasurementValues().size(), 14 );
    }
}