/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.facturas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.FacturaCreator;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
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
public class CreateFacturaPlanAutopagos extends org.apache.struts.action.Action {

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

        HttpSession session = request.getSession();
        JSONObject jSONObject = new JSONObject();
        if (session != null) {
            //aqui incluimos todo el codigo de la creacion de usauario
            FacturaAutopagos facturaAutopagos = (FacturaAutopagos) form;

            //FacturaCreator.generarFacturaAutopagosLibre(facturaAutopagos); 
            //retornamos la lista de usuarios 
            Calendar calendarFechaInicial = Calendar.getInstance();
            calendarFechaInicial.setTimeInMillis(facturaAutopagos.getFechaInicial());
            calendarFechaInicial.set(Calendar.HOUR, 0);
            calendarFechaInicial.set(Calendar.MINUTE, 0);
            calendarFechaInicial.set(Calendar.SECOND, 0);
            calendarFechaInicial.set(Calendar.MILLISECOND, 0);
            Calendar calendarFechaFinal = Calendar.getInstance();
            calendarFechaFinal.setTimeInMillis(facturaAutopagos.getFechaFinal());
            calendarFechaFinal.set(Calendar.HOUR, 0);
            calendarFechaFinal.set(Calendar.MINUTE, 0);
            calendarFechaFinal.set(Calendar.SECOND, 0);
            calendarFechaFinal.set(Calendar.MILLISECOND, 0);
            
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(facturaAutopagos.getIdconvenio());
            ArrayList<String> emails = new ArrayList<>();
            for(Usuario usuario:convenios.getUsuarioCollection()){
                emails.add(usuario.getEmail());
            }
            FacturaCreator.generarFacturaAutopagos(convenios,emails,calendarFechaInicial,calendarFechaFinal);
            
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            ArrayList<FacturaAutopagos> facturaAutopagoses = new ArrayList<>(facturaAutopagosJpaController.findFacturaAutopagosEntities());
            jSONObject = DataGrid.getFacturasAutopagosData(facturaAutopagoses);
            request.setAttribute("facturas", jSONObject);
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
