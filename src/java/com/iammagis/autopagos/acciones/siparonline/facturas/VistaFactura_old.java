/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class VistaFactura_old extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private final byte[] singlePixelTransparentShimBytes = {71, 73, 70, 56, 57, 97, 1, 0, 1, 0, -128, 0, 0, -1, -1, -1, 0, 0, 0, 33, -7, 4, 1, 0, 0, 0, 0, 44, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 68, 1, 0, 59};

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

        System.out.println("llamando");
        String token = request.getParameter("token");
        int idFactura = Integer.parseInt(request.getParameter("id"));
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        Convenios convenios = conveniosJpaController.findByToken(token);
        JSONObject jSONObject = new JSONObject();
        if (convenios != null) {
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            Factura factura = facturaJpaController.findFactura(idFactura);
            if (factura.getConveniosIdconvenios().getIdconvenios() == convenios.getIdconvenios()) {
                //es el mismo
                factura.setVisto(Boolean.TRUE);
                facturaJpaController.edit(factura);
            }
            jSONObject.put("estado", "ok");
        } else {
            jSONObject.put("error", "Error de token");
        }
        response.setContentType("image/gif");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }

    public byte[] get1pixelTrackingImageAsBytes() {
        return singlePixelTransparentShimBytes;
    }
}
