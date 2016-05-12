package com.evnica.interop.main;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class: Formatter
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Formatter
{
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern( "dd.MM.yyyy" );
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern( "HH:mm" );
    static final DateTimeFormatter WEB_FORMATTER = DateTimeFormat.forPattern( "yyyy-MM-dd'T'HH:mm:ss'Z'" );


    public static DateTimeFormatter getWebDateFormatter()
    {
        return WEB_FORMATTER;
    }

    static final NumberFormat NUMBER_FORMAT = new DecimalFormat( "#0.00" );

    public static DateTimeFormatter getDateFormatter()
    {
        return DATE_FORMATTER;
    }

    public static DateTimeFormatter getTimeFormatter()
    {
        return TIME_FORMATTER;
    }

    public static NumberFormat getNumberFormat()
    {
        return NUMBER_FORMAT;
    }
}
