/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored;
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
 * @author Usuario
 */
public class LoadReferidosMovil extends org.apache.struts.action.Action {

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
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        String token = request.getParameter("token");

        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        JSONArray arrayArbol = new JSONArray();
        if (usuario != null) {
            if (request.getParameter("id") == null) {
                ArrayList<UsuarioHasUsuarioReferidosAutored> usuarioReferidosAutoreds = new ArrayList<>(usuario.getUsuarioHasUsuarioReferidosAutoredCollection1());
                for (UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored : usuarioReferidosAutoreds) {
                    Usuario usuarioReferido = usuarioHasUsuarioReferidosAutored.getUsuario();
                    JSONObject jSONObjectUsuario = new JSONObject();
                    jSONObjectUsuario.put("id", usuarioReferido.getIdUsuario());
                    jSONObjectUsuario.put("img", usuarioReferido.getFoto());
                    jSONObjectUsuario.put("nombre", usuarioReferido.getNombre() + " " + usuarioReferido.getApellidos());
                    jSONObjectUsuario.put("email", usuarioReferido.getEmail());
                    jSONObjectUsuario.put("puntos", usuarioHasUsuarioReferidosAutored.getPuntos());
                    if (!usuario.getFacturaCollection().isEmpty()) {
                        ArrayList<Factura> facturas = new ArrayList<>(usuarioReferido.getFacturaCollection());
                        if (!facturas.isEmpty()) {
                            jSONObjectUsuario.put("fecha", facturas.get(facturas.size() - 1).getFechaCreacion().longValue());
                        } else {
                            jSONObjectUsuario.put("fecha", 0);
                        }
                    } else {
                        jSONObjectUsuario.put("fecha", System.currentTimeMillis());
                    }
                    jSONObjectUsuario.put("referidos", usuarioReferido.getUsuarioHasUsuarioReferidosAutoredCollection1().size());
                    arrayArbol.put(jSONObjectUsuario);
                }
                jSONObject.put("referidos", arrayArbol);
            } else {
                long id = Long.parseLong(request.getParameter("id"));
                Usuario usuarioReferente = usuarioJpaController.findUsuario(id);
                ArrayList<UsuarioHasUsuarioReferidosAutored> usuarioReferidosAutoreds = new ArrayList<>(usuarioReferente.getUsuarioHasUsuarioReferidosAutoredCollection1());
                for (UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored : usuarioReferidosAutoreds) {
                    Usuario usuarioReferido = usuarioHasUsuarioReferidosAutored.getUsuario();
                    JSONObject jSONObjectUsuario = new JSONObject();
                    jSONObjectUsuario.put("id", usuarioReferido.getIdUsuario());
                    jSONObjectUsuario.put("img", usuarioReferido.getFoto());
                    jSONObjectUsuario.put("nombre", usuarioReferido.getNombre() + " " + usuarioReferido.getApellidos());
                    jSONObjectUsuario.put("email", usuarioReferido.getEmail());
                    jSONObjectUsuario.put("puntos", usuarioHasUsuarioReferidosAutored.getPuntos());
                    if (!usuarioReferido.getFacturaCollection().isEmpty()) {
                        ArrayList<Factura> facturas = new ArrayList<>(usuarioReferido.getFacturaCollection());
                        if (!facturas.isEmpty()) {
                            jSONObjectUsuario.put("fecha", facturas.get(facturas.size() - 1).getFechaCreacion().longValue());
                        } else {
                            jSONObjectUsuario.put("fecha", 0);
                        }
                    } else {
                        jSONObjectUsuario.put("fecha",0);
                    }
                    jSONObjectUsuario.put("referidos", usuarioReferido.getUsuarioHasUsuarioReferidosAutoredCollection1().size());
                    arrayArbol.put(jSONObjectUsuario);
                } 
                jSONObject.put("referidos", arrayArbol);
            }
        } else {
            jSONObject.put("error", "Error de sesion");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }

}
