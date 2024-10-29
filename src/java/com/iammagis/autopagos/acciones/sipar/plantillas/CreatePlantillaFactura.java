/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.sipar.plantillas;

import com.iammagis.autopagos.jpa.beans.Campo;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.CampoJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
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
public class CreatePlantillaFactura extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAccess = new PropertiesAccess();

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

            String nombre = request.getParameter("nombre");
            String html = request.getParameter("html");
            String variables = request.getParameter("variables");

            if (html.contains("$logo")) {
                html = html.replace("$logo", propertiesAccess.SERVER + usuario.getConveniosIdconvenios().getLogo());
            }

            FacturaTemplateJpaController facturaTemplateJpaController = new FacturaTemplateJpaController(manager);
            CampoJpaController campoJpaController = new CampoJpaController(manager);
            JSONArray array = new JSONArray(variables);

            FacturaTemplate facturaTemplate = new FacturaTemplate();
            facturaTemplate.setConveniosIdconvenios(usuario.getConveniosIdconvenios());
            facturaTemplate.setHtml(html);
            facturaTemplate.setNombre(nombre);

            facturaTemplate = facturaTemplateJpaController.create(facturaTemplate);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jSONObject1 = array.getJSONObject(i);
                Campo campo = new Campo();
                campo.setXls(jSONObject1.getString("xls"));
                campo.setReferencia(jSONObject1.getString("variable"));
                campo.setFacturaTemplateidfacturaTemplate(facturaTemplate);
                campoJpaController.create(campo);
            }
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(usuario.getConveniosIdconvenios().getIdconvenios());
            ArrayList<FacturaTemplate> facturaAutopagoses = new ArrayList<>(convenios.getFacturaTemplateCollection());
             
            
            jSONObject.put("tabla", DataGrid.getPlantillasGrid(facturaAutopagoses)); 
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