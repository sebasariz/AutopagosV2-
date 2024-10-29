/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Campo;
import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.MailSender;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.beans.support.XlsReader;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.FacturaTemplateJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class UpdateFacturasTemplateForm extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAccess = new PropertiesAccess();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
            Factura factura = (Factura) form;
            FormFile plantillaFile = factura.getFileFacturas();
            File facturas = null;

            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            FacturaTemplateJpaController facturaTemplateJpaController = new FacturaTemplateJpaController(manager);

            FacturaTemplate facturaTemplate = facturaTemplateJpaController.findFacturaTemplate(factura.getPlantilla());
            ArrayList<Campo> campos = new ArrayList<>(facturaTemplate.getCampoCollection());
            try {
                // get file from the bean
                String fname = plantillaFile.getFileName();
                if (fname.length() == 0) {
                    System.out.println("sin archivo");
                } else {
                    fname = fname.replace(" ", "");
                    fname = System.currentTimeMillis() + fname;
                    // save file in the app server  
                    facturas = new File(getServlet().getServletContext().getRealPath("") + "/tmp/" + fname);
                    FileOutputStream fos = new FileOutputStream(facturas);
                    fos.write(plantillaFile.getFileData());
                    fos.close();

                    XlsReader xlsReader = new XlsReader();
                    ArrayList<Factura> facturasArray = xlsReader.getFacturasCustom(facturas, campos);
                    String error = xlsReader.getError();
                    facturas.delete();
                    if (error != null) {
                        jSONObject.put("error", error);
                        response.setContentType("application/json");
                        PrintWriter printWriter = response.getWriter();
                        printWriter.print(jSONObject);
                        return null;
                    }
                    UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);

                    double valorTotal = 0;
                    for (Factura factura2 : facturasArray) {
                        factura2.setFacturaTemplateidfacturaTemplate(facturaTemplate);
                        factura2.setConveniosIdconvenios(usuario.getConveniosIdconvenios());

                        Factura facturaAnterior = facturaJpaController.getFacturaFromConvenioYReferencia(usuario.getConveniosIdconvenios().getIdconvenios(), factura2.getReferencia());

                        //separar los usuarios por coma
                        ArrayList<Usuario> usuarios = new ArrayList<>();
                        String[] emails = factura2.getEmail().split(",");
                        for (String email : emails) {
                            //inicio
                            if (email.length() > 0) {
                                Usuario usuarioEmail = new Usuario();
                                usuarioEmail.setEmail(email);
                                usuarioEmail = usuarioJpaController.emailExist(usuarioEmail);
                                if (usuarioEmail != null) {
                                    usuarios.add(usuarioEmail);
                                }
                            }
                        }
                        factura2.setUsuarioCollection(usuarios);
                        //fin
                        if (facturaAnterior != null) {
                            facturaAnterior.setValor(factura2.getValor());
                            facturaAnterior.setEstadoIdestado(new Estado(1));
                            facturaAnterior.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                            facturaAnterior.setCampos(factura2.getCampos());
                            facturaJpaController.edit(facturaAnterior);
                            factura2 = facturaAnterior;
                        } else {
                            //buscar al usuario si se encuentra registrado
                            factura2.setEstadoIdestado(new Estado(1));
                            factura2.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                            facturaJpaController.create(factura2);
                        }

                        File file = null;
                        if (factura2.getFacturaTemplateidfacturaTemplate() != null) {
                            String buf = factura2.getFacturaTemplateidfacturaTemplate().getHtml();
                            campos = new ArrayList<>(factura2.getCampos());

                            buf = buf.replace("$plantillaFile", propertiesAccess.SERVER + factura2.getConveniosIdconvenios().getLogo());
                            buf = buf.replace("$numero", factura2.getReferencia());
                            buf = buf.replace("$referencia", factura2.getReferencia());
                            buf = buf.replace("$fecha-factura", simpleDateFormat.format(new Date(factura2.getFechaCreacion().longValue())));
                            buf = buf.replace("$fecha-vencimiento", simpleDateFormat.format(new Date(factura2.getFechaVencimiento().longValue())));
                            buf = buf.replace("$total", factura2.getValor() + "");
                            buf = buf.replace("$email", factura2.getEmail() + "");
                            for (Campo campo : campos) {
                                buf = buf.replace(campo.getReferencia(), campo.getValor());
                            }
                            //creamos el PDF desde el HTML
                            try {
                                file = new File(getServlet().getServletContext().getRealPath("") + File.separator + "docs" + File.separator + "factura" + System.currentTimeMillis() + ".pdf");
                                OutputStream outputStream = new FileOutputStream(file);
                                com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.LETTER);
                                PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                                document.open();

                                System.out.println("buf: " + buf);
                                InputStream is = new ByteArrayInputStream(buf.getBytes());
                                XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, Charset.forName("UTF-8"));
                                document.close();
                                outputStream.close();
                                buf = null;
                            } catch (IOException e) {
                                e.printStackTrace();
                                jSONObject.put("error", e.getMessage());
                            }
                            Convenios convenios = usuario.getConveniosIdconvenios();
                            //calculamos comision
                            double valorComisionUsuario = 0;
                            if (convenios.getValorFijoUsuario() != null && convenios.getValorFijoUsuario() != 0) {
                                valorComisionUsuario += convenios.getValorFijoUsuario();
                            }
                            if (convenios.getValorVariableUsuario() != null && convenios.getValorVariableUsuario() != 0) {
                                valorComisionUsuario += convenios.getValorVariableUsuario() * factura2.getValor() / 100;
                            }
                            factura2.setValorComision(valorComisionUsuario + valorComisionUsuario * propertiesAccess.IVA);
                            jSONObject = MailSender.sendMailNotification(factura2, file, 1);

                            valorTotal += factura2.getValor();
                        }

                    }
                    //incrementamos los indicadores
                    ConfigJpaController configJpaController = new ConfigJpaController(manager);
                    Config config = configJpaController.findConfig(1);
                    if (usuario.getConveniosIdconvenios().getTipoConvenio() == 1) {
                        config.setSiparValorTotalCargado(config.getSiparValorTotalCargado() + valorTotal);
                        config.setSiparNumeroFacturasCargadas(config.getSiparNumeroFacturasCargadas() + facturasArray.size());
                    } else if (usuario.getConveniosIdconvenios().getTipoConvenio() == 2) {
                        //online
                        config.setOnlineCobrosRealizados(config.getOnlineCobrosRealizados() + facturasArray.size());
                    }
                    configJpaController.edit(config);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("ERROR-------" + e);
            }

            ArrayList<Factura> facturas1 = facturaJpaController.getFacturasFromConvenio(usuario.getConveniosIdconvenios().getIdconvenios());
            jSONObject.put("tabla", DataGrid.getFacturasGrid(facturas1));
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
