package com.evnica.interop.test;

import com.evnica.interop.main.Formatter;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Class: TestData
 * Version: 0.1
 * Created on 22.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
class TestData
{

    static LocalDate[] dates = {new LocalDate( "2015-01-30" ), new LocalDate( "2015-01-29" ), new LocalDate( "2015-01-31" ),
                         new LocalDate( "2015-02-03" ), new LocalDate( "2015-03-02" ), new LocalDate( "2015-03-03" )};
    static LocalTime[] timestamps =
                     {new LocalTime( 0, 0 ), new LocalTime( 10, 30 ), new LocalTime( 20, 0 ), new LocalTime( 23, 59 )};


}
