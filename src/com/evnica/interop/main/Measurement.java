package com.evnica.interop.main;

import org.joda.time.LocalTime;

/**
 * Class: Measurement
 * Version: 0.1
 * Created on 11.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Measurement implements Comparable<Measurement>
{
    Double value;
    LocalTime timestamp;
    private static final String UNIT = "cm";

    public Measurement( LocalTime timestamp, Double value )
    {
        this.value = value;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo( Measurement anotherMeasurement )
    {
        int result;

        if (this.timestamp.equals(anotherMeasurement.timestamp))
        {
            result = 0;
        }
        else if (this.timestamp.isBefore( anotherMeasurement.timestamp ))
        {
            result = -1;
        }
        else  // this is after anotherMeasurement
        {
            result = 1;
        }
        return result;
    }

    @Override
    public String toString()
    {
        return timestamp.toString( Formatter.TIME_FORMATTER ) + "#" + value;
    }
}
