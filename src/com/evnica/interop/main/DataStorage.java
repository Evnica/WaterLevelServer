package com.evnica.interop.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class: DataStorage
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DataStorage
{
    static List<Station> stations = new ArrayList<>();

    public static Station findStation ( String stationName)
    {
        Station result;
        String toFind = stationName.toLowerCase();
        List<Station> chosenStations = stations.stream()
                .filter( st -> st.name.toLowerCase().contains( toFind ) )
                .collect( Collectors.toList());
        if (chosenStations.size() > 0) { result = chosenStations.get( 0 ); }
        else { result = null; }
        return result;
    }

    public static String getNamesOfStationsInStorage()
    {
        String names = "None";
        if (  stations.size() > 1 )
        {
            names = stations.get( 0 ).name;
            for (int i = 1; i < stations.size(); i++)
            {
                names = names + ", " + stations.get( i ).name;
            }
        }
        else if (stations.size() == 1)
        {
            names = stations.get( 0 ).name;
        }
        return names;
    }

    public static void fillTheStorage() throws IOException
    {
        List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/WEB-INF/resources" );
        List<List<String>> data = new ArrayList<>( resources.size() );
        resources.forEach( source -> data.add( DataReader.readData( source ) ) );
        stations = new ArrayList<>(  );
        data.forEach(st -> stations.add( DataProcessor.convertTextIntoStation( st ) ) );
    }


}
