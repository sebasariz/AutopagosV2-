/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.comercio;

import com.iammagis.autopagos.jpa.beans.support.Correo;
import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

/**
 *
 * @author sebastianarizmendy
 */
public class SendReferenciaComercio extends org.apache.struts.action.Action {

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

        JSONObject jSONObject = new JSONObject();

        String nombre = request.getParameter("nombre");
        String contacto = request.getParameter("contacto");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        String mensaje = "Nuevo comercio referido<br>";
        mensaje += "nombre: " + nombre + "<br>";
        mensaje += "contacto: " + contacto + "<br>";
        mensaje += "telefono: " + telefono + "<br>";
        mensaje += "correo: " + correo + "<br>";

        ArrayList<String> correos = new ArrayList<>();
        correos.add("alejandro.gomez@autopagos.co");
        correos.add("sebasariz@autopagos.co");

        CorreoZoho correoZoho = new CorreoZoho("Nuevo comercio referido", mensaje, correos);
        correoZoho.start();

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
