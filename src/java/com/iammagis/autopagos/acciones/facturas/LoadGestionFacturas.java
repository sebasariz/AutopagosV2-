/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.facturas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class LoadGestionFacturas extends org.apache.struts.action.Action {

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
        ActionErrors errores = new ActionErrors();
        if (session != null) {
            String content = "/pages/facturas.jsp";
            session.setAttribute("contenido", content);

            
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU"); 
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            ArrayList<FacturaAutopagos> facturaAutopagoses = new ArrayList<>(facturaAutopagosJpaController.findFacturaAutopagosEntities());
              JSONObject jSONObject = DataGrid.getFacturasAutopagosData(facturaAutopagoses);
            request.setAttribute("facturas", jSONObject);
            //cargamos los convenios
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            ArrayList<Convenios> convenioses = new ArrayList<>(conveniosJpaController.findConveniosEntities());
            request.setAttribute("convenios", convenioses);
            //facturas emitidas
            request.setAttribute("facturasemitidas", facturaAutopagoses.size());
            //facturas pagadas
            int facturaspagadas=facturaAutopagosJpaController.getFacturasPagadas();
            request.setAttribute("facturaspagadas", facturaspagadas);
            request.setAttribute("iva", propertiesAccess.IVA);
            
            
            
        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }
        return mapping.findForward(SUCCESS);
    }
}