/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Codigo;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.CodigoJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author root
 */
public class LoadInvitacionMovil extends org.apache.struts.action.Action {

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
        
        request.setCharacterEncoding("UTF-8");
        String sesionToken = request.getParameter("token");
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        
        Usuario usuario = usuarioJpaController.findByToken(sesionToken);
        if (usuario != null) {
            //existe el usuario
            //generar el codigo de invitacion
            CodigoJpaController codigoJpaController = new CodigoJpaController(manager);
            Codigo codigo = codigoJpaController.getCodigoByUsuario(usuario.getIdUsuario());
            if (codigo == null) {
                codigo = new Codigo();
                String codigoString = UUID.randomUUID().toString().substring(0, 6);
                codigo.setCodigo(codigoString);
                codigo.setUsado(Boolean.FALSE);
                codigo.setUsuarioidUsuario(usuario);
                codigoJpaController.create(codigo);
            }
            request.setAttribute("nombre", usuario.getNombre()+" "+usuario.getApellidos());
            request.setAttribute("codigo", codigo.getCodigo());
            //codigo generado
            return mapping.findForward(SUCCESS);
        } else {
            return mapping.findForward("error");
        }
        
    }
};
