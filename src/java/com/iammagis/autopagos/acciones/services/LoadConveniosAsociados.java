/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.services;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author root
 */
public class LoadConveniosAsociados extends org.apache.struts.action.Action {

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

        response.setHeader("Access-Control-Allow-Origin", "*"); 
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU"); 
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        ArrayList<Convenios> convenioses = new ArrayList<>(conveniosJpaController.findConveniosEntities());
        JSONObject jSONObject = DataGrid.getConveniosDataAnonimouse(convenioses);
        request.setAttribute("convenios", jSONObject);
        return mapping.findForward(SUCCESS);
    }
}
