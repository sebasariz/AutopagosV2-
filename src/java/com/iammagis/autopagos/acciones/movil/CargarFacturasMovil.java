/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class CargarFacturasMovil extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAccess = new PropertiesAccess();
    EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
    FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
    JSONArray arrayFacuturas;

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
        String token = request.getParameter("token");
        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        if (usuario != null) {
            arrayFacuturas = new JSONArray();
            ArrayList<Factura> facturas = new ArrayList<>(usuario.getFacturaCollection());
            System.out.println("facturas de usuario: " + facturas.size());
            loadFacturas(facturas); 
            System.out.println("respuesta conjunta: " + arrayFacuturas);
            jSONObject.put("facturas", arrayFacuturas);  
            jSONObject.put("puntos", usuario.getPuntos());
            jSONObject.put("inscritas", usuario.getFacturaCollection().size());
            jSONObject.put("pendientes", usuario.getFacturaCollection().size());
        } else {
            jSONObject.put("error", "Error de session");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
 
    private void loadFacturas(ArrayList<Factura> facturas) {
        for (Factura factura : facturas) {
            try {
                JSONObject jSONObjectFactura = new JSONObject();
                if (factura.getConveniosIdconvenios().getTipoConvenio() == 2) {
                    jSONObjectFactura.put("id", factura.getIdfactura());
                    jSONObjectFactura.put("estado", factura.getEstadoIdestado().getNombre());
                    jSONObjectFactura.put("estadoid", factura.getEstadoIdestado().getIdestado());
                    jSONObjectFactura.put("comision_fija", factura.getConveniosIdconvenios().getValorFijoUsuario());
                    jSONObjectFactura.put("comision_variable", factura.getConveniosIdconvenios().getValorVariableUsuario());
                    
                    if (factura.getFechaVencimiento() != null) {
                        jSONObjectFactura.put("fecha", factura.getFechaVencimiento().longValue());
                    } else {
                        jSONObjectFactura.put("fecha", 0);
                    }
                    jSONObjectFactura.put("convenio", factura.getConveniosIdconvenios().getNombre());
                    if (factura.getNombre() != null) {
                        jSONObjectFactura.put("nombre", factura.getNombre());
                    } else {
                        jSONObjectFactura.put("nombre", factura.getConveniosIdconvenios().getNombre());

                    }
                    jSONObjectFactura.put("referencia", factura.getReferencia() + "");
                    if (factura.getValor() != null) {
                        jSONObjectFactura.put("valor", factura.getValor());
                    } else {
                        jSONObjectFactura.put("valor", 0);
                    }
                    jSONObjectFactura.put("img", propertiesAccess.SERVER + "img/logoConvenios/" + factura.getConveniosIdconvenios().getLogo());
                } else if (factura.getConveniosIdconvenios().getTipoConvenio() == 3) {

                    //JSONObject jSONObjectRespuesta = FullCargaAutopagosServices.getEstado(factura.getConveniosIdconvenios(), factura.getReferencia());
                    /*System.out.println("Factura nueva: " + factura.getNombre() + "Ref: " + factura.getReferencia() + " convenio: " + factura.getConveniosIdconvenios().getNombre() + " jSONObjectRespuesta: " + jSONObjectRespuesta);
                    if (jSONObjectRespuesta.has("estado") && jSONObjectRespuesta.getBoolean("estado") && jSONObjectRespuesta.getString("codigo").equals("00")) {
                        //esta disponible para ser asociada
                        String codigoFull = jSONObjectRespuesta.getString("codfullcarga");
                        double valor = jSONObjectRespuesta.getDouble("valor");
                        factura.setValor(valor);
                        factura.setCodigoFullcarga(codigoFull);

                        long horas48Despues = System.currentTimeMillis() - 172800000;
                        if ((valor != 0 && (factura.getEstadoIdestado().getIdestado() == 1 || factura.getEstadoIdestado().getIdestado() == 3)) || (valor != 0 && factura.getFechaEmision().longValue() < horas48Despues && factura.getEstadoIdestado().getIdestado() == 2)) {
                            factura.setEstadoIdestado(new Estado(1));
                        }
                        factura = facturaJpaController.edit(factura);
                        jSONObjectFactura.put("id", factura.getIdfactura());
                        jSONObjectFactura.put("estado", factura.getEstadoIdestado().getNombre());
                        jSONObjectFactura.put("estadoid", factura.getEstadoIdestado().getIdestado());
                        if (factura.getFechaVencimiento() != null) {
                            jSONObjectFactura.put("fecha", factura.getFechaVencimiento().longValue());
                        } else {
                            jSONObjectFactura.put("fecha", 0);
                        }
                        jSONObjectFactura.put("convenio", factura.getConveniosIdconvenios().getNombre());
                        jSONObjectFactura.put("nombre", factura.getNombre());
                        jSONObjectFactura.put("referencia", factura.getReferencia() + "");
                        if (factura.getValor() != null) {
                            jSONObjectFactura.put("valor", factura.getValor());
                        } else {
                            jSONObjectFactura.put("valor", 0);
                        }
                        jSONObjectFactura.put("img", propertiesAccess.SERVER + "img/logoConvenios/" + factura.getConveniosIdconvenios().getLogo());
                    } else if (jSONObjectRespuesta.has("codigo") && jSONObjectRespuesta.getString("codigo").equals("02") && jSONObjectRespuesta.getString("msg").contains("FACTURA YA PAGADA")) {
                        //factura ya se encuentra pagada
                        factura.setEstadoIdestado(new Estado(2));
                        factura.setValor(0D);
                        factura = facturaJpaController.edit(factura);
                        jSONObjectFactura.put("id", factura.getIdfactura());
                        jSONObjectFactura.put("estado", factura.getEstadoIdestado().getNombre());
                        jSONObjectFactura.put("estadoid", factura.getEstadoIdestado().getIdestado());
                        if (factura.getFechaVencimiento() != null) {
                            jSONObjectFactura.put("fecha", factura.getFechaVencimiento().longValue());
                        } else {
                            jSONObjectFactura.put("fecha", 0);
                        }
                        jSONObjectFactura.put("convenio", factura.getConveniosIdconvenios().getNombre());
                        jSONObjectFactura.put("nombre", factura.getNombre());
                        jSONObjectFactura.put("referencia", factura.getReferencia() + "");
                        if (factura.getValor() != null) {
                            jSONObjectFactura.put("valor", factura.getValor());
                        } else {
                            jSONObjectFactura.put("valor", 0);
                        }
                        jSONObjectFactura.put("img", propertiesAccess.SERVER + "img/logoConvenios/" + factura.getConveniosIdconvenios().getLogo());
                    } else {
                        jSONObjectFactura.put("id", factura.getIdfactura());
                        jSONObjectFactura.put("estado", factura.getEstadoIdestado().getNombre());
                        jSONObjectFactura.put("estadoid", factura.getEstadoIdestado().getIdestado());
                        if (factura.getFechaVencimiento() != null) {
                            jSONObjectFactura.put("fecha", factura.getFechaVencimiento().longValue());
                        } else {
                            jSONObjectFactura.put("fecha", 0);
                        }
                        jSONObjectFactura.put("convenio", factura.getConveniosIdconvenios().getNombre());
                        jSONObjectFactura.put("nombre", factura.getNombre());
                        jSONObjectFactura.put("referencia", factura.getReferencia() + "");
                        jSONObjectFactura.put("valor", 0);
                        jSONObjectFactura.put("img", propertiesAccess.SERVER + "img/logoConvenios/" + factura.getConveniosIdconvenios().getLogo());

                    }*/
                }
                arrayFacuturas.put(jSONObjectFactura);
            } catch (JSONException ex) {
                Logger.getLogger(CargarFacturasMovil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CargarFacturasMovil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
