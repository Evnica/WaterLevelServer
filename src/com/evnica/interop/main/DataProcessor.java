package com.evnica.interop.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class: DataProcessor
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: DataProcessor converts the two-dimensional list of Strings into instances of Station class with required
 * calculations of mean hourly measurement values
 */
public class DataProcessor
{

    private static final Logger LOGGER = LogManager.getLogger( DataProcessor.class );
    private static final int START_OF_MEASUREMENT_VALUES = 12;


    public static Station convertTextIntoStation (List<String> fileContent)
    {
        Station station = new Station(fileContent.get( 1 ) + " (" +  fileContent.get( 3 ) + ")", fileContent.get( 2 ));
        List<DayMeasurement> measurements = new ArrayList<>(  );
        Double[] oneHourMeasurements = new Double[4];
        Double value;
        List <Measurement> oneDayMeasurements = new ArrayList<>(  );
        String[] onePairSplit;
        String stringDate = fileContent.get( 0 );
        LocalTime measurementTime;
        LocalDate date;
        boolean newDay = false;
        int i = START_OF_MEASUREMENT_VALUES;
        int hourlyValuesCounter = 0;

        while ( i < fileContent.size())
        {
               do
               {
                   onePairSplit = fileContent.get( i ).split( "#" );
                   try
                   {
                       value = Double.parseDouble( onePairSplit[1] );
                   }
                   catch ( NumberFormatException e )
                   {
                       LOGGER.error( "Invalid measurement value " + onePairSplit[1] + " in the line " + i + " for the " +
                               "station " + station.name + " on " + stringDate );
                       value = null;
                   }
                   oneHourMeasurements[hourlyValuesCounter] = value;
                   hourlyValuesCounter++;
                   i++;
               }
               while ( hourlyValuesCounter < 4 );

               hourlyValuesCounter = 0;

               try
               {
                   measurementTime = LocalTime.parse( onePairSplit[0], Formatter.TIME_FORMATTER );
               }
               catch ( Exception e )
               {
                   if (onePairSplit[0].equals( "24:00" ))
                   {
                        newDay = true;
                        measurementTime = LocalTime.parse( "00:00", Formatter.TIME_FORMATTER );
                   }
                   else
                   {
                       LOGGER.error( "Invalid time value " + onePairSplit[0] + " in the line " + i + " for the " +
                               "station " + station.name + " on " + stringDate );
                       measurementTime = null;
                   }
               }

               value = calcAverage(oneHourMeasurements);

               if ( !newDay )
               {
                   oneDayMeasurements.add( new Measurement(measurementTime, value) );
               }
               else
               {
                   try
                   {
                       date = LocalDate.parse( stringDate, Formatter.DATE_FORMATTER );
                   }
                   catch ( Exception e )
                   {
                       LOGGER.error( "Invalid date value " + stringDate + " for the station " + station.name);
                       date = null;
                   }
                   measurements.add( new DayMeasurement(date, oneDayMeasurements) );
                   oneDayMeasurements = new ArrayList<>(  );
                   oneDayMeasurements.add( new Measurement( measurementTime, value ) );
               }

               if (newDay)
               {
                   if ( i < fileContent.size() - 2 )
                   {
                       stringDate = fileContent.get( i + 1 );
                       i += 13;
                       newDay = false;
                   }
                   else
                   {
                       if ( value != null )
                       {
                           date = measurements.get( measurements.size() - 1 ).date.plusDays( 1 );
                           measurements.add( new DayMeasurement( date, oneDayMeasurements ) );
                       }
                   }
               }
        }
        replaceNullValuesWithMeanValues( measurements );
        station.measurements = measurements;
        return station;
    }


    private static Double calcAverage(Double[] values)
    {
        Double result = null;
        Double sumOfValidMeasurements = 0.0;
        int numberOfValidMeasurements = 0;

        for ( Double value: values)
        {
            if (value != null)
            {
                numberOfValidMeasurements++;
                sumOfValidMeasurements = sumOfValidMeasurements + value;
            }
        }
        if (numberOfValidMeasurements > 1)
        {
            result = sumOfValidMeasurements / numberOfValidMeasurements;
        }
        if (numberOfValidMeasurements == 1)
        {
            result = sumOfValidMeasurements;
        }

        return result;
    }


    private static void replaceNullValuesWithMeanValues(List<DayMeasurement> dayMeasurements)
    {
        Collections.sort(dayMeasurements);
        dayMeasurements.forEach( day -> Collections.sort( day.hourlyMeasurementValues ) );
        Double previousValidValue = null;
        Double next;
        Double average;
        List<Integer> indicesOfNullValues = new ArrayList<>(  );

        for (DayMeasurement day: dayMeasurements)
        {
            if ( day.hourlyMeasurementValues.size() > 2 )
            {
                int i =  day.hourlyMeasurementValues.size() - 1; // start with the end of the day

                while (day.hourlyMeasurementValues.get( i ).value == null) //skip all null-values at the end of the day
                {
                    i--;
                }

                day.indexOfLastValidMeasurement = i;

                while ( i > 0 )
                {
                    if (day.hourlyMeasurementValues.get( i ).value != null)
                    {
                        previousValidValue = day.hourlyMeasurementValues.get( i ).value;
                        day.indexOfFirstValidMeasurement = i;
                    }
                    else //current value is null
                    {
                        next = day.hourlyMeasurementValues.get( i - 1 ).value;

                        if (next == null) //2 or more values in a row are null
                        {
                            indicesOfNullValues.add( i ); // save indices of all null values exclusive the last found
                        }
                        else // next != null
                        {
                            if ( previousValidValue != null) // next != null, lastValid != null
                            {
                                average = ( previousValidValue + next) / 2;
                                day.hourlyMeasurementValues.get( i ).value = average;

                                if (!indicesOfNullValues.isEmpty()) //2 or more values in a row between two valid values were null
                                {
                                    for (Integer index: indicesOfNullValues)
                                    {
                                        day.hourlyMeasurementValues.get( index ).value = average; // replace all interim null values with an average
                                    }
                                    indicesOfNullValues.clear();
                                }
                            }
                        }
                    }
                    i--;
                }
            }
        }
    }




}
