package com.evnica.interop.main;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Class: ReportServlet
 * Version: 0.1
 * Created on 10.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */

@WebServlet (name = "ReportServlet",
             loadOnStartup = 1,
             urlPatterns = "/pegel")
public class ReportServlet extends HttpServlet
{

    @Override
    public void init()
    {

        /**TODO read and convert data from all the files in the resource folder*/


    }




}
