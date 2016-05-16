package com.evnica.interop.main;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: ReportServlet
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */

@WebServlet (name = "ReportServlet",
             loadOnStartup = 1,
             urlPatterns = "/ReportServlet")
public class ReportServlet extends HttpServlet
{
    private static final long serialVersionUID = 2938837482734983849L;
    private String stationName, format;
    private LocalDateTime startDateTime, endDateTime;
    private LocalDate startDate, endDate;
    private LocalTime startTime, endTime;
    private Station requestedStation;
    private List<DayMeasurement> measurementsWithinInterval;
    private static final String FORMAT = "BS2016SS";
    private static final Logger LOGGER = LogManager.getLogger( ReportServlet.class );
    private PrintWriter printWriter = null;

    @Override
    public void init() throws ServletException
    {
        try
        {
            List<File> resources = DataReader.listAllFilesInResources( "/resources" );
            List<List<String>> data = new ArrayList<>( resources.size() );
            resources.forEach( source -> data.add( DataReader.readData( source ) ) );
            data.forEach(st -> DataStorage.stations.add( DataProcessor.convertTextIntoStation( st ) ) );
        }
        catch ( IOException e )
        {
            throw new ServletException( "Measurement data can't be read. Further processing of requests is not possible. ", e );
        }

    }

    @Override
    protected void service( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        if (request.getMethod().toLowerCase().equals( "get" ))
        {
            doGet( request, response );
        }
        if (request.getMethod().toLowerCase().equals( "post" ))
        {
            doPost( request, response );
        }
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        format = request.getParameter( "format" );
        stationName = request.getParameter( "name" );

        // if the station name is not provided, further processing is impossible
        if (stationName == null)
        {
            LOGGER.error( "Station name not provided" );
            response.sendError( HttpServletResponse.SC_BAD_REQUEST,
                    "The station name must be passed as a value of the 'name' parameter for your " +
                    "request to be processed. " );
        }

        try
        {
            printWriter = response.getWriter();
        }
        catch ( IOException e )
        {
            LOGGER.error( "No PrintWriter available to send response: ", e );
        }
        // if name is provided, look in the storage for the corresponding station
        requestedStation = DataStorage.findStation( stationName );

        // if it's not found in storage
        if (requestedStation == null)
        {
            if ( printWriter != null )
            {
                // inform the user of the available stations
                String message =    "Requested station not found in sources. \n " +
                                    "Measurement data is available for the following stations: "
                                    + DataStorage.getNamesOfStationsInStorage();
                printWriter.append( message );
                printWriter.flush();
            }
        }

          if (!format.toUpperCase().equals( FORMAT ))
        {
            LOGGER.error( "Unavailable format requested" );
            response.sendError( HttpServletResponse.SC_BAD_REQUEST,
                        "The data exchange is currently available only in BS2016SS format" );
        }

        try
        {
            startDateTime = Formatter.WEB_FORMATTER.parseLocalDateTime( request.getParameter( "start" ) );
            endDateTime = Formatter.WEB_FORMATTER.parseLocalDateTime( request.getParameter( "end" ) );
        }
        catch ( Exception e )
        {
            LOGGER.error( "Wrong date format ", e );
            response.sendError( HttpServletResponse.SC_BAD_REQUEST,
                        "The start date and the end date must be provided in format yyyy-MM-dd'T'HH:mm:ss'Z' " );
        }

        startDate = startDateTime.toLocalDate();
        startTime = startDateTime.toLocalTime();
        endDate = endDateTime.toLocalDate();
        endTime = endDateTime.toLocalTime();

        measurementsWithinInterval = requestedStation.getMeasurementsWithinInterval( startDate, startTime, endDate, endTime );

        displayReport( response, requestedStation.name, measurementsWithinInterval );



    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        doGet( request, response );
        /*TODO: provide a different implementation*/
    }

    private void displayReport(HttpServletResponse response, String stationName, List<DayMeasurement> measurements)
    {
        //TODO: implement using JasperReports
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}
