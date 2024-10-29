/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Plan;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class FacturaCreator {

    static SimpleDateFormat simpleDateFormatFile = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    static SimpleDateFormat simpleDateFormaHour = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
    static NumberFormat numberFormat = new DecimalFormat("###,##0.00");
    static PropertiesAccess propertiesAcces = new PropertiesAccess();
    private static String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 5.01 Transitional//ES\" \"http://www.w3.org/TR/html4/loose.dtd\">"
            + "<html>"
            + "<head>"
            + "</head><body>";
    private static String footer = "</body></html>";

    public static String generarFactura(FacturaAutopagos factura) throws JSONException {
        String facturaHtml = "";

        Calendar calendarVencimiento = Calendar.getInstance();
        calendarVencimiento.setTime(new Date(factura.getFechaEmision().longValue()));
        calendarVencimiento.add(Calendar.DAY_OF_YEAR, 8);
        facturaHtml = "<html>"
                + "<head>"
                + ""
                + ""
                + "</head>"
                + "    <body>"
                + "         "
                + "            <table  style=\"width:750px;margin:0 auto; border:1px solid #000000; padding:5px;\" cellspacing=\"0\">"
                + "                <tr>"
                + "                    <td style=\"width:130px;\"><img style=\"width\" src=\"" + propertiesAcces.SERVER + "img/template/logo-banner-principal-2.png\"></img><span style=\"font-size:12px;\"></span>"
                + "                    </td>"
                + "                    <td style=\"width:450px;text-align:center;font-size:14px;\"><strong>AUTOPAGOS.CO SAS</strong>"
                + "                        <p style=\"margin:0px;font-size:10px;\"><strong>NIT: " + propertiesAcces.NIT + "</strong></p>"
                + "                        <p style=\"margin:0px;font-size:10px;\">" + propertiesAcces.DIRECCION + "</p>"
                + "                        <p style=\"margin:0px;font-size:10px;\">TEL: " + propertiesAcces.TELEFONO + "</p>"
                + "                        <p style=\"margin:0px;font-size:13px;\"><a href=\"" + propertiesAcces.SERVER + "\" target=\"_blank\">www.autopagos.co</a></p> "
                + "                        <p style=\"margin:0px; font-size:9px;\">- RÉGIMEN COMÚN -</p> "
                + "                    </td>"
                + "                    <td style=\"width:150px;\">"
                + "                        <table style=\"width:100%;\" cellspacing=\"0\">"
                + "                            <tr>"
                + "                                <td>"
                + "                                    <p style=\"text-align:center; font-size: 14px; color:#D50000;\"> <strong>FACTURA</strong></p>"
                + "                                </td>"
                + "                            </tr>"
                + "                            <tr style=\"height:35px;\">"
                + "                                <td style=\"border:1px solid #000000;\">"
                + "                                    <p style=\"text-align:center; font-size: 12px;\"> <strong>" + factura.getIdfacturaAutopagos() + "</strong></p>"
                + "                                </td>"
                + "                            </tr>"
                + "                        </table>"
                + "                    </td>"
                + "                </tr>"
                + "                "
                + "                <tr>"
                + "                    <td colspan=\"3\">"
                + "                        <table style=\"width:100%;  \" cellspacing=\"0\">"
                + "                            <tr style=\"height:35px;\">"
                + "                                <td style=\"width:60%; border:1px solid #000000;\">"
                + "                                    <p style=\"font-size:10px; text-align:center;\">" + propertiesAcces.RESOLUCION + "</p>"
                + "                                </td>"
                + "                                <td style=\"width:30%;\">"
                + "                                    <table cellspacing=\"0\" style=\" font-size:12px; height:35px; width:100%;\">"
                + "                                        <tr>"
                + "                                            <td style=\"border:1px solid #000000; padding:3px;\">"
                + "                                                <p><strong>Fecha Emisión:</strong></p>"
                + "                                            </td>"
                + "                                            <td style=\"border:1px solid #000000; padding:3px;\">"
                + "                                                <p>" + simpleDateFormat.format(new Date(factura.getFechaEmision().longValue())) + "</p>"
                + "                                            </td>"
                + "                                        </tr>"
                + "                                        <tr>"
                + "                                            <td style=\"border:1px solid #000000; padding:3px;\">"
                + "                                                <p><strong>Fecha vencimiento:</strong></p>"
                + "                                            </td>"
                + "                                            <td style=\"border:1px solid #000000; padding:3px;\">"
                + "                                                <p>" + simpleDateFormat.format(calendarVencimiento.getTime()) + "</p>"
                + "                                            </td>"
                + "                                        </tr>"
                + ""
                + "                                    </table>"
                + "                                </td>"
                + "                            </tr>"
                + "                        </table>"
                + "                    </td>"
                + "                    "
                + "                </tr>"
                + "                <tr style=\"height:50px;\">"
                + "                    <td colspan=\"3\">"
                + "                        <table style=\"width:100%;\" cellspacing=\"0\">"
                + "                                <tr style=\"height:25px;\"><td></td></tr>"
                + "                                <tr ><td style=\"background:#F2F2F2; width:100%; text-align:center;font-size:14px; padding:5px;\"><p><strong>DATOS  DEL CLIENTE</strong></p> </td></tr>"
                + "                            </table>"
                + "                    </td>"
                + "                </tr>"
                + "                <tr>"
                + "                    <td colspan=\"3\">"
                + "                        <table style=\"width:650px; font-size:13px;\" cellspacing=\"0\">"
                + "                        <tr style=\"background:#ffffff;height:15px\">"
                + "                            <td colspan=\"2\"> </td>"
                + "                        </tr>"
                + "                            <tr>"
                + "                                <td style=\"padding:5px;\"><strong>Cliente:</strong></td>"
                + "                                <td style=\"\">" + factura.getConveniosIdconvenios().getTelefono() + "</td>"
                + "                                <td style=\"padding:5px;\"><strong>NIT:</strong> </td>"
                + "                                <td style=\"padding:5px;\">" + factura.getConveniosIdconvenios().getNit() + "</td>"
                + "                            </tr>"
                + "                            <tr>"
                + "                                <td style=\"padding:5px; font-size:12px;\"><strong>DIRECCIÓN:</strong></td>"
                + "                                <td style=\"font-size:12px;\">" + factura.getConveniosIdconvenios().getDireccion() + "</td>"
                + "                                 <td style=\"padding:5px; font-size:12px;\"><strong>TELÉFONOS:</strong></td>"
                + "                                <td style=\"font-size:12px;\" colspan=\"3\">" + factura.getConveniosIdconvenios().getTelefono() + "</td>"
                + "                            </tr>"
                + "                        </table>"
                + "                    </td>"
                + "                    "
                + "                </tr>"
                + "                <tr style=\"background:#ffffff;height:5px\">"
                + "                    <td colspan=\"2\"> </td>      "
                + "                </tr>"
                + "            </table>"
                + "            "
                + ""
                + "            <table style=\"width:750px;margin:0 auto; font-size:12px;\" cellspacing=\"0\">"
                + "                <tr>"
                + "                    <td colspan=\"2\" style=\"padding:0;\">"
                + "                        <table style=\"width: 650px;margin:0 auto; \" cellspacing=\"2\">"
                + "                        <tr style=\"background:#ffffff;height:5px\">"
                + "                            <td colspan=\"2\"> </td>      "
                + "                        </tr>"
                + "                            <tr style=\"height:30px;background: #f2f2f2;\">"
                + "                                <td style=\"padding:2px; font-size:12px; text-align:center;width: 350px;\"><strong>DESCRIPCIÓN</strong></td>"
                + "                                <td style=\"padding:2px; font-size:12px; text-align:center;width: 100px;\"><strong>VALOR TOTAL</strong></td>           "
                + "                            </tr>";
        if (factura.getTipo() == 1) {
            //factura de plan

            //pago de factura 
            facturaHtml += " <tr style=\"background:#ffffff;height:50px; \">"
                    + " <td style=\"padding:2px; font-size:12px; width: 350px;\">" + factura.getConveniosIdconvenios().getPlanIdplan().getNombre() + "</td>"
                    + " <td style=\"padding:2px; font-size:12px; text-align:center; width: 100px;\">$ " + numberFormat.format(factura.getValor()) + "</td>     "
                    + " </tr>";

            if (factura.getNumeroTransaccionesConvenio() > factura.getPlanIdplan().getNumeroTransacciones()) {
                int numeroAdicionales = factura.getNumeroTransaccionesConvenio() - factura.getConveniosIdconvenios().getPlanIdplan().getNumeroTransacciones();
                facturaHtml += " <tr style=\"background:#ffffff;height:50px; \">"
                        + " <td style=\"padding:2px; font-size:12px; width: 350px;\"> Adicionales: " + numeroAdicionales + "</td>"
                        + " <td style=\"padding:2px; font-size:12px; text-align:center; width: 100px;\">$ " + numberFormat.format(numeroAdicionales * factura.getPlanIdplan().getValorExtraTransaccion()) + "</td>     "
                        + " </tr>";
            }

        } else if (factura.getTipo() == 2) {
            JSONArray jSONArray = new JSONArray(factura.getItems());
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);

                facturaHtml += " <tr style=\"background:#ffffff;height:50px; \">"
                        + " <td style=\"padding:2px; font-size:12px; width: 350px;\">" + jSONObject.getString("descripcion") + "</td>"
                        + " <td style=\"padding:2px; font-size:12px; text-align:center; width: 100px;\">$ " + numberFormat.format(jSONObject.getDouble("valor")) + "</td>     "
                        + " </tr>";
            }
        }

        facturaHtml += "                        </table>"
                + "                    </td>"
                + "                </tr>"
                + "            </table>"
                + "            "
                + "            <table style=\"width:750px;margin:0 auto;\" cellspacing=\"0\">"
                + "                <tr>"
                + "                    <td style=\"border: 1px solid #000000;width:400px; font-size:12px;padding:5px;\"><strong>OBSERVACIONES:</strong> Recuerde que el pago de esta factura debe ser realizado a"
                + "través del botón de pagos emitido y notificado por correo electrónico</td>"
                + "                    <td style=\"border: 1px solid #000000;\">"
                + "                        <table style=\" width:250px;\" >"
                + "                            <tr>"
                + "                                <td style=\"font-size:12px;\"><strong>SUBTOTAL</strong></td>"
                + "                                <td style=\"text-align:right; font-size:12px;\">$ " + numberFormat.format(factura.getValor()) + "</td>"
                + "                            </tr>"
                + "                            <tr>"
                + "                                <td style=\"font-size:12px;\"><strong>IVA " + (propertiesAcces.IVA * 100) + "%</strong></td>"
                + "                                <td style=\"text-align:right;font-size:12px;\">$ " + numberFormat.format(factura.getIva()) + "</td>"
                + "                            </tr>"
                + "                                "
                + "                        </table>"
                + "                    </td>"
                + "                </tr>"
                + "                <tr style=\"background:#f2f2f2;padding:5px;\">"
                + "                    <td style=\"font-size:12px; border: 1px solid #000000; \"></td>"
                + "                    <td style=\"font-size:12px; border: 1px solid #000000; \">"
                + "                        <table>"
                + "                            <tr>"
                + "                                <td style=\"font-size:12px;\"><strong>VALOR TOTAL</strong></td>"
                + "                                <td style=\"text-align:right; font-size:12px;\">$ " + numberFormat.format(factura.getValor() + factura.getIva()) + "</td>"
                + "                            </tr>"
                + "                        </table>"
                + "                    </td>"
                + "                </tr>"
                + "            </table>"
                + "            <table style=\"width:650px;margin:10px auto; font-size:12px;\" cellspacing=\"0\">"
                + "                <tr style=\"background:#ffffff;height:20px\">"
                + "                    <td colspan=\"2\"> </td>      "
                + "                </tr>"
                + "                <tr>"
                + "                    <td colspan=\"2\">"
                + "                        <p>1. Esta FACTURA DE VENTA se asimila en todos sus efectos a una LETRA DE CAMBIO según el Artículo 774 del código de"
                + "                        comercio.</p>"
                + "                    </td>"
                + "                </tr>"
                + "                 <tr style=\"background:#ffffff;height:40px\">"
                + "                    <td colspan=\"2\"> </td>      "
                + "                </tr>"
                + "                <tr>"
                + "                    <td colspan=\"2\">"
                + "                        <table style=\"width:650px; text-align:center;margin:0 auto;\">"
                + "                            <tr>"
                + "                                <td style=\"width:325px;border:1px solid #000000;padding:10px;\">"
                + "                                    <p style=\"font-size:12px;\"><strong>EMITE:</strong></p>"
                + "                                    <p style=\"font-size:14px;\">AUTOPAGOS.CO SAS</p>"
                + "                                </td>"
                + "                                <td style=\"width:325px; border:1px solid #000000; padding:10px;\">"
                + "                                    <p style=\"font-size:12px;\"><strong>RECIBE:</strong></p>"
                + "                                    <p style=\"font-size:14px;\">" + factura.getConveniosIdconvenios().getNombre() + "</p>"
                + "                                </td>"
                + "                            </tr>"
                + "                             "
                + "                        </table>"
                + "                    </td>"
                + "                    "
                + "                </tr>"
                + "            </table>"
                + "            <table style=\"width:750px;margin:10px auto;text-align:center;font-size:10px\" cellspacing=\"3\">"
                + "                <tr><td style=\"color:#3498db\">Copia digital de la Factura Oficial - Autopagos.co SAS</td></tr> "
                + "            </table> ";
        if (factura.getTipo() == 1) {
            //factura de plan 
            facturaHtml += "<table style=\"width:600px; margin:0 auto;\" cellspacing=\"1\">"
                    + "                <tr style=\"height:30px;background: #f2f2f2;\">"
                    + "                    <td style=\"text-align:center; width:100%;font-size:13px;\" colspan=\"4\"><strong>DETALLE TRANSACCIONAL - SIPAR -</strong></td>"
                    + "                </tr>"
                    + "                <tr style=\"height:10px;\">"
                    + "                    <td></td>"
                    + "                </tr>"
                    + "                <tr style=\"height:30px;background: #f2f2f2;\">"
                    + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 150px;\"><strong>FECHA Y HORA</strong></td>"
                    + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 300px;\"><strong>ID</strong></td>"
                    + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 150px;\"><strong>REFERENCIA</strong></td>"
                    + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 150px;\"><strong>TIPO</strong></td>"
                    + "                               "
                    + "                </tr>";
            ArrayList<Transaccion> transaccions = new ArrayList<>(factura.getTransaccionCollection());
            for (Transaccion transaccion : transaccions) {

//                if (transaccion.getTipoTransaccionidtipoTransaccion().getIdtipoTransaccion() == 6) {
                //pago de factura
                facturaHtml += "                <tr style=\"height:30px;\">"
                        + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 150px;\">" + simpleDateFormaHour.format(new Date(transaccion.getFecha().longValue())) + "</td>"
                        + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 300px;\">" + transaccion.getIdTransaccion() + "</td>"
                        + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 150px;\">" + transaccion.getReferencia() + "</td>"
                        + "                    <td style=\"padding:2px; font-size:12px; text-align:center;width: 150px;\">" + transaccion.getTipoTransaccionidtipoTransaccion().getNombre() + "</td>"
                        + "                               "
                        + "                </tr>";

//                }
            }
            facturaHtml += "            </table>";
        }

        facturaHtml += "</body>"
                + "</html>";

        return facturaHtml;
    }

    public static void generarFacturaAutopagosLibre(FacturaAutopagos autopagos) throws NonexistentEntityException, Exception {

        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        Convenios convenios = conveniosJpaController.findConvenios(autopagos.getIdconvenio());
        JSONArray conceptos = new JSONArray(autopagos.getItems());
        ArrayList<Usuario> usuarios = new ArrayList<>(convenios.getUsuarioCollection());
        ArrayList<String> correos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            correos.add(usuario.getEmail());
        }

        //obtenemos los valores
        double subtotal = 0;
        double iva = 0;
        for (int i = 0; i < conceptos.length(); i++) {
            JSONObject jSONObject = conceptos.getJSONObject(i);
            subtotal += jSONObject.getDouble("valor");
        }
        iva = subtotal * propertiesAcces.IVA;

        Calendar calendarCatual = Calendar.getInstance();
        Calendar calendarVencimiento = Calendar.getInstance();
        calendarVencimiento.add(Calendar.DAY_OF_YEAR, 8);
        //
        FacturaAutopagos facturaAutopagos = new FacturaAutopagos();
        facturaAutopagos.setConveniosIdconvenios(convenios);
        facturaAutopagos.setEstadoIdestado(new Estado(1));
        facturaAutopagos.setFechaEmision(BigInteger.valueOf(System.currentTimeMillis()));
        facturaAutopagos.setIva(iva);
        facturaAutopagos.setNumeroTransaccionesConvenio(0);
        facturaAutopagos.setValor(subtotal);
        facturaAutopagos.setTipo(2);//tipo 1 == plan - tipo 2 == libre
        facturaAutopagos.setItems(conceptos.toString());
        //obtener las transacciones en json

        FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
        facturaAutopagos = facturaAutopagosJpaController.create(facturaAutopagos);

        String htmlFact = generarFactura(facturaAutopagos);
        //construir el archivo para adjuntar en el correo
        File padre = new File(VariablesSession.context + File.separator + "soportes" + File.separator + convenios.getNit() + File.separator);
        if (!padre.exists()) {
            padre.mkdirs();
        }
        File file = new File(VariablesSession.context + File.separator + "soportes" + File.separator + convenios.getNit() + File.separator + "Factura" + simpleDateFormatFile.format(new Date()) + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        InputStream is = new ByteArrayInputStream(htmlFact.getBytes());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
        document.close();
        outputStream.close();
        //aqui enviamos la factura por correo armandola con el pdf y el boton en el correo para que nos pague al conenio
        //enviamos el correo 
        double total = subtotal + iva;
        String template = CorreoZoho.getTemplate(CorreoZoho.nuevaFacturaGeneradaLibreTemplate);

        template = template.replace("$server", propertiesAcces.SERVER);
        template = template.replace("$valor", numberFormat.format(total));
        template = template.replace("$fecha-emision", simpleDateFormat.format(calendarCatual.getTime()));
        template = template.replace("$fecha-vencimiento", simpleDateFormat.format(calendarVencimiento.getTime()));
        template = template.replace("$link", propertiesAcces.SERVER + "LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());
        template = template.replace("$nombre-convenio", convenios.getNombre());
        CorreoZoho correoZoho = new CorreoZoho("Nueva factura generada AUTOPAGOS.CO", template, correos, file);
        correoZoho.start();

    }

    static void generarFacturaAutopagos(Convenios convenios, ArrayList<String> correos) throws NonexistentEntityException, Exception {
        int transacciones = convenios.getNumeroTransaccionesPlanPost();
        Plan plan = convenios.getPlanIdplan();
        int numeroExcedente = 0;
        double valorExcedente = 0;
        if (transacciones > plan.getNumeroTransacciones()) {
            numeroExcedente = transacciones - plan.getNumeroTransacciones();
            valorExcedente = numeroExcedente * plan.getValorExtraTransaccion();
        }

        double totalFactura = plan.getCargoBasico() + valorExcedente;
        double ivaFactura = propertiesAcces.IVA * totalFactura;
        double totalIvaIncluido = totalFactura + ivaFactura;

        Calendar calendarActual = Calendar.getInstance();
        Calendar calendarVencimiento = Calendar.getInstance();
        calendarVencimiento.add(Calendar.DAY_OF_YEAR, 8);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.add(Calendar.DAY_OF_YEAR, -1);
        calendarFinal.set(Calendar.HOUR, 0);
        calendarFinal.set(Calendar.MINUTE, 0);
        calendarFinal.set(Calendar.SECOND, 0);
        calendarFinal.set(Calendar.MILLISECOND, 0);

        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.setTime(calendarFinal.getTime());
        calendarInicial.add(Calendar.MONTH, -1);
        calendarInicial.set(Calendar.HOUR, 0);
        calendarInicial.set(Calendar.MINUTE, 0);
        calendarInicial.set(Calendar.SECOND, 0);
        calendarInicial.set(Calendar.MILLISECOND, 0);

        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
        ArrayList<Transaccion> transaccions = transaccionJpaController.getTransaccionesSiparByConvenioFacturacion(calendarInicial.getTimeInMillis(), calendarFinal.getTimeInMillis(), convenios.getIdconvenios());

        convenios.setNumeroTransaccionesPlanPost(0);
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        conveniosJpaController.edit(convenios);

        //generar factura
        FacturaAutopagos facturaAutopagos = new FacturaAutopagos();
        facturaAutopagos.setConveniosIdconvenios(convenios);
        facturaAutopagos.setEstadoIdestado(new Estado(1));
        facturaAutopagos.setFechaEmision(BigInteger.valueOf(calendarActual.getTimeInMillis()));
        facturaAutopagos.setIva(ivaFactura);
        facturaAutopagos.setNumeroTransaccionesConvenio(transacciones);
        facturaAutopagos.setValor(totalFactura);
        facturaAutopagos.setPlanIdplan(plan);
        facturaAutopagos.setTipo(1);//tipo 1 == plan - tipo 2 == libre
        facturaAutopagos.setTransaccionCollection(transaccions);
        FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
        facturaAutopagos = facturaAutopagosJpaController.create(facturaAutopagos);

        String htmlFact = generarFactura(facturaAutopagos);
        //construir el archivo para adjuntar en el correo
        File padre = new File(VariablesSession.context + File.separator + "soportes" + File.separator + convenios.getNit() + File.separator);
        if (!padre.exists()) {
            padre.mkdirs();
        }
        File file = new File(VariablesSession.context + File.separator + "soportes" + File.separator + convenios.getNit() + File.separator + "Factura" + simpleDateFormatFile.format(new Date()) + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        InputStream is = new ByteArrayInputStream(htmlFact.getBytes());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
        document.close();
        outputStream.close();

        //aqui enviamos la factura por correo armandola con el pdf y el boton en el correo para que nos pague al conenio
        //enviamos el correo 
        String template = CorreoZoho.getTemplate(CorreoZoho.nuevaFacturaGeneradaTemplate);
        template = template.replace("$server", propertiesAcces.SERVER);
        template = template.replace("$nombre-convenio", convenios.getNombre());
        template = template.replace("$nitComercio", convenios.getNit());
        template = template.replace("$fecha", simpleDateFormat.format(calendarActual.getTime()));
        template = template.replace("$tarifa-plan", (numberFormat.format(plan.getCargoBasico() * propertiesAcces.IVA)));
        template = template.replace("$total-trx-adicionles", numberFormat.format(valorExcedente));
        template = template.replace("$cant-trx-adicionles", numeroExcedente + "");
        template = template.replace("$total-trx-adicional", numberFormat.format(plan.getValorExtraTransaccion()));
        template = template.replace("$iva", numberFormat.format(ivaFactura));
        template = template.replace("$link", propertiesAcces.SERVER + "LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());
        template = template.replace("$total-a-pagar", numberFormat.format(totalIvaIncluido));
        template = template.replace("$fecha-vencimiento", simpleDateFormat.format(calendarVencimiento.getTime()));
        CorreoZoho correoZoho = new CorreoZoho("Nueva factura generada AUTOPAGOS.CO", template, correos, file);
        correoZoho.start();
    }

    public static void generarFacturaAutopagos(Convenios convenios, ArrayList<String> correos, Calendar calendarInicial, Calendar calendarFinal) throws NonexistentEntityException, Exception {
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
        ArrayList<Transaccion> transaccions = transaccionJpaController.getTransaccionesSiparByConvenio(calendarInicial.getTimeInMillis(), calendarFinal.getTimeInMillis(), convenios.getIdconvenios());
        int transacciones = transaccions.size();
        Plan plan = convenios.getPlanIdplan();
        int numeroExcedente = 0;
        double valorExcedente = 0;
        if (transacciones > plan.getNumeroTransacciones()) {
            numeroExcedente = transacciones - plan.getNumeroTransacciones();
            valorExcedente = numeroExcedente * plan.getValorExtraTransaccion();
        }

        double totalFactura = plan.getCargoBasico() + valorExcedente;
        double ivaFactura = propertiesAcces.IVA * totalFactura;

        Calendar calendarActual = Calendar.getInstance();
        Calendar calendarVencimiento = Calendar.getInstance();
        calendarVencimiento.add(Calendar.DAY_OF_YEAR, 8);

        convenios.setNumeroTransaccionesPlanPost(0);
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        conveniosJpaController.edit(convenios);

        double totalIvaIncluido = totalFactura + ivaFactura;
        //generar factura
        FacturaAutopagos facturaAutopagos = new FacturaAutopagos();
        facturaAutopagos.setConveniosIdconvenios(convenios);
        facturaAutopagos.setEstadoIdestado(new Estado(1));
        facturaAutopagos.setFechaEmision(BigInteger.valueOf(calendarActual.getTimeInMillis()));
        facturaAutopagos.setIva(ivaFactura);
        facturaAutopagos.setNumeroTransaccionesConvenio(transacciones);
        facturaAutopagos.setValor(totalFactura);
        facturaAutopagos.setPlanIdplan(plan);
        facturaAutopagos.setTipo(1);//tipo 1 == plan - tipo 2 == libre
        //facturaAutopagos.setTransaccionCollection(transaccions);
        FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
        facturaAutopagos = facturaAutopagosJpaController.create(facturaAutopagos);
        for (Transaccion transaccion : transaccions) {
            transaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
            transaccionJpaController.edit(transaccion);
        }
        facturaAutopagos.setTransaccionCollection(transaccions);

        String htmlFact = generarFactura(facturaAutopagos);
        //construir el archivo para adjuntar en el correo
        File padre = new File(VariablesSession.context + File.separator + "soportes" + File.separator + convenios.getNit() + File.separator);
        if (!padre.exists()) {
            padre.mkdirs();
        }
        File file = new File(VariablesSession.context + File.separator + "soportes" + File.separator + convenios.getNit() + File.separator + "Factura" + simpleDateFormatFile.format(new Date()) + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        InputStream is = new ByteArrayInputStream(htmlFact.getBytes());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
        document.close();
        outputStream.close();
        //aqui enviamos la factura por correo armandola con el pdf y el boton en el correo para que nos pague al conenio
        //enviamos el correo 
        String template = CorreoZoho.getTemplate(CorreoZoho.nuevaFacturaGeneradaTemplate);
        template = template.replace("$server", propertiesAcces.SERVER);
        template = template.replace("$nombre-convenio", convenios.getNombre());
        template = template.replace("$nitComercio", convenios.getNit());
        template = template.replace("$fecha", simpleDateFormat.format(calendarActual.getTime()));
        template = template.replace("$tarifa-plan", (numberFormat.format(plan.getCargoBasico() * propertiesAcces.IVA)));
        template = template.replace("$total-trx-adicionles", numberFormat.format(valorExcedente));
        template = template.replace("$cant-trx-adicionles", numeroExcedente + "");
        template = template.replace("$total-trx-adicional", numberFormat.format(plan.getValorExtraTransaccion()));
        template = template.replace("$iva", numberFormat.format(ivaFactura));
        template = template.replace("$link", propertiesAcces.SERVER + "LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());
        template = template.replace("$total-a-pagar", numberFormat.format(totalIvaIncluido));
        template = template.replace("$fecha-vencimiento", simpleDateFormat.format(calendarVencimiento.getTime()));
        CorreoZoho correoZoho = new CorreoZoho("Nueva factura generada AUTOPAGOS.CO", template, correos, file);
        correoZoho.start();

    }

    public static void recordarFactura(FacturaAutopagos facturaAutopagos, ArrayList<String> emails) {
        Calendar calendarVencimiento = Calendar.getInstance();
        calendarVencimiento.setTime(new Date(facturaAutopagos.getFechaEmision().longValue()));
        calendarVencimiento.add(Calendar.DAY_OF_YEAR, 8);
        if (facturaAutopagos.getTipo() == 1) {
            double total = facturaAutopagos.getValor() + facturaAutopagos.getIva();
            String template = CorreoZoho.getTemplate(CorreoZoho.recordatorioTemplate);
            template = template.replace("$server", propertiesAcces.SERVER);
            template = template.replace("$nombre-convenio", facturaAutopagos.getConveniosIdconvenios().getNombre());
            template = template.replace("$referencia", facturaAutopagos.getIdfacturaAutopagos() + "");
            template = template.replace("$total", numberFormat.format(total));
            template = template.replace("$fecha-vencimiento", simpleDateFormat.format(calendarVencimiento.getTime()));
            template = template.replace("$fecha", simpleDateFormat.format(facturaAutopagos.getFechaEmision().longValue()));
            template = template.replace("$link", propertiesAcces.SERVER + "LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());

            template = template.replace("$total-plan", numberFormat.format(facturaAutopagos.getPlanIdplan().getCargoBasico()));
            template = template.replace("$trx-adicional", numberFormat.format(facturaAutopagos.getPlanIdplan().getValorExtraTransaccion()));
            CorreoZoho correoZoho = new CorreoZoho("Factura pendiente de pago AUTOPAGOS.CO", template, emails);
            correoZoho.start();
        } else if (facturaAutopagos.getTipo() == 2) {
            double total = facturaAutopagos.getValor() + facturaAutopagos.getIva();
            String template = CorreoZoho.getTemplate(CorreoZoho.nuevaFacturaGeneradaLibreTemplate);
            template = template.replace("$nombre-convenio", facturaAutopagos.getConveniosIdconvenios().getNombre());
            template = template.replace("$referencia", facturaAutopagos.getIdfacturaAutopagos() + "");
            template = template.replace("$server", propertiesAcces.SERVER);
            template = template.replace("$valor", numberFormat.format(total));
            template = template.replace("$fecha-emision", simpleDateFormat.format(new Date(facturaAutopagos.getFechaEmision().longValue())));
            template = template.replace("$fecha-vencimiento", simpleDateFormat.format(calendarVencimiento.getTime()));
            template = template.replace("$link", propertiesAcces.SERVER + "LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());
            CorreoZoho correoZoho = new CorreoZoho("Factura pendiente de pago AUTOPAGOS.CO", template, emails);
            correoZoho.start();
        }
    }

    static void suspenderFactura(FacturaAutopagos facturaAutopagos, ArrayList<String> emails) throws NonexistentEntityException, Exception {
        Convenios convenios = facturaAutopagos.getConveniosIdconvenios();
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
        Calendar calendarVencimiento = Calendar.getInstance();
        calendarVencimiento.setTime(new Date(facturaAutopagos.getFechaEmision().longValue()));
        calendarVencimiento.add(Calendar.DAY_OF_YEAR, 8);
        if (facturaAutopagos.getTipo() == 1) {
            convenios.setActivo(Boolean.FALSE);
            conveniosJpaController.edit(convenios);
            //enviar correo de convenio cerrado por exceso de pago 
            String template = CorreoZoho.getTemplate(CorreoZoho.servicioSuspendidoTemplate);
            template = template.replace("$server", propertiesAcces.SERVER);
            template = template.replace("$nombre-convenio", convenios.getNombre());
            template = template.replace("$total", numberFormat.format(facturaAutopagos.getValor()));
            template = template.replace("$referencia", facturaAutopagos.getIdfacturaAutopagos()+"");
            template = template.replace("$fecha", simpleDateFormat.format(facturaAutopagos.getFechaEmision().longValue()));
            template = template.replace("$fecha-vencimiento", simpleDateFormat.format(calendarVencimiento.getTime()));
            
            template = template.replace("$total-plan", numberFormat.format(facturaAutopagos.getPlanIdplan().getCargoBasico()));
            template = template.replace("$trx-adicional", numberFormat.format(facturaAutopagos.getPlanIdplan().getValorExtraTransaccion()));
            template = template.replace("$link", propertiesAcces.SERVER + "LoadPagoSipar.ap?id=" + facturaAutopagos.getIdfacturaAutopagos());

            CorreoZoho correoZoho = new CorreoZoho("Servicio suspendido AUTOPAGOS.CO", template, emails);
            correoZoho.start();
        }
    }

    public static void createPDFFromString(String html) {

    }
}
