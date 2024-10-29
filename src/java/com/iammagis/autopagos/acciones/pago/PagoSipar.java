/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago;

import com.iammagis.autopagos.acciones.pago.support.PagarFactura;
import com.iammagis.autopagos.jpa.beans.Banco;
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.control.BancoJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
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
 * @author Usuario
 */
public class PagoSipar extends org.apache.struts.action.Action {

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

        
        FacturaAutopagos factura = (FacturaAutopagos) form;
        //aqui retornamos el url generado para el banco
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        BancoJpaController bancoJpaController = new BancoJpaController(manager);
        Banco banco = bancoJpaController.findBanco(factura.getIdBanco());
        FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
        factura = facturaAutopagosJpaController.findFacturaAutopagos(factura.getIdfacturaAutopagos());
        ComprobanterecaudoPSE comprobanterecaudoPSE = PagarFactura.generarTransaccionInicialAutopagos(factura);
        String retorno = PagarFactura.pagarPSEOnline(comprobanterecaudoPSE, banco);
        if (retorno.equals("error")) {
            return mapping.findForward("error");
        } else {
            response.sendRedirect(retorno);
        }
        return null;
    }
}
