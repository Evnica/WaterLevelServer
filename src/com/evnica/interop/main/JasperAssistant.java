package com.evnica.interop.main;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * Class: JasperAssistant
 * Version: 0.1
 * Created on 14.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Creates a data source for jasper report (table model format)
 */
public class JasperAssistant
{
    private static List<DayMeasurement> measurements;
    private static int numberOfRows = 0;
    private static Object[][] tableContent;
    private static TableModel tableModel;

    public static void init (List<DayMeasurement> chosenMeasurements)
    {
        measurements = chosenMeasurements;
        createTableModel();
    }

    public static TableModel getTableModel()
    {
        return tableModel;
    }

    private static void createTableModel()
    {
        createATable();
        String columns[] = {"date", "timestamp", "value"};
        tableModel = new DefaultTableModel( tableContent, columns );
    }

    private static void createATable( )
    {
        countSizeOfData();
        if ( numberOfRows > 0 )
        {
            tableContent = new Object[numberOfRows][3];
            String date;
            int i = 0;

            for (DayMeasurement day: measurements )
            {
                date = day.date.toString( Formatter.DATE_FORMATTER );
                for (Measurement pair: day.hourlyMeasurementValues)
                {
                    tableContent[i][0] = date;
                    tableContent[i][1] = pair.timestamp.toString( Formatter.TIME_FORMATTER );
                    tableContent[i][2] = pair.value;
                    i++;
                }
            }
        }
    }

    private static void countSizeOfData()
    {
        numberOfRows = 0;
        if ( measurements != null && measurements.size() > 0 )
        {
            for (DayMeasurement day: measurements )
            {
                numberOfRows = numberOfRows + day.hourlyMeasurementValues.size();
            }
        }
    }

    public static Object[][] getTableContent()
    {
        return tableContent;
    }
}