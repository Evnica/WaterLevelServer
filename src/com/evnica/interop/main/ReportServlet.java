package com.evnica.interop.main;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
             urlPatterns = "/pegel")
public class ReportServlet extends HttpServlet
{

    @Override
    public void init() throws ServletException
    {
        try
        {
            List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelServer/app/WEB-INF/resources" );
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
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        super.doGet( request, response );
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        super.doPost( request, response );
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}
