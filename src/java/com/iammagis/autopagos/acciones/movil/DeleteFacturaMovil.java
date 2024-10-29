/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
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
public class DeleteFacturaMovil extends org.apache.struts.action.Action {

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
        String jsonArrayString = request.getParameter("json");

        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        if (usuario != null) {
            JSONArray jSONArray = new JSONArray(jsonArrayString);
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject id = jSONArray.getJSONObject(i);
                Factura facturaBorrar = facturaJpaController.findFactura(id.getInt("id"));
                //buscamso la factura por si ya existe 
                if (facturaBorrar != null) {
                    ArrayList<Usuario> usuarios = new ArrayList<>(facturaBorrar.getUsuarioCollection());
                    usuarios.remove(usuario);
                    System.out.println("facturaBorrar.getConveniosIdconvenios().getTipoConvenio(): " + facturaBorrar.getConveniosIdconvenios().getTipoConvenio());
                    if (!usuarios.isEmpty() || facturaBorrar.getConveniosIdconvenios().getTipoConvenio() == 2) {
                        facturaBorrar.setUsuarioCollection(usuarios);
                        facturaJpaController.edit(facturaBorrar);
                    } else {

                        TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
                        if (facturaBorrar != null && facturaBorrar.getTransaccionCollection() != null) {
                            ArrayList<Transaccion> transaccions = new ArrayList<Transaccion>(facturaBorrar.getTransaccionCollection());
                            for (Transaccion transaccion : transaccions) {
                                transaccion.setFacturaIdfactura(null);
                                transaccionJpaController.destroy(transaccion.getIdtransaccion());
                            }
                        }
                        System.out.println("eliminado facutra: " + id);
                        facturaJpaController.destroy(facturaBorrar.getIdfactura());
                    }
                }
            }
            usuario = usuarioJpaController.findUsuario(usuario.getIdUsuario());
            ArrayList<Factura> facturas = new ArrayList<>(usuario.getFacturaCollection());
            JSONArray arrayFacuturas = new JSONArray();
            for (Factura factura : facturas) {
                JSONObject jSONObjectFactura = new JSONObject();
                jSONObjectFactura.put("id", factura.getIdfactura());
                jSONObjectFactura.put("estado", factura.getEstadoIdestado().getNombre());
                if (factura.getFechaVencimiento() != null) {
                    jSONObjectFactura.put("fecha", factura.getFechaVencimiento().longValue());
                } else {
                    jSONObjectFactura.put("fecha", 0);
                }
                jSONObjectFactura.put("nombre", factura.getNombre());
                jSONObjectFactura.put("valor", factura.getValor());
                jSONObjectFactura.put("img", propertiesAccess.SERVER + "img/logoConvenios/" + factura.getConveniosIdconvenios().getLogo());
                arrayFacuturas.put(jSONObjectFactura);
            }
            jSONObject.put("facturas", arrayFacuturas);
        } else {
            jSONObject.put("error", "Error de session");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
