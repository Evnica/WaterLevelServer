package com.evnica.interop.test;

import com.evnica.interop.main.*;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Class: JasperAssistantTest
 * Version: 0.1
 * Created on 16.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class JasperAssistantTest
{
    @Test
    public void getTableModel() throws Exception
    {
        DataStorage.fillTheStorage();
        Station s = DataStorage.findStation( "Riedenburg" );
        assertNotNull( s );

        List<DayMeasurement> chosenMeasurements = s.getMeasurementsWithinInterval
                ( TestData.dates[0], TestData.timestamps[0], TestData.dates[5], TestData.timestamps[1] );

        JasperAssistant.init( chosenMeasurements );

        int numberOfDays = chosenMeasurements.size();
        int numberOfEntriesInTheLastDay = chosenMeasurements.get( numberOfDays - 1 ).getHourlyMeasurementValues().size();

        Map<String, Object> parameters = new HashMap<>(  );
        parameters.put( "ReportTitle", s.getName() );
        parameters.put( "DateFrom", TestData.dates[0].toString( Formatter.getDateFormatter()) + " " +
                TestData.timestamps[0].toString( Formatter.getTimeFormatter())
        );
        parameters.put( "DateTo", TestData.dates[5].toString( Formatter.getDateFormatter()) + " " +
                TestData.timestamps[1].toString( Formatter.getTimeFormatter())
        );
        parameters.put( "AvailableFrom",
                chosenMeasurements.get( 0 ).getDate().toString( Formatter.getDateFormatter()) + " " +
                        chosenMeasurements.get( 0 ).getHourlyMeasurementValues().get( 0 ).getTimestamp().toString( Formatter.getTimeFormatter() )
        );
        parameters.put( "AvailableTo",
                chosenMeasurements.get( numberOfDays - 1).getDate().toString( Formatter.getDateFormatter()) + " " +
                        chosenMeasurements.get( numberOfDays - 1).getHourlyMeasurementValues()
                                .get( numberOfEntriesInTheLastDay - 1 )
                                .getTimestamp().toString(Formatter.getTimeFormatter())
        );

        String sourceFileName =
                "../WaterLevelServer/app/WEB-INF/lib/Test.jasper";

        JasperPrint jasperPrint = JasperFillManager.fillReport( sourceFileName, parameters, new JRTableModelDataSource( JasperAssistant.getTableModel() ));
        JasperExportManager.exportReportToPdfFile( jasperPrint, "report.pdf" );


    }

}