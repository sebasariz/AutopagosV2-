/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.daemon;

import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.support.DaemonsControl;
import com.iammagis.autopagos.jpa.beans.support.VariablesSession;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import java.io.PrintWriter; 
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class GuardarConfigDaemin extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

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

        HttpSession session = request.getSession();
        if (session != null) {
            if (VariablesSession.context == null) {
                VariablesSession.context = getServlet().getServletContext().getRealPath("");
            }
            int estado = Integer.parseInt(request.getParameter("estado"));
            String fecha = request.getParameter("fecha");
            int estadoPrivados = Integer.parseInt(request.getParameter("estadoPrivados"));
            int estadoFacturacion = Integer.parseInt(request.getParameter("estadoFacturacion"));
            String correo = request.getParameter("correo");
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");
 
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConfigJpaController configJpaController = new ConfigJpaController(manager);
            Config config = configJpaController.findConfig(1);
            if (estadoFacturacion == 1) {
                config.setAutopagosDaemonFacturacionActivo(true);
            } else {
                config.setAutopagosDaemonFacturacionActivo(false);
            }
            if (estadoPrivados == 1) {
                config.setAutopagosDaemonPrivadosActivo(true);
            } else {
                config.setAutopagosDaemonPrivadosActivo(false);
            }
            if (estado == 1) {
                config.setAutopaogsDaemon(true);
            } else {
                config.setAutopaogsDaemon(false);
            }
            config.setAutopagosEmailSoporte(correo);
            config.setAutopagosDaemonUsuario(user);
            config.setAutopagosDaemonPass(pass);
            //formateamos la fecha
            try {
                Date date = simpleDateFormat.parse(fecha); 
                config.setAutopagosDaemonFechaFacturacion(BigInteger.valueOf(date.getTime()));
                configJpaController.edit(config);
                DaemonsControl.startStopDaemons();
            } catch (Exception e) {
                e.printStackTrace();
                jSONObject.put("error", "El formato de hora no coincide (HH:MM A)");
            }

        } else {
            MessageResources messages = MessageResources.getMessageResources("com.iammagis.autopagos.resources");
            String message = messages.getMessage(request.getLocale(), "erros.tiempoAgotado");
            jSONObject.put("error", message);
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
