/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.export;

import dhtmlx.xml2excel.ExcelWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sebasariz
 */
public class XlsGenerator extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM-dd hh:mm a");

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
 
        String xml = request.getParameter("grid_xml");
        xml = URLDecoder.decode(xml, "UTF-8");
        response.setContentType("application/xlsx"); 
        response.setHeader("Content-Disposition", "inline; filename=Reporte-" + simpleDateFormat.format(new Date()) + ".xlsx");
        (new ExcelWriter()).generate(xml, response);

        return null;
    }
}
