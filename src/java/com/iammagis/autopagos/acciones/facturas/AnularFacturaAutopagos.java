/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.facturas;

import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.FacturaCreator;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletOutputStream;
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
public class AnularFacturaAutopagos extends org.apache.struts.action.Action {

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
            int id = Integer.parseInt(request.getParameter("id"));
            FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
            FacturaAutopagos facturaAutopagos = facturaAutopagosJpaController.findFacturaAutopagos(id);

            facturaAutopagos.setEstadoIdestado(new Estado(5));
            facturaAutopagosJpaController.edit(facturaAutopagos);
            ArrayList<FacturaAutopagos> facturaAutopagoses = new ArrayList<>(facturaAutopagosJpaController.findFacturaAutopagosEntities());
            jSONObject = DataGrid.getFacturasAutopagosData(facturaAutopagoses);
            request.setAttribute("facturas", jSONObject);
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
