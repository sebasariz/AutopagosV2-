/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.online.configuracion;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.TipoCuenta;
import com.iammagis.autopagos.jpa.beans.Usuario; 
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.TipoCuentaJpaController;
import java.io.PrintWriter;
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
public class SaveConvenioConfiguracion extends org.apache.struts.action.Action {

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
            Convenios conveniosAnterior = usuario.getConveniosIdconvenios();
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            conveniosAnterior = conveniosJpaController.findConvenios(conveniosAnterior.getIdconvenios());
            Convenios convenio = (Convenios) form;

            conveniosAnterior.setBanco(convenio.getBanco());
            conveniosAnterior.setNumeroCuenta(convenio.getNumeroCuenta());
            conveniosAnterior.setTitularCuenta(convenio.getTitularCuenta());
            conveniosAnterior.setDireccionRespuesta(convenio.getDireccionRespuesta());

            TipoCuentaJpaController tipoCuentaJpaController = new TipoCuentaJpaController(manager);
            TipoCuenta tipoCuenta = tipoCuentaJpaController.findTipoCuenta(convenio.getIdTipoCuenta());
            conveniosAnterior.setTipoCuentaidtipoCuenta(tipoCuenta);

            conveniosJpaController.edit(conveniosAnterior);
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
