/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
public class EnviarComprobanteMovil extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    static DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    static PropertiesAccess propertiesAccess = new PropertiesAccess();

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

        request.setCharacterEncoding("UTF-8");
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        String token = request.getParameter("token");
        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        if (usuario != null) {
            int idComprobante = Integer.parseInt(request.getParameter("idComprobante"));
            String email = request.getParameter("email");
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            Transaccion transaccion = transaccionJpaController.findTransaccion(idComprobante);
            //enviamos el correo
            ArrayList<String> correos = new ArrayList<>();
            correos.add(email);

            double valor = 0;
            if (transaccion.getValor() != null) {
                valor = transaccion.getValor();
            }
            int valorComision = 0;
            String templaString = CorreoZoho.getTemplate(CorreoZoho.pagoUsuarioTemplate);
            templaString = templaString.replace("$costo-trx", decimalFormat.format(valorComision));
            templaString = templaString.replace("$costo-total", decimalFormat.format(valor + valorComision));
            templaString = templaString.replace("$numero", "1");
            templaString = templaString.replace("$server", propertiesAccess.SERVER);
            templaString = templaString.replace("$direccion", propertiesAccess.DIRECCION);

            String tabla = "<tr>"
                                + "<td style=\"width:20%;\"> <p>" + transaccion.getFacturaIdfactura().getReferencia() + "</p></td>"
                                + "<td style=\"width:20%; text-align:center;\" > <img style=\"width:40px;\" src=\"" + propertiesAccess.SERVER + "img/logoConvenios/" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getLogo() + "\"></td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getFacturaIdfactura().getConveniosIdconvenios().getNombre() + "</span></p> </td>"
                                + "<td style=\"width:20%;\"><p><span style=\"color:#2196F3;\">" + transaccion.getEstadoIdestado().getNombre() + " </span></p> </td>"
                                + "<td style=\"width:20%; text-align:right;\" ><p> <span style=\"color:#AEEA00;\">$ " + decimalFormat.format(transaccion.getFacturaIdfactura().getValor()) + "</span></p></td>"
                                + "</tr>"
                                + "<tr>"
                                + "<td colspan=\"5\" style=\"padding:0;\">"
                                + "<p style=\"text-align:center; font-size:9px; border-bottom:1px solid #cccccc;\"></p>"
                                + "</td>"
                                + "</tr>";
            templaString = templaString.replace("$tabla", tabla);

            CorreoZoho correoZoho = new CorreoZoho("Transaccion APROBADA AUTOPAGOS", templaString, correos);
            correoZoho.start();

        } else {
            jSONObject.put("error", "Error de session");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
