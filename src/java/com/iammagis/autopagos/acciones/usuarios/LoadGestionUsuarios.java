/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.usuarios;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.TipoUsuario;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.TipoUsuarioJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
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
public class LoadGestionUsuarios extends org.apache.struts.action.Action {

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
            String content = "/pages/usuarios.jsp";
            session.setAttribute("contenido", content);

            //CARGAMOS LA LISTA DE USUARIOS
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            ArrayList<Usuario> usuarios = new ArrayList<>(usuarioJpaController.findUsuarioEntities());
            JSONObject jSONObject = DataGrid.getUserData(usuarios);
            request.setAttribute("usuarios", jSONObject);
            //cargamos los usuarios de utored
            int usuariosAutored = usuarioJpaController.countUsersAutored();
            request.setAttribute("usuariosAutored", usuariosAutored);
            int usuariosSiparEInternos = usuarioJpaController.countUsersSiperEInternos();
            request.setAttribute("usuariosSipar", usuariosSiparEInternos);
            //cargamos los tipos de usuarios
            TipoUsuarioJpaController tipoUsuarioJpaController = new TipoUsuarioJpaController(manager);
            ArrayList<TipoUsuario> tipoUsuarios = new ArrayList<>(tipoUsuarioJpaController.findTipoUsuarioEntities());
            request.setAttribute("tipousuarios", tipoUsuarios);
            //cargamos los convenios
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            ArrayList<Convenios> convenioses = new ArrayList<>(conveniosJpaController.findConveniosEntities());
            request.setAttribute("convenios", convenioses);
            
            
        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }
        return mapping.findForward(SUCCESS);
    }
}
