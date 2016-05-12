package com.evnica.interop.main;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class: Formatter
 * Version: 0.1
 * Created on 12.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Formatter
{
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern( "dd.MM.yyyy" );
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern( "HH:mm" );
    static final NumberFormat NUMBER_FORMAT = new DecimalFormat( "#0.00" );

}
