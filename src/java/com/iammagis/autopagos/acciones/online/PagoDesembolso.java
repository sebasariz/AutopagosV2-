/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.online;

import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class PagoDesembolso extends org.apache.struts.action.Action {

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
        JSONObject jSONObject = new JSONObject();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            int idTransaccion = Integer.parseInt(request.getParameter("idTransaccion"));
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            Transaccion transaccion = transaccionJpaController.findTransaccion(idTransaccion);
            FacturaAutopagos facturaAutopagos = transaccion.getFacturaAutopagosIdfacturaAutopagos();
            facturaAutopagos.setEstadoIdestado(new Estado(2));
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            facturaAutopagosJpaController.edit(facturaAutopagos);
            

            ArrayList<Transaccion> transaccions = new ArrayList<>(facturaAutopagos.getTransaccionCollection());
            for (Transaccion transaccionAll : transaccions) {
                transaccionAll.setEstadoIdestado(new Estado(2));
                transaccionJpaController.edit(transaccionAll);
            }

            ArrayList<Transaccion> transaccions1 = transaccionJpaController.getTransaccionesPagosOnline();
            jSONObject = DataGrid.getTransaccionesPagoOnline(transaccions1);
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
}
