/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.daemon;

import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class LoadGestionDaemon extends org.apache.struts.action.Action {

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

        HttpSession session = request.getSession();
        ActionErrors errores = new ActionErrors();
        if (session != null) {
            String content = "/pages/daemon.jsp";
            session.setAttribute("contenido", content);

            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConfigJpaController configJpaController = new ConfigJpaController(manager);
            Config config = configJpaController.findConfig(1);
            if (config.getAutopagosDaemonFechaFacturacion() != null) {
                config.setAutopagosFechaString(simpleDateFormat.format(new Date(config.getAutopagosDaemonFechaFacturacion().longValue())));
            } else {
                config.setAutopagosFechaString(simpleDateFormat.format(new Date()));
            }
            request.setAttribute("config", config);

        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }

        return mapping.findForward(SUCCESS);
    }
}