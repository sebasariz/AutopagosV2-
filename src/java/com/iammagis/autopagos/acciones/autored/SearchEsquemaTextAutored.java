/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.autored;

import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sebastianarizmendy
 */
public class SearchEsquemaTextAutored extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private static JSONArray array;

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
        array = new JSONArray();
        HttpSession session = request.getSession();
        JSONObject jSONObject = new JSONObject();
        if (session != null) {
            String search = request.getParameter("text");
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            //obtenemsos las transacciones realizadas durante un periodo de tiempo
            request.setCharacterEncoding("UTF-8");
            //obtener los usuari os papa QUE SOY YO EL 36
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            ArrayList<Usuario> usuarios = usuarioJpaController.findByNameContains(search);
            System.out.println("usuarios: " + usuarios.size());
            for (Usuario usuarioPapa : usuarios) {
                JSONObject jSONObjectUsuario = new JSONObject();
                jSONObjectUsuario.put("id", usuarioPapa.getIdUsuario());
                jSONObjectUsuario.put("img", usuarioPapa.getFoto()); 
                String texto = usuarioPapa.getNombre() + " " + usuarioPapa.getApellidos() + " (" + usuarioPapa.getEmail() + ")"
                        + " == Referidos: " + usuarioPapa.getUsuarioHasUsuarioReferidosAutoredCollection1().size();
                if (!usuarioPapa.getFacturaCollection().isEmpty()) {
                    ArrayList<Factura> facturas = new ArrayList<>(usuarioPapa.getFacturaCollection());
                    if (!facturas.isEmpty()) {
                        texto += " == fecha ultimo pago: " + simpleDateFormat.format(new Date(facturas.get(facturas.size() - 1).getFechaCreacion().longValue()));
                    }
                }
                jSONObjectUsuario.put("text", texto);
                jSONObjectUsuario.put("nodes", setArray(usuarioPapa));
                array.put(jSONObjectUsuario);

            }
            jSONObject.put("data", array);

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

    private JSONArray setArray(Usuario usuario) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        ArrayList<UsuarioHasUsuarioReferidosAutored> usuarioReferidosAutoreds = new ArrayList<>(usuario.getUsuarioHasUsuarioReferidosAutoredCollection1());
        for (UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored : usuarioReferidosAutoreds) {
            Usuario usuarioReferido = usuarioHasUsuarioReferidosAutored.getUsuario();
            JSONObject jSONObjectUsuario = new JSONObject();
            jSONObjectUsuario.put("id", usuarioReferido.getIdUsuario());
            jSONObjectUsuario.put("img", usuarioReferido.getFoto());

            String texto = usuarioReferido.getNombre() + " " + usuarioReferido.getApellidos() + " (" + usuarioReferido.getEmail() + ")"
                    + " == Refediros: " + usuarioReferido.getUsuarioHasUsuarioReferidosAutoredCollection1().size();
            if (!usuarioReferido.getFacturaCollection().isEmpty()) {
                ArrayList<Factura> facturas = new ArrayList<>(usuarioReferido.getFacturaCollection());
                if (!facturas.isEmpty()) {
                    texto += " == fecha ultimo pago: " + simpleDateFormat.format(new Date(facturas.get(facturas.size() - 1).getFechaCreacion().longValue()));
                }
            }
            jSONObjectUsuario.put("text", texto);
            jSONObjectUsuario.put("nodes", setArray(usuarioReferido));

            jSONArray.put(jSONObjectUsuario);
        }
        return jSONArray;
    }
}
