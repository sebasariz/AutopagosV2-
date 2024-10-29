/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.facturasautopagos;

import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.FacturaCreator;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
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
import java.text.SimpleDateFormat;
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
public class DetalleFactura extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy-hhmm-a");

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

            String htmlFact = ""; 
            htmlFact = FacturaCreator.generarFactura(facturaAutopagos);

            File padre = new File(getServlet().getServletContext().getRealPath("") + File.separator + "soportes" + File.separator + facturaAutopagos.getConveniosIdconvenios().getIdconvenios() + File.separator);
            if (!padre.exists()) {
                padre.mkdirs();
            }
            File file = new File(getServlet().getServletContext().getRealPath("") + File.separator + "soportes" + File.separator + facturaAutopagos.getConveniosIdconvenios().getIdconvenios() + File.separator + "Factura" + new Date() + ".pdf");
            OutputStream outputStream = new FileOutputStream(file);
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            InputStream is = new ByteArrayInputStream(htmlFact.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            outputStream.close();

            InputStream in = new FileInputStream(file);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=comprobante" + simpleDateFormat.format(new Date()) + ".pdf");
            ServletOutputStream out = response.getOutputStream();
            byte[] outputByte = new byte[4096];
            //copy binary content to output stream
            while (in.read(outputByte, 0, 4096) != -1) {
                out.write(outputByte, 0, 4096);
            }
            in.close();
            out.flush();
            out.close();
            file.delete();
            padre.delete();
            return null;
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
