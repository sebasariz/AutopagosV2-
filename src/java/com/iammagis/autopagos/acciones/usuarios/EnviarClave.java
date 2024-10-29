/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.usuarios;

import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Usuario
 */
public class EnviarClave extends org.apache.struts.action.Action {

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

        ActionErrors errores = new ActionErrors();
        Usuario usuario = (Usuario) form;
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuarioExiste = usuarioJpaController.emailExist(usuario);
        if (usuarioExiste != null) {
            String template = CorreoZoho.getTemplate(CorreoZoho.recordarUsuarioTemplate);
            template = template.replace("$server", propertiesAccess.SERVER); 
            template = template.replace("$emailComercio", usuarioExiste.getEmail());
            template = template.replace("$contrasenaComercio", usuarioExiste.getPass());
 
            ArrayList<String> strings = new ArrayList<>();
            strings.add(usuario.getEmail());

            CorreoZoho correoZoho = new CorreoZoho("Recuperación de clave AUTOPAGOS.CO", template, strings);
            correoZoho.start();
            errores.add("register", new ActionMessage("ok.usuario.enviado"));
            saveErrors(request, errores);
            return mapping.findForward(SUCCESS);
        } else {
            errores.add("register", new ActionMessage("erros.noEmailExiste"));
            saveErrors(request, errores);
            return mapping.findForward("error");
        }
    }
}
