/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago;

import com.iammagis.autopagos.jpa.beans.Banco;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.BancoJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import java.util.ArrayList;
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
public class LoadPagoSipar extends org.apache.struts.action.Action {

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

        if (request.getParameter("id") != null) {
            int idFactura = Integer.parseInt(request.getParameter("id"));
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            FacturaAutopagos facturaAutopagos = facturaAutopagosJpaController.findFacturaAutopagos(idFactura);
            if (facturaAutopagos.getEstadoIdestado().getIdestado() == 4) {
                //no se puede efectura el pago por que se encuentra en  
                return mapping.findForward("comprobando");
            } else if (facturaAutopagos.getEstadoIdestado().getIdestado() == 2) {
                return mapping.findForward("pagada");
            } else {
//                System.out.println("generar el pago");
                String email = "";
                for (Usuario usuario : facturaAutopagos.getConveniosIdconvenios().getUsuarioCollection()) {
                    email += usuario.getEmail() + ",";
                } 
                double total = facturaAutopagos.getValor() + facturaAutopagos.getIva();
                double valorComision = 0;
                session.setAttribute("convenio", facturaAutopagos.getConveniosIdconvenios());
                request.setAttribute("referencia", facturaAutopagos.getIdfacturaAutopagos());
                request.setAttribute("idfactura", idFactura);
                request.setAttribute("valor", total);
                request.setAttribute("comision", valorComision);
                request.setAttribute("email", email);
                double totalComision = total + valorComision;
                request.setAttribute("total", totalComision);
            }
        }
        BancoJpaController bancoJpaController = new BancoJpaController(manager);
        ArrayList<Banco> bancos = new ArrayList<>(bancoJpaController.findBancoEntities());
        session.setAttribute("bancos", bancos); 
        return mapping.findForward(SUCCESS);
    }
}
