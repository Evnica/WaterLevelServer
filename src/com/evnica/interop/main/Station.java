package com.evnica.interop.main;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: Station
 * Version: 0.1
 * Created on 11.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class Station
{

    String name, bodyOfWater, place;
    List<DayMeasurement> measurements = new ArrayList<>(  );

    public Station( String name, String bodyOfWater, String place )
    {
        this.name = name;
        this.bodyOfWater = bodyOfWater;
        this.place = place;
    }

   public String toString()
   {
       String pairs = "";
       for (DayMeasurement measurement: measurements)
       {
           pairs = pairs + "\n\n" + measurement;
       }
       return "Messstation: " + name + " [" + bodyOfWater + ", " + place + "]:" + pairs;
   }
}
