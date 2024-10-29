/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.sesion.online;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.TipoTransaccion;
import com.iammagis.autopagos.jpa.beans.TipoUsuario;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.TipoUsuarioJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
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
public class SolicitudPago extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAccess = new PropertiesAccess();
    DecimalFormat decimalFormat = new DecimalFormat("###.00");

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
            double valorRetiro = propertiesAccess.VALORRETIRO;
            double ivaRetiro = valorRetiro * propertiesAccess.IVA;
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");

            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(usuario.getConveniosIdconvenios().getIdconvenios());
            Double valor = Double.parseDouble(request.getParameter("valor"));
            double disponible = convenios.getValorRecaudado() - convenios.getValorComisiones() - (valorRetiro + ivaRetiro);

            if (disponible < valor) {
                jSONObject.put("error", "El monto supera el disponible mas el valor de la transaccion. "
                        + "Recuerde que cada transaccion tiene un valor de: $" + propertiesAccess.VALORRETIRO + " más IVA");
            } else {

                //regla de tres para calcular la comision actual
                convenios = conveniosJpaController.findConvenios(convenios.getIdconvenios());
                double valorRecaudado = convenios.getValorRecaudado();
                double valorComisiones = convenios.getValorComisiones();
                double comisionPorRetiroConIVa = valorComisiones * valor / valorRecaudado;

                //VALOR
                double comisionPorRetiro = comisionPorRetiroConIVa / (1 + propertiesAccess.IVA);
                double IvaComisionPorRetiro = comisionPorRetiroConIVa - comisionPorRetiro;
                double nuevoValorRecaudado = valorRecaudado - valor - comisionPorRetiroConIVa - valorRetiro - ivaRetiro;
                convenios.setValorRecaudado(nuevoValorRecaudado);
                convenios.setValorComisiones(valorComisiones - comisionPorRetiroConIVa);
                conveniosJpaController.edit(convenios);
                jSONObject.put("recaudado", convenios.getValorRecaudado());
                jSONObject.put("disponible", convenios.getValorRecaudado() - convenios.getValorComisiones());

                //generamos la factura de autopagos
                FacturaAutopagos facturaAutopagos = new FacturaAutopagos();
                facturaAutopagos.setConveniosIdconvenios(convenios);
                facturaAutopagos.setEstadoIdestado(new Estado(1));
                facturaAutopagos.setFechaEmision(BigInteger.valueOf(System.currentTimeMillis()));
                facturaAutopagos.setIva(ivaRetiro + IvaComisionPorRetiro);
                facturaAutopagos.setNumeroTransaccionesConvenio(1);
                facturaAutopagos.setValor(valorRetiro + ivaRetiro + comisionPorRetiroConIVa);

                FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
                facturaAutopagos = facturaAutopagosJpaController.create(facturaAutopagos);
                TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);

                //generar factura por la comision que se cobra
                Transaccion transaccionComisionTransaccion = new Transaccion();
                transaccionComisionTransaccion.setEstadoIdestado(new Estado(1));
                transaccionComisionTransaccion.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
                transaccionComisionTransaccion.setIdTransaccion(UUID.randomUUID().toString());
                transaccionComisionTransaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                transaccionComisionTransaccion.setValor(comisionPorRetiro * -1);
                transaccionComisionTransaccion.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(1));
                transaccionJpaController.create(transaccionComisionTransaccion);

                //transaccion iva comision transaccion
                Transaccion transaccionIVAComisionTransaccion = new Transaccion();
                transaccionIVAComisionTransaccion.setEstadoIdestado(new Estado(1));
                transaccionIVAComisionTransaccion.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
                transaccionIVAComisionTransaccion.setIdTransaccion(UUID.randomUUID().toString());
                transaccionIVAComisionTransaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                transaccionIVAComisionTransaccion.setValor(IvaComisionPorRetiro * -1);
                transaccionIVAComisionTransaccion.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(2));
                transaccionJpaController.create(transaccionIVAComisionTransaccion);

                //transaccion desembolso
                Transaccion transaccionDesembolso = new Transaccion();
                transaccionDesembolso.setIdTransaccion(UUID.randomUUID().toString());
                transaccionDesembolso.setEstadoIdestado(new Estado(1));
                transaccionDesembolso.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                transaccionDesembolso.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
                transaccionDesembolso.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(5));
                transaccionDesembolso.setValor(valor * -1);
                transaccionJpaController.create(transaccionDesembolso);

                //transaccion costo transaccion
                Transaccion transaccionCostoTransaccion = new Transaccion();
                transaccionCostoTransaccion.setEstadoIdestado(new Estado(1));
                transaccionCostoTransaccion.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
                transaccionCostoTransaccion.setIdTransaccion(UUID.randomUUID().toString());
                transaccionCostoTransaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                transaccionCostoTransaccion.setValor(valorRetiro * -1);
                transaccionCostoTransaccion.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(8));
                transaccionJpaController.create(transaccionCostoTransaccion);

                //transaccion iva costo transaccion
                Transaccion transaccionIVACostoTransaccion = new Transaccion();
                transaccionIVACostoTransaccion.setEstadoIdestado(new Estado(1));
                transaccionIVACostoTransaccion.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
                transaccionIVACostoTransaccion.setIdTransaccion(UUID.randomUUID().toString());
                transaccionIVACostoTransaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                transaccionIVACostoTransaccion.setValor(ivaRetiro * -1);
                transaccionIVACostoTransaccion.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(9));
                transaccionJpaController.create(transaccionIVACostoTransaccion);

                //envio correo usuario solicitud
                ArrayList<String> strings = new ArrayList<String>();
                strings.add(usuario.getEmail());
                String template = CorreoZoho.getTemplate(CorreoZoho.solicitudDesembolsoUsuarioTemplate);
                template = template.replace("$valor", valor + "");
                template = template.replace("$costo", valorRetiro + "");
                template = template.replace("$iva", (valorRetiro * propertiesAccess.IVA) + "");
                CorreoZoho correoZoho = new CorreoZoho("Solicitud de transferencia de fondos", template, strings);
                correoZoho.start();
                //ENVIAR CORREO CONVENIO

                TipoUsuarioJpaController tipoUsuarioJpaController = new TipoUsuarioJpaController(manager);
                TipoUsuario tipoUsuario = tipoUsuarioJpaController.findTipoUsuario(1);
                ArrayList<Usuario> usuarios = new ArrayList<Usuario>(tipoUsuario.getUsuarioCollection());
                ArrayList<String> stringsConvenio = new ArrayList<String>();
                for (Usuario usuario1 : usuarios) {
                    stringsConvenio.add(usuario1.getEmail());
                }
                //calculamos comision 
                String templateConvenio = CorreoZoho.getTemplate(CorreoZoho.solicitudDesembolsoTemplate);

                templateConvenio = templateConvenio.replace("$nombre-convenio", convenios.getNombre());
                templateConvenio = templateConvenio.replace("$nitComercio", convenios.getNit());
                templateConvenio = templateConvenio.replace("$fecha", new Date().toString());

                templateConvenio = templateConvenio.replace("$banco", convenios.getBanco());
                templateConvenio = templateConvenio.replace("$tipo-cuenta", convenios.getTipoCuentaidtipoCuenta().getNombre());
                templateConvenio = templateConvenio.replace("$titular", convenios.getTitularCuenta());
                templateConvenio = templateConvenio.replace("$n-cuenta", convenios.getNumeroCuenta());
                templateConvenio = templateConvenio.replace("$total-desembolso", decimalFormat.format(valor) + "");
