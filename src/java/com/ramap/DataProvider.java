/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramap;

import au.com.bytecode.opencsv.CSVReader;
import ents.DatasetController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author edsgICT-WB52
 */
@WebServlet(name = "DataProvider", urlPatterns = {"/DataProvider"})
public class DataProvider extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        DatasetController dataCon = new DatasetController();
        List<String[]> resultRows = null;
        List<String[]> resultRowsPrev = null;
        String desc = "";        
        try {                                    
            JSONObject jo= getJSONObject("http://50.57.74.15/api/3/action/package_show?id=",dataCon.getDatasetId(request.getParameter("title")));
            JSONObject jsonObj = (JSONObject) jo.get("result");
            JSONArray resources = (JSONArray)jsonObj.get("resources"); 
            Integer year = Integer.parseInt(request.getParameter("year").trim()); 
            Integer formerYear = year-1;
            int status = 0;
            for(Object obj: resources){                
               URL url = replaceHost(new URL((String)((JSONObject)obj).get("url")));
               URLConnection con = url.openConnection();
                con.setAllowUserInteraction(true);
                con.setUseCaches(false);
                con.connect();       
                CSVReader reader = new CSVReader(new InputStreamReader(con.getInputStream()));
                List<String[]> rows = reader.readAll();
                String yearFromDoc = rows.get(1)[getYearColumn(rows)].trim();
                if(yearFromDoc.equals(request.getParameter("year").trim())){                    
                    desc = (String) ((JSONObject)obj).get("description");
                    resultRows = rows;
                    status++;
                    if(status>1)
                        break;                                                                               
                }else if(yearFromDoc.equals(Integer.toString(formerYear))){
                    resultRowsPrev = rows;
                    status++;
                    if(status>1)
                        break;
                }                                                  
            }
            out.print(produceJSON(resultRows,resultRowsPrev,desc,request.getParameter("title"))); 
        } catch(Exception e){
                Logger.getLogger("DataProvider").log(Level.SEVERE, null, e);
            }finally {            
            out.close();
        }
    }
    
    private List<Double> getPercentages(List<String[]> prev, List<String[]> present){
        List<Double> percentages = new ArrayList<Double>();
        for(int i=1;i<present.size();i++){
            String[] row = present.get(i);
            String[] prevRow = prev.get(i);
            Float a = Float.parseFloat(row[5]);
            Float b = Float.parseFloat(prevRow[5]);
            Float c;
            if(a==0)
                c=-100.0F;
            else
                c = ((a-b)/b)*100;            
            percentages.add(Math.ceil(c));
        }
        return percentages;
    }
    private URL replaceHost(URL url) throws MalformedURLException{
        URL newUrl = new URL(url.getProtocol()+"://50.57.74.15"+url.getPath());        
        return newUrl;
    }
    
    private JSONObject produceJSON(List<String[]> lines, List<String[]> prevLines,String desc,String title){  
        JSONObject jObject = new JSONObject();
        JSONArray array = new JSONArray();
        List<Double> percentages = null;
        if(prevLines!=null)
            percentages = getPercentages(prevLines,lines);
        
        int i=0;
        int j=0;
        for(String[] line: lines){  
            if(i==0){  
                i++;
                continue;
            }
            JSONArray innerArray = new JSONArray();
            for(String column: line){
                innerArray.add(column);
            }
            if(prevLines!=null)
                innerArray.add(percentages.get(j));
            array.add(innerArray);
            j++;
            
        }     
        jObject.put("title",title);
        jObject.put("description", desc);
        jObject.put("results",array);        
        return jObject;
    }
    
    private Integer getYearColumn(List<String[]> lines){
        Integer col = 0;
        for(String column: lines.get(0)){
            if(column.trim().equalsIgnoreCase("year")){
                break;
            }
            col++;
        }
        return col;
    }
    private JSONObject getJSONObject(String ur, String path) throws IOException, ParseException{
        JSONObject jo;        
        JSONParser parser = new JSONParser();            
        URL url = new URL(ur+path);
        URLConnection con = url.openConnection();
        con.setAllowUserInteraction(true);
        con.setUseCaches(false);
        con.connect();                            
        Object obj = parser.parse(new InputStreamReader(con.getInputStream()));
        jo = (JSONObject)obj;        
        return jo;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
