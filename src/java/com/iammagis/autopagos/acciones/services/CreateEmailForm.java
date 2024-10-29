/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.services;

import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sebastianarizmendy
 */
public class CreateEmailForm extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

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
        
        String telefono = request.getParameter("telefono");
        String nombre =  request.getParameter("nombre");
        String email =  request.getParameter("email");
        String mensaje =request.getParameter("mensaje");
        
        String completo ="Nuevo mensaje creado por pagina AUTOPAGOS<br>";
        completo+="Telefono: "+telefono+"<br>";
        completo+="Nombre: "+nombre+"<br>";
        completo+="Email: "+email+"<br>";
        completo+="Mensaje: "+mensaje+".<br>";
        
        PropertiesAccess propertiesAccess = new PropertiesAccess();
        ArrayList<String> strings = new ArrayList<String>();
        strings.add(propertiesAccess.CORREO);
        new CorreoZoho("Contacto WEB", completo, strings).run();
        return null;
    }
}
