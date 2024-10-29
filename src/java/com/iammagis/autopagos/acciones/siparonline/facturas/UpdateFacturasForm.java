/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.MailSender;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.beans.support.XlsReader;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class UpdateFacturasForm extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAccess = new PropertiesAccess();

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
            System.out.println("subiendo subiendo");

            Factura factura = (Factura) form;
            FormFile logo = factura.getFileFacturas();
            File facturas = null;
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            try {
                // get file from the bean
                String fname = logo.getFileName();
                if (fname.length() == 0) {
                    System.out.println("sin archivo");
                } else {
                    fname = fname.replace(" ", "");
                    fname = System.currentTimeMillis() + fname;
                    // save file in the app server  
                    facturas = new File(getServlet().getServletContext().getRealPath("") + "/tmp/" + fname);
                    FileOutputStream fos = new FileOutputStream(facturas);
                    fos.write(logo.getFileData());
                    fos.close(); 
                    XlsReader xlsReader = new XlsReader(); 
                    ArrayList<Factura> facturasArray = xlsReader.getFacturas(facturas);
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

                        factura2.setConveniosIdconvenios(usuario.getConveniosIdconvenios());
                        Factura facturaAnterior = facturaJpaController.getFacturaFromConvenioYReferencia(usuario.getConveniosIdconvenios().getIdconvenios(), factura2.getReferencia());

                        //los usuarios
                        String[] emails = factura2.getEmail().split(",");
                        ArrayList<Usuario> usuarios = new ArrayList<>();
                        for (String email : emails) {
                            Usuario usuarioEmail = new Usuario();
                            usuarioEmail.setEmail(email);
                            usuarioEmail = usuarioJpaController.emailExist(usuarioEmail);
                            if (usuarioEmail != null) {
                                usuarios.add(usuarioEmail);
                            }
                        }
                        factura2.setUsuarioCollection(usuarios);

                        if (facturaAnterior != null) {
                            facturaAnterior.setValor(factura2.getValor());
                            facturaAnterior.setEstadoIdestado(new Estado(1));
                            facturaAnterior.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                            facturaJpaController.edit(facturaAnterior);
                        } else {
                            //buscar al usuario si se encuentra registrado
                            factura2.setEstadoIdestado(new Estado(1));
                            factura2.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                            facturaJpaController.create(factura2);
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
                        jSONObject = MailSender.sendMailNotification(factura2, null, 1);

                        valorTotal += factura2.getValor();

                    } 
                    //incrementamos los indicadores
                    ConfigJpaController configJpaController = new ConfigJpaController(manager);
                    Config config = configJpaController.findConfig(1);
                    if (usuario.getConveniosIdconvenios().getTipoConvenio() == 1) {
                        config.setSiparNumeroFacturasCargadas(config.getSiparNumeroFacturasCargadas() + facturasArray.size());
                        config.setSiparValorTotalCargado(config.getSiparValorTotalCargado() + valorTotal);
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
        }

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
