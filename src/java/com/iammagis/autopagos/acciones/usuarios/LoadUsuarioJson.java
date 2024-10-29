/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.usuarios;

import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
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
public class LoadUsuarioJson extends org.apache.struts.action.Action {

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

        JSONObject jSONObject = new JSONObject();
        HttpSession session = request.getSession();
        if (session != null) {
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            long idUsuario = Long.parseLong(request.getParameter("idUsuario"));
            Usuario usuario = usuarioJpaController.findUsuario(idUsuario);
            jSONObject.put("id", usuario.getIdUsuario());
            jSONObject.put("nombre", usuario.getNombre());
            jSONObject.put("apellidos", usuario.getApellidos());
            jSONObject.put("celular", usuario.getCelular());
            jSONObject.put("numeroDeDocumento", usuario.getNumeroDeDocumento());
            jSONObject.put("email", usuario.getEmail());
            jSONObject.put("pass", usuario.getPass());
            jSONObject.put("idTipoUsuario", usuario.getTipousuarioIDTipoUsuario().getIDTipoUsuario());
            if (usuario.getConveniosIdconvenios() != null) {
                jSONObject.put("convenio", usuario.getConveniosIdconvenios().getIdconvenios());
            } 
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
