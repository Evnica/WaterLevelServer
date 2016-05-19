package com.evnica.interop.main;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class: ReportServlet
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Processes user request and displays a report (NOT YET IMPLEMENTED)
 */

@WebServlet (name = "ReportServlet",
             loadOnStartup = 1,
             urlPatterns = "/ReportServlet")
public class ReportServlet extends HttpServlet

{
    static {
        System.setProperty("java.awt.headless", "true");
        System.out.println(java.awt.GraphicsEnvironment.isHeadless());
    }

    private static final long serialVersionUID = 2938837482734983849L;
    private LocalDateTime startDateTime, endDateTime;
    private LocalDate startDate, endDate;
    private LocalTime startTime, endTime;
    private Station requestedStation;
    private List<DayMeasurement> measurementsWithinInterval;
    private static final String FORMAT = "BS2016SS";
    private static final Logger LOGGER = LogManager.getLogger( ReportServlet.class );
    private static final String RESOURCES = "/resources";
    private static final String JASPER_SOURCE = "/jasper/Test3.jasper";  //"/jasper/Test.jasper"; with SansSerif also doesn't work, though this font family is supported by JVM.

    @Override
    public void init() throws ServletException
    {
        /*String [] fonts = getFontNames();
        for (String f: fonts)
        {
            System.out.println(f);
        }
        System.out.println("FAMILIES");
        String [] fontsFamilies = getFontFamilies();
        for (String ff: fontsFamilies)
        {
            System.out.println(ff);
        }*/
    }

    /*public static Font [] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    }

    public static String [] getFontNames() {
        Font [] fonts = getFonts();
        String [] fontNames = new String [fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fontNames[i] = fonts[i].getFontName();
        }
        return fontNames;
    }

    public static String [] getFontFamilies() {
        Font [] fonts = getFonts();
        String [] fontFamilies = new String [fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fontFamilies[i] = fonts[i].getFamily();
        }
        return fontFamilies;
    }*/


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
        storeAvailableData( request );
        checkValidityOfParameters( request, response );
        convertDates();

        measurementsWithinInterval = requestedStation.getMeasurementsWithinInterval( startDate, startTime, endDate, endTime );

        createReport( request, response, requestedStation.name );


    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        doGet( request, response );
        /*TODO: provide a different implementation*/
    }

    private void createReport( HttpServletRequest request, HttpServletResponse response, String stationName) throws ServletException, IOException
    {
        String start = startDate.toString(Formatter.DATE_FORMATTER) + " " + startTime.toString( Formatter.TIME_FORMATTER ) ;
        String end = endDate.toString(Formatter.DATE_FORMATTER) + " " + endTime.toString( Formatter.TIME_FORMATTER ) ;
        JasperAssistant.init( measurementsWithinInterval );
        Map<String, Object> parameters = JasperAssistant.createParameters( stationName, start, end );

        File source = new File (request.getSession().getServletContext().getRealPath( JASPER_SOURCE ));

        try
        {
            byte[] byteStream = JasperRunManager.runReportToPdf
                    ( source.getAbsolutePath(), parameters, new JRTableModelDataSource( JasperAssistant.getTableModel()));
            OutputStream out = response.getOutputStream();
            response.setHeader( "Content-Disposition", "inline, filename='Pegel.pdf'" );
            response.setContentType( "application/pdf" );
            response.setContentLength( byteStream.length );
            out.write( byteStream, 0, byteStream.length );
        }
        catch ( JRException e )
        {
            LOGGER.error( "Report couldn't be created", e );
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't fill report with data: " + e.getMessage()  );
        }

    }

    private void storeAvailableData(HttpServletRequest request) throws ServletException
    {
        try
        {
            File folder = new File (request.getSession().getServletContext().getRealPath( RESOURCES ));
            List<File> resources = DataReader.listAllFilesInResourcesFromServlet( folder );
            List<List<String>> data = new ArrayList<>( );
            resources.forEach( source -> data.add( DataReader.readData( source ) ) );
            List<Station> stations = new ArrayList<>(  );
            for (List<String> oneStationData: data)
            {
                stations.add( DataProcessor.convertTextIntoStation( oneStationData ) );
            }
            DataStorage.stations = stations;
        }
        catch ( IOException e )
        {
            throw new ServletException( "Measurement data can't be read. Further processing of requests is not possible. ", e );
        }
    }

    private void checkValidityOfParameters(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        String format = request.getParameter( "format" );
        String stationName = request.getParameter( "name" );

        // if the station name is not provided, further processing is impossible
        if ( stationName == null)
        {
            LOGGER.error( "Station name not provided" );
            response.sendError( HttpServletResponse.SC_BAD_REQUEST,
                    "The station name must be passed as a value of the 'name' parameter for your " +
                            "request to be processed. " );
        }
        // if name is provided, look in the storage for the corresponding station
        requestedStation = DataStorage.findStation( stationName );
        // if it's not found in storage
        if (requestedStation == null)
        {

            response.sendError( HttpServletResponse.SC_BAD_REQUEST,
                    "Requested station not found in sources. \n " +
                            "Measurement data is available for the following stations: "
                            + DataStorage.getNamesOfStationsInStorage() );
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
    }

    private void convertDates()
    {
        startDate = startDateTime.toLocalDate();
        startTime = startDateTime.toLocalTime();
        endDate = endDateTime.toLocalDate();
        endTime = endDateTime.toLocalTime();

    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}
