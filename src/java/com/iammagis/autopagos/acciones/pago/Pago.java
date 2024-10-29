/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago;

import com.iammagis.autopagos.acciones.pago.support.PagarFactura;
import com.iammagis.autopagos.jpa.beans.Banco;
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.support.TimerTaskSipar;
import com.iammagis.autopagos.jpa.beans.support.VariablesSession;
import com.iammagis.autopagos.jpa.control.BancoJpaController;
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
public class Pago extends org.apache.struts.action.Action {

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
        Factura factura = (Factura) form;
        Convenios convenios = (Convenios) session.getAttribute("convenio");
        //aqui retornamos el url generado para el banco
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        BancoJpaController bancoJpaController = new BancoJpaController(manager);
        System.out.println("factura.getIdBanco(): " + factura.getIdBanco());
        Banco banco = bancoJpaController.findBanco(factura.getIdBanco());
        factura.setConveniosIdconvenios(convenios);
        ComprobanterecaudoPSE comprobanterecaudoPSE = PagarFactura.generarTransaccionInicial(factura);
        
        if (null == convenios.getTipoConvenio()) {
            return mapping.findForward("error");
        } else {
            switch (convenios.getTipoConvenio()) {
                case 1: {
                    //sipar
                    System.out.println("pago sipar");
                    String retorno = PagarFactura.pagarPSESipar(comprobanterecaudoPSE, banco);
                    if (retorno.equals("error")) {
                        return mapping.findForward("error");
                    } else {
                        response.sendRedirect(retorno);
                    }
                    break;
                }
                case 2: {
                    //online
                    System.out.println("entrando por online: " + banco.getIdBanco());
                    String retorno = PagarFactura.pagarPSEOnline(comprobanterecaudoPSE, banco);
                    response.sendRedirect(retorno);
                    break;
                }
                default:
                    return mapping.findForward("error");
            }
        }
        if (comprobanterecaudoPSE != null && VariablesSession.timer != null) {
            TimerTaskSipar timerTaskSipar = new TimerTaskSipar(comprobanterecaudoPSE); 
            VariablesSession.timer.schedule(timerTaskSipar, 600000);
        }
        return mapping.findForward("error");
    }
}
