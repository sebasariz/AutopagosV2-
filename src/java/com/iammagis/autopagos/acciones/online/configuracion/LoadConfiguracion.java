/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.online.configuracion;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.TipoCuenta;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.TipoCuentaJpaController;
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
public class LoadConfiguracion extends org.apache.struts.action.Action {

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
            String content = "/pages/online_configuracion.jsp";
            session.setAttribute("contenido", content);
            
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(usuario.getConveniosIdconvenios().getIdconvenios());
            if(convenios.getActivo()){
                convenios.setActivoString("Activo");
            }else{
                convenios.setActivoString("Inactivo");
            }
            convenios.setTipoCuenta(convenios.getTipoCuentaidtipoCuenta().getIdtipoCuenta());
            request.setAttribute("convenio", convenios);
            request.setAttribute("server", propertiesAccess.SERVER);
            //cargamos los tipos de cuentas
            TipoCuentaJpaController tipoCuentaJpaController = new TipoCuentaJpaController(manager);
            ArrayList<TipoCuenta> tipoCuentas = new ArrayList<>(tipoCuentaJpaController.findTipoCuentaEntities());
            request.setAttribute("tipoCuentas", tipoCuentas);
        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }
        return mapping.findForward(SUCCESS);
    }
}
