/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.sesion;

import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
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
 * @author sebastianarizmendy
 */
public class LoadResumenPlataforma extends org.apache.struts.action.Action {

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
        if (session != null) {
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            Usuario usuarioLoged = (Usuario) session.getAttribute("usuario");
            String content = "";
            if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 1) {
                //root
                content = "/contenedor/root.jsp";
//                FullCargaAutopagosServices.getSaldo();
                //cargamos los indicadores globbales 
                DataGrid.getGlobalIndicators(request);
            } else if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 2) {
                //Sipar
                content = "contenedor/sipar.jsp";
                request.setAttribute("convenio", usuarioLoged.getConveniosIdconvenios());
            } else if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 3) {
                //Online
                content = "contenedor/online.jsp";
                //cargamos los datos del convenio
                double saldo = usuarioLoged.getConveniosIdconvenios().getValorRecaudado() - usuarioLoged.getConveniosIdconvenios().getValorComisiones();
                usuarioLoged.getConveniosIdconvenios().setSaldo(saldo);
                request.setAttribute("convenio", usuarioLoged.getConveniosIdconvenios());

            } else if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 4) {
                //Autored
                errores.add("register", new ActionMessage("erros.noUsuAndPass"));
                saveErrors(request, errores);
                return mapping.findForward("error");
            }
            session.setAttribute("contenido", content);

        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }
        return mapping.findForward(SUCCESS);

    }
}
