/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
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
public class LoadComprobantesMovil extends org.apache.struts.action.Action {

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

        request.setCharacterEncoding("UTF-8");
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        String token = request.getParameter("token");
        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        if (usuario != null) {
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            ArrayList<Transaccion> transaccions = transaccionJpaController.get10ComprobantesUsuarios(usuario.getIdUsuario());

            JSONArray arrayTransacciones = new JSONArray();
            for (Transaccion transaccion : transaccions) {

                JSONObject jSONObjectTransaccion = new JSONObject();
                jSONObjectTransaccion.put("id", transaccion.getIdtransaccion());
                jSONObjectTransaccion.put("token", transaccion.getIdTransaccion());
                jSONObjectTransaccion.put("fecha", transaccion.getFecha().longValue());
                jSONObjectTransaccion.put("tipo", transaccion.getTipoTransaccionidtipoTransaccion().getNombre());
                if (transaccion.getValor() != null) {
                    jSONObjectTransaccion.put("valor", transaccion.getValor());
                } else {
                    jSONObjectTransaccion.put("valor", 0);
                }
                if (transaccion.getFacturaIdfactura() != null) {
                    jSONObjectTransaccion.put("nombre", transaccion.getFacturaIdfactura().getNombre());
                    jSONObjectTransaccion.put("img", propertiesAccess.SERVER + "img/logoConvenios/" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getLogo());
                } else {
                    jSONObjectTransaccion.put("nombre", "Factura eliminada");
                    jSONObjectTransaccion.put("img", "");
                }

                arrayTransacciones.put(jSONObjectTransaccion);
            }
            jSONObject.put("transacciones", arrayTransacciones);
        } else {
            jSONObject.put("error", "Error de session");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
