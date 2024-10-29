/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago;

import com.iammagis.autopagos.jpa.beans.Banco;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.control.BancoJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.itextpdf.text.pdf.codec.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sebastianarizmendy
 */
public class LoadPagoUsuario extends org.apache.struts.action.Action {

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

        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        Convenios convenios = null;
        String referencia = "";
        String valor = "0";
        if (request.getParameter("token") != null) {
            //datos del convenio
            String token = request.getParameter("token");
            convenios = conveniosJpaController.findByToken(token);
            if (convenios.getValorVariable() != null) {
                request.setAttribute("variable", convenios.getValorVariable());
            } else {
                request.setAttribute("variable", "");
            }
            request.setAttribute("comfija", convenios.getValorFijoUsuario());
            request.setAttribute("comvar", convenios.getValorVariableUsuario());
            //aqui si factura existe o no
            if (request.getParameter("idfactura") != null) {

                //datos de la factura
                int idFactura = Integer.parseInt(request.getParameter("idfactura"));
                FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
                Factura factura = facturaJpaController.findFactura(idFactura);
                if (factura == null) {
                    return mapping.findForward("error");
                }
                if (factura.getEstadoIdestado().getIdestado() == 4) {
                    //no se puede efectura el pago por que se encuentra en  
                    return mapping.findForward("comprobando");
                } else if (factura.getEstadoIdestado().getIdestado() == 2) {
                    return mapping.findForward("pagada");
                } else {
                    double valorComision = 0;
                    valorComision = convenios.getValorFijoUsuario() + convenios.getValorVariableUsuario() * factura.getValor();
                    System.out.println("valor de la comision: " + valorComision);
                    session.setAttribute("convenio", convenios);
                    request.setAttribute("referencia", factura.getReferencia());
                    request.setAttribute("idfactura", idFactura);
                    request.setAttribute("valor", factura.getValor());
                    request.setAttribute("comision", valorComision);
                    request.setAttribute("email", factura.getEmail());
                    double total = factura.getValor() + valorComision;
                    request.setAttribute("total", total);
                }
            } else {
                //si no tiene idfactura 
                referencia = request.getParameter("referencia");
                valor = request.getParameter("valor");
                String email = request.getParameter("email");
                double valorComision = 0;
                valorComision = convenios.getValorFijoUsuario() + convenios.getValorVariableUsuario() * Double.parseDouble(valor);
                request.setAttribute("referencia", referencia);
                request.setAttribute("idfactura", 0);
                request.setAttribute("valor", valor);
                request.setAttribute("comision", valorComision);
                request.setAttribute("email", email);
                double total = Double.parseDouble(valor) + valorComision;
                request.setAttribute("total", total);
            }
        } else if (request.getParameter("enc") != null) {
            //si no tiene idfactura
            byte[] decore = Base64.decode(request.getParameter("enc"));
            String decodingString = new String(decore);

            Map map = splitQuery(decodingString);
            referencia = map.get("referencia").toString();
            valor = map.get("valor").toString();

            //datos del convenio
            convenios = conveniosJpaController.findByToken(map.get("token").toString());
            if (convenios.getValorVariable() != null) {
                request.setAttribute("variable", convenios.getValorVariable());
            } else {
                request.setAttribute("variable", "");
            }
            request.setAttribute("comfija", convenios.getValorFijoUsuario());
            request.setAttribute("comvar", convenios.getValorVariableUsuario());
            double valorComision = 0;
            valorComision = convenios.getValorFijoUsuario() + convenios.getValorVariableUsuario() * Double.parseDouble(valor);

            request.setAttribute("referencia", referencia);
            request.setAttribute("idfactura", 0);
            request.setAttribute("valor", valor);
            request.setAttribute("comision", valorComision);
            double total = Double.parseDouble(valor) + valorComision;
            request.setAttribute("total", total);
        }

        BancoJpaController bancoJpaController = new BancoJpaController(manager);
        ArrayList<Banco> bancos = new ArrayList<>(bancoJpaController.findBancoEntities());

        session.setAttribute("bancos", bancos);
        session.setAttribute("convenio", convenios);
        return mapping.findForward(SUCCESS);
    }

    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
