package com.evnica.interop.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class: DataStorage
 * Version: 0.1
 * Created on 11.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DataStorage
{
    List<Station> stations = new ArrayList<>(  );

    public void setStations( List<Station> stations )
    {
        this.stations = stations;
    }

    public Station findStation ( String stationName)
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


}
