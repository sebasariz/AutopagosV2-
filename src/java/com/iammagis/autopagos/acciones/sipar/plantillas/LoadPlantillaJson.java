/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.sipar.plantillas;

import com.iammagis.autopagos.jpa.beans.Campo;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.FacturaTemplateJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class LoadPlantillaJson extends org.apache.struts.action.Action {

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
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");

            FacturaTemplateJpaController facturaTemplateJpaController = new FacturaTemplateJpaController(manager);
            int idFacturaTemplate = Integer.parseInt(request.getParameter("id"));
            FacturaTemplate facturaTemplate = facturaTemplateJpaController.findFacturaTemplate(idFacturaTemplate);
            jSONObject.put("nombre", facturaTemplate.getNombre());
            jSONObject.put("html", facturaTemplate.getHtml());
            jSONObject.put("id", facturaTemplate.getIdfacturaTemplate());
            ArrayList<Campo> campos = new ArrayList<>(facturaTemplate.getCampoCollection());
            JSONArray array = new JSONArray();
            for (Campo campo : campos) {
                JSONObject jSONObjectCampo = new JSONObject();
                jSONObjectCampo.put("nombre", campo.getNombre());
                jSONObjectCampo.put("referencia", campo.getReferencia());
                jSONObjectCampo.put("xls", campo.getXls());
                array.put(jSONObjectCampo);
            }
            jSONObject.put("campos", array); 
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
