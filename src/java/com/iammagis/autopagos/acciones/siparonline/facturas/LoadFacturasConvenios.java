/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
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

/**
 *
 * @author Usuario
 */
public class LoadFacturasConvenios extends org.apache.struts.action.Action {

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
        ActionErrors errores = new ActionErrors();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            String content = "/pages/sipar_online_facturas.jsp";
            session.setAttribute("contenido", content);


            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            ArrayList<Factura> facturas = new ArrayList<>(facturaJpaController.getFacturasFromConvenio(usuario.getConveniosIdconvenios().getIdconvenios()));
            request.setAttribute("tabla", DataGrid.getFacturasGrid(facturas));

            //cargamos plantillas
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(usuario.getConveniosIdconvenios().getIdconvenios());
            ArrayList<FacturaTemplate> facturaTemplates = new ArrayList<>(convenios.getFacturaTemplateCollection());
            request.setAttribute("facturaTemplates", facturaTemplates);
            
            //setting convenio
            request.setAttribute("convenio", usuario.getConveniosIdconvenios());
        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }
        return mapping.findForward(SUCCESS);
    }
}