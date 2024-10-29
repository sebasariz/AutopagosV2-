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
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
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
import org.apache.struts.util.MessageResources;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class InscribirFacturaConvenio extends org.apache.struts.action.Action {

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
        JSONObject jSONObject = new JSONObject();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            String referencia = request.getParameter("numero");
            String email = request.getParameter("email");
            long fechaEmision = 0;
            if (request.getParameter("fechaEmision") != null && !request.getParameter("fechaEmision").isEmpty()) {
                fechaEmision = Long.parseLong(request.getParameter("fechaEmision"));
            }
            long fechaVencimiento = 0;
            if (request.getParameter("fechaVencimiento") != null && !request.getParameter("fechaVencimiento").isEmpty()) {
                fechaVencimiento = Long.parseLong(request.getParameter("fechaVencimiento"));
            }
            double valor = 0;
            if (request.getParameter("valor") != null && !request.getParameter("valor").isEmpty()) {
                valor = Double.parseDouble(request.getParameter("valor"));
            }
            
            Factura factura = new Factura();
            factura.setEmail(email);
            factura.setReferencia(referencia);
            factura.setValor(valor);
            factura.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
            factura.setFechaEmision(BigInteger.valueOf(fechaEmision));
            factura.setFechaVencimiento(BigInteger.valueOf(fechaVencimiento));
            factura.setConveniosIdconvenios(usuario.getConveniosIdconvenios());
            factura.setEstadoIdestado(new Estado(1));
            Factura facturaAnterior = facturaJpaController.getFacturaFromConvenioYReferencia(usuario.getConveniosIdconvenios().getIdconvenios(), factura.getReferencia());
            
            String[] emails = email.split(",");
            ArrayList<Usuario> usuarios = new ArrayList<>();
            for (String emailSeparator : emails) {
                Usuario usuarioEmail = new Usuario();
                usuarioEmail.setEmail(emailSeparator);
                usuarioEmail = usuarioJpaController.emailExist(usuarioEmail);
                if (usuarioEmail != null) {
                    usuarios.add(usuarioEmail);
                }
            }
            factura.setUsuarioCollection(usuarios);
            
            if (facturaAnterior != null) {
                facturaAnterior.setValor(factura.getValor());
                facturaAnterior.setEstadoIdestado(new Estado(1));
                facturaAnterior.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                facturaJpaController.edit(facturaAnterior);
            } else {
                //buscar al usuario si se encuentra registrado
                factura.setEstadoIdestado(new Estado(1));
                factura.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                try {
                    facturaJpaController.create(factura);
                } catch (Exception e) {
                    e.printStackTrace();
                    jSONObject.put("error", "Un correo electronico se encuentra duplicado");
                    response.setContentType("application/json");
                    PrintWriter printWriter = response.getWriter();
                    printWriter.print(jSONObject);
                    return null;
                }
            }
            
            Convenios convenios = usuario.getConveniosIdconvenios();
            //calculamos comision
            double valorComisionUsuario = 0;
            if (convenios.getValorFijoUsuario() != null && convenios.getValorFijoUsuario() != 0) {
                valorComisionUsuario += convenios.getValorFijoUsuario();
            }
            if (convenios.getValorVariableUsuario() != null && convenios.getValorVariableUsuario() != 0) {
                valorComisionUsuario += convenios.getValorVariableUsuario() * valor / 100;
            }
            factura.setValorComision(valorComisionUsuario + valorComisionUsuario * propertiesAccess.IVA);
            
            jSONObject = MailSender.sendMailNotification(factura, null,1);
            ArrayList<Factura> facturas = facturaJpaController.getFacturasFromConvenio(usuario.getConveniosIdconvenios().getIdconvenios());
            jSONObject.put("tabla", DataGrid.getFacturasGrid(facturas));

            //incrementamos los indicadores
            ConfigJpaController configJpaController = new ConfigJpaController(manager);
            Config config = configJpaController.findConfig(1);
            if (convenios.getTipoConvenio() == 1) {
                config.setSiparNumeroFacturasCargadas(config.getSiparNumeroFacturasCargadas() + 1);
                config.setSiparValorTotalCargado(config.getSiparValorTotalCargado() + valor);
            } else if (convenios.getTipoConvenio() == 2) {
                //online
                config.setOnlineCobrosRealizados(config.getOnlineCobrosRealizados() + 1);
            }
            configJpaController.edit(config);
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