//                templateConvenio = templateConvenio.replace("$total-transaccion", valorRetiro + "");
//                templateConvenio = templateConvenio.replace("$total-iva-transaccion", valorRetiro * propertiesAccess.IVA + "");
                CorreoZoho correoZohoConvenio = new CorreoZoho("Solicitud de transferencia de fondos", templateConvenio, strings);
                correoZohoConvenio.start();

                long fechaInicial = Long.parseLong(request.getParameter("fechaInicial"));
                long fechaFinal = Long.parseLong(request.getParameter("fechaFinal"));
                ArrayList<Transaccion> transacciones = transaccionJpaController.getTransaccionesOnlineByConvenio(fechaInicial, fechaFinal, convenios.getIdconvenios());
                // System.out.println("transacciones: " + transacciones.size());
                JSONObject jSONObjectGrid = DataGrid.getTransaccionesDataConvenio(transacciones);
                jSONObject.put("grid", jSONObjectGrid);

                //buscamos los datos de la grafica 
                JSONObject jSONObjectGrafica = DataGrid.getGraphicTransactionsOnline(transacciones, fechaInicial, fechaFinal);
                jSONObject.put("grafica", jSONObjectGrafica);

                jSONObject.put("msg", "La solicitud de pago se ha generado.");
            }
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
