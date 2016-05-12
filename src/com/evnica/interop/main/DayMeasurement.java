package com.evnica.interop.main;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: DayMeasurement
 * Version: 0.1
 * Created on 11.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */

class DayMeasurement implements Comparable<DayMeasurement>
{
    LocalDate date;
    List<Measurement> hourlyMeasurementValues = new ArrayList<>();
    int indexOfLastValidMeasurement = -1; // shows if there are null-values at the end of period
    int indexOfFirstValidMeasurement = 0; // shows if there are null-values at the end of period

    DayMeasurement( LocalDate date, List<Measurement> hourlyMeasurementValues )
    {
        this.date = date;
        this.hourlyMeasurementValues = hourlyMeasurementValues;
    }

    @Override
    public int compareTo( DayMeasurement anotherDayMeasurement )
    {
        int result;

        if ( this.date.isBefore( anotherDayMeasurement.date ) ) { result = -1; }
        else if ( this.date.isAfter( anotherDayMeasurement.date ) ) { result = 1; }
        else { result = 0; }

        return result;
    }

    @Override
    public String toString()
    {
        String hourlyValues = "";
        for ( Measurement m: hourlyMeasurementValues )
        {
            hourlyValues = hourlyValues + "\n" + m;
        }

        return date.toString( Formatter.DATE_FORMATTER) + hourlyValues;
    }
}
