/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class DeleteFacturaFromConvenio extends org.apache.struts.action.Action {

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

            JSONArray array = new JSONArray(request.getParameter("ids"));

            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            for (int i = 0; i < array.length(); i++) {
                Factura facturaAnterior = facturaJpaController.findFactura(array.getInt(i));
                TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
                if (facturaAnterior != null && facturaAnterior.getTransaccionCollection() != null) {
                    ArrayList<Transaccion> transaccions = new ArrayList<>(facturaAnterior.getTransaccionCollection());
                    for (Transaccion transaccion : transaccions) {
                        transaccion.setFacturaIdfactura(null);
                        transaccionJpaController.edit(transaccion);
                    }
                }
                facturaJpaController.destroy(array.getInt(i));
            }
            //eliminados los seleccionados
            ArrayList<Factura> facturas = new ArrayList<>();
            if (usuario.getConveniosIdconvenios() != null) {
                facturas = facturaJpaController.getFacturasFromConvenio(usuario.getConveniosIdconvenios().getIdconvenios());
            }

            jSONObject.put("tabla", DataGrid.getFacturasGrid(facturas));
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
