/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.MailSender;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class RememberInv extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAcces = new PropertiesAccess();

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
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        JSONObject jSONObject = new JSONObject();
        if (usuario != null) {
            int idFactura = Integer.parseInt(request.getParameter("id"));
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            Convenios convenios = usuario.getConveniosIdconvenios();
            Factura factura = facturaJpaController.findFactura(idFactura);

            double valor = factura.getValor();
            double valorComisionUsuario = 0;
            if (convenios.getValorFijoUsuario() != null && convenios.getValorFijoUsuario() != 0) {
                valorComisionUsuario += convenios.getValorFijoUsuario();
            }
            if (convenios.getValorVariableUsuario() != null && convenios.getValorVariableUsuario() != 0) {
                valorComisionUsuario += convenios.getValorVariableUsuario() * valor / 100;
            }
            factura.setValorComision(valorComisionUsuario + valorComisionUsuario * propertiesAcces.IVA);
            MailSender.sendMailNotification(factura, null,2);
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
