/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.acciones.pago.support.PagarFactura;
import com.iammagis.autopagos.jpa.beans.Banco;
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.TipoTransaccion;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.BancoJpaController;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;
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
public class PagarFacturasMovil extends org.apache.struts.action.Action {

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
        String arrayIds = request.getParameter("ids");
        int creditos = Integer.parseInt(request.getParameter("creditos"));
        int idbanco = Integer.parseInt(request.getParameter("banco"));
        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        if (usuario != null) {
            JSONArray arrayIDS = new JSONArray(arrayIds);
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            ArrayList<Transaccion> transaccions = new ArrayList<>();
            double total = 0;
            ArrayList<Factura> facturas = new ArrayList<>();
            for (int i = 0; i < arrayIDS.length(); i++) {
                JSONObject jSONObjectFactura = arrayIDS.getJSONObject(i);
                //validacion de valores y estado de facturas
                Factura factura = facturaJpaController.findFactura(jSONObjectFactura.getInt("id"));

                total += factura.getValor()
                        + factura.getConveniosIdconvenios().getValorFijoUsuario()
                        + factura.getConveniosIdconvenios().getValorVariableUsuario() * factura.getValor();
                if (factura.getEstadoIdestado().getIdestado() == 4) {
                    jSONObject.put("error", "La factura: " + factura.getReferencia() + " de la entidad: " + factura.getConveniosIdconvenios().getNombre() + " no se encuentra disponible para pagar.");
                    response.setContentType("application/json");
                    PrintWriter printWriter = response.getWriter();
                    printWriter.print(jSONObject);
                    return null;
                }
                facturas.add(factura);
            }

            //agregamos valor comision avvillas
            //aqui tengo que agregar la transaccion de la comision
            Transaccion transaccionComisionUsuario = new Transaccion();
            transaccionComisionUsuario.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
            transaccionComisionUsuario.setEstadoIdestado(new Estado(4));
            transaccionComisionUsuario.setIdTransaccion(UUID.randomUUID().toString());
            transaccionComisionUsuario.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(3));
            transaccionComisionUsuario.setUsuarioidUsuario(usuario);
            transaccionComisionUsuario.setValor(propertiesAccess.VALORCOMISIONAVVILLAS);
            transaccions.add(transaccionComisionUsuario);
 

            //creamos el comprobante
            ComprobanterecaudoPSE comprobanterecaudoPSE = new ComprobanterecaudoPSE();
            comprobanterecaudoPSE.setCreditos(creditos);
            comprobanterecaudoPSE.setEstadoIdestado(new Estado(4));
            comprobanterecaudoPSE.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
            comprobanterecaudoPSE.setDescripcion("Autopagos_multiple_id:" + usuario.getIdUsuario());
            comprobanterecaudoPSE.setValorTotal(total);
            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.create(comprobanterecaudoPSE);
            for (Transaccion transaccion : transaccions) {
                transaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanterecaudoPSE);
                transaccionJpaController.create(transaccion);
            }
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
            BancoJpaController bancoJpaController = new BancoJpaController(manager);
            Banco banco = bancoJpaController.findBanco(idbanco);

            //simulaciond e valor
//            comprobanterecaudoPSE.setValorTotal(1000D);
            String retorno = PagarFactura.pagarPSEAutopagos(comprobanterecaudoPSE, banco, null);
            JSONObject jSONObjectRetorno = new JSONObject(retorno);
            System.out.println("jSONObjectRetorno: " + jSONObjectRetorno);
            comprobanterecaudoPSE.setIdAvvillas(jSONObjectRetorno.getInt("pagoId"));
            comprobanterecaudoPSEJpaController.edit(comprobanterecaudoPSE);
            if (!jSONObjectRetorno.getBoolean("ok")) {
                jSONObject.put("error", "En el momento no podemos procesar tu pago, intenta mas tarde.");
            } else {
                for (Factura factura : facturas) {
                    factura.setEstadoIdestado(new Estado(4));
                    facturaJpaController.edit(factura);
                }
                jSONObject.put("url", jSONObjectRetorno.getString("dato"));
            }
        } else {
            jSONObject.put("error", "Error de session");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
