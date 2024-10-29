/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.services.recargas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author sebastianarizmendy
 */
public class LoadConveniosRecargas extends org.apache.struts.action.Action {

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

        JSONObject jSONObject = new JSONObject();
        if (request.getParameter("token") == null) {
            jSONObject.put("error", "parametro token requerido");
        } else if (request.getParameter("email") == null) {
            jSONObject.put("error", "parametro email requerido");
        } else if (request.getParameter("pass") == null) {
            jSONObject.put("error", "parametro pass requerido");
        } else {
            String token = request.getParameter("token");
            String user = request.getParameter("email");
            String pass = request.getParameter("pass");
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            Usuario usuario = usuarioJpaController.findByTokenUserAndPass(token, user, pass); 
            if (usuario != null) {
                ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
                ArrayList<Convenios> convenioses = conveniosJpaController.getConveniosRecarga();
                JSONArray arrayConvenios = new JSONArray();
                for (Convenios convenios : convenioses) {
                    JSONObject jSONObjectConveino = new JSONObject();
                    jSONObjectConveino.put("id", convenios.getIdconvenios());
                    jSONObjectConveino.put("nombre", convenios.getNombre());
                    jSONObjectConveino.put("img", propertiesAccess.SERVER + "/img/logoConvenios/" + convenios.getLogo());
                    jSONObjectConveino.put("ref", convenios.getTextoGuiaTercero());
                    arrayConvenios.put(jSONObjectConveino);
                }
                jSONObject.put("convenios", arrayConvenios);
            } else {
                jSONObject.put("error", "Usuario - pass o token incorrecto");
            }
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;

    }
}
