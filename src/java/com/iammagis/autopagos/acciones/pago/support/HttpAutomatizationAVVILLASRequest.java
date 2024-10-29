/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago.support;

import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

/**
 *
 * @author Usuario
 */
public class HttpAutomatizationAVVILLASRequest {

    PropertiesAccess propertiesAccess = new PropertiesAccess();

    //temp
    String codigoConvenio;
    //Generales
    private String cookies;
    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36";
    private final String HOST = "www.pagosvirtualesavvillas.com.co";
    private final String languaje = "es-419,es;q=0.8,en;q=0.6,gl;q=0.4";
    private final String Accept = "*/*";
    private final String cache_control = "no-cache";
    private final String Accept_Encoding = "gzip,deflate,sdch, br";
    private final String Connection = "keep-alive";
    private final String Referer = "https://plataforma.autopagos.com/";
    private final HttpClient client;

    public HttpAutomatizationAVVILLASRequest() {
        this.client = HttpClientBuilder.create().build();
        codigoConvenio = propertiesAccess.CODIGOCONVENIOAVVILLAS;
        System.out.println("codigoConvenio: "+codigoConvenio);
    }

    public String getTokenPagePSECargaRecarga(String url, String codigoAutopagos, String descripcion, String codigoBanco, String correo, String saldo,String idConvenio,int idCOmprobantePSE) throws IOException, JSONException {
        String readedComplete = ""; 
        HttpPost post = new HttpPost(url);
        //add parameters
        
        
        org.json.JSONObject jSONObject = new org.json.JSONObject("{\"pago\":{"
                + "\"id\":null,"
                + "\"activo\":false,"
                + "\"fecha\":null,"
                + "\"convenioId\":"+idConvenio+","
                + "\"baseDatosDetalleId\":null,"
                        + "\"referenciaPago1\":null,"
                        + "\"referenciaPago2\":null,"
                        + "\"referenciaPago3\":null,"
                        + "\"referenciaPago4\":null,"
                        + "\"pseUserType\":\"0\","
                        + "\"pseCodigoBanco\":\""+codigoBanco+"\","
                        + "\"pseNombreBanco\":null,"
                        + "\"pseFechaProceso\":null,"
                        + "\"valor\":"+saldo+","
                        + "\"randomUuid\":null,"
                        + "\"descripcion\":\""+descripcion+"\","
                        + "\"aprobado\":null,"
                        + "\"usuarioId\":null,"
                        + "\"direccionIp\":null,"
                        + "\"correo\":\""+correo+"\","
                        + "\"intencionPagoId\":null,"
                        + "\"pseudocuenta\":null,"
                        + "\"tipoDocumentoId\":null,"
                        + "\"numeroDocumento\":null,"
                        + "\"creadoPorWs\":false,"
                        + "\"fechaWsFinalizada\":null,"
                        + "\"urlRespuesta\":null,"
                        +"\"tipoPagoId\":1,"
                        + "\"hash\":null,\"transaccionAvalId\":null,"
                        + "\"adicional1\":null,"
                        + "\"adicional2\":null,"
                        + "\"adicional3\":null,"
                        + "\"transaccionConvenioId\":null,"
                        + "\"estadoRegistro\":0},"
                                + "\"detalles\":["
                                    + "{\"reg\":{"
                                        + "\"id\":1,\"activo\":true,"
                                        + "\"pagoId\":0,\"fecha\":null,"
                                        + "\"convenioId\":"+idConvenio+" "
                                                + ",\"baseDatosDetalleId\":89644539,"
                                                + "\"referenciaPago1\":\""+idCOmprobantePSE+"\","
                                                + "\"referenciaPago2\":null,"
                                                + "\"referenciaPago3\":null,"
                                                + "\"referenciaPago4\":null,"
                                                        + "\"convenioConceptoId\":null,"
                                                        + "\"tipoDocumentoId\":null,"
                                                        + "\"numeroDocumento\":null,"
                                                        + "\"intencionPagoId\":null,"
                                                        + "\"pseudocuenta\":null,"
                                                        + "\"valor\":"+saldo+",\"estadoRegistro\":1}"
                                                                + ",\"informacionAdicional\":[],\"convenio\":{"
                                                                + "\"id\":"+idConvenio+""
                                                                        + ",\"activo\":true"
                                                                        + ",\"recaudadorId\":23738"
                                                                        + ",\"nombre\":\"AUTOPAGOS - CLL 32 E No.80 A - 57 INT 201\""
                                                                        + ",\"numero\":\"5101139210\""
                                                                        + ",\"municipioId\":612"
                                                                        + ",\"direccion\":\"Cll 32E #80A - 57 INT 201\""
                                                                        + ",\"contacto\":\"Alejandro Gomez Montoya\""
                                                                        + ",\"telefono\":\"3015972588\""
                                                                        + ",\"extension\":null"
                                                                        + ",\"correo\":\"alejandro.gomez@autopagos.co\""
                                                                        + ",\"tipoCuentaId\":1,\"cuentaRecaudo\":\"510115876\""
                                                                        + ",\"descripcion\":\"CLL 32 E No.80 A - 57 INT 201\""
                                                                        + ",\"categoriaConvenioId\":10"
                                                                        + ",\"palabrasClave\":\"AUTOPAGOS\""
                                                                        + ",\"usaBaseDatos\":true"
                                                                        + ",\"variosPagosPorReferencia\":true"
                                                                        + ",\"referencia1\":\"REFERENCIA\""
                                                                        + ",\"referencia1Largo\":5"
                                                                        + ",\"referencia2\":null"
                                                                        + ",\"referencia2Largo\":null"
                                                                        + ",\"referencia2Requerida\":false"
                                                                        + ",\"referencia3\":null"
                                                                        + ",\"referencia3Largo\":null"
                                                                        + ",\"referencia3Requerida\":false"
                                                                        + ",\"referencia4\":null"
                                                                        + ",\"referencia4Largo\":null"
                                                                        + ",\"referencia4Requerida\":false"
                                                                        + ",\"baseDatosIdActual\":8935"
                                                                        + ",\"wsUrlConsultarFactura\":null"
                                                                        + ",\"wsUrlVerificarFactura\":null"
                                                                        + ",\"wsUrlPagarFactura\":null"
                                                                        + ",\"wsTipoPago\":null"
                                                                        + ",\"requiereIdentificacion\":false"
                                                                        + ",\"habilitarAccesoWs\":false"
                                                                        + ",\"urlRespuesta\":null"
                                                                        + ",\"mostrarRespuestaCpv\":false"
                                                                        + ",\"urlImagen\":null"
                                                                        + ",\"ocultarEnCpv\":false"
                                                                        + ",\"permitirPagoPse\":true"
                                                                        + ",\"permitirPagoTdc\":false"
                                                                        + ",\"codigoUnicoTdc\":null"
                                                                        + ",\"claveHttps\":null"
                                                                        + ",\"urlOrigenHttps\":null"
                                                                        + ",\"rbmIdTerminal\":null"
                                                                        + ",\"permitirAval\":false"
                                                                        + ",\"sincronizacionAvalId\":null"
                                                                        + ",\"fechaCreacion\":1461012176000"
                                                                        + ",\"fechaActualizacion\":1461702903000"
                                                                        + ",\"referenciaGrandesPagadores\":null"
                                                                        + ",\"permitirMultiplesPagos\":false"
                                                                        + ",\"permitirMultiplesConvenios\":false"
                                                                        + ",\"mostrarPagadas\":true"
                                                                        + ",\"permitirCambiarValor\":false"
                                                                        + ",\"mostrarEstado\":true"
                                                                        + ",\"referencia1Tipo\":2"
                                                                        + ",\"referencia2Tipo\":2"
                                                                        + ",\"referencia3Tipo\":2"
                                                                        + ",\"referencia4Tipo\":2"
                                                                        + ",\"estadoRegistro\":1}}]}");
        
        System.out.println("correo: " + correo);
        System.out.println("codigoConvenio: " + codigoConvenio);
        System.out.println("codigoBanco: " + codigoBanco);
//        JSONObject jsonObjectPago = new JSONObject();
//        jsonObjectPago.put("activo", false);
//        jsonObjectPago.put("adicional1", "");
//        jsonObjectPago.put("adicional2", "");
//        jsonObjectPago.put("adicional3", "");
//        jsonObjectPago.put("aprobado", "");
//        jsonObjectPago.put("baseDatosDetalleId", "");
//        jsonObjectPago.put("convenioId", codigoConvenio);
//        jsonObjectPago.put("correo", correo);
//        jsonObjectPago.put("creadoPorWs", false);
//        jsonObjectPago.put("descripcion", descripcion);
//        jsonObjectPago.put("direccionIp", "");
//        jsonObjectPago.put("estadoRegistro", 0);
//        jsonObjectPago.put("fecha", "");
//        jsonObjectPago.put("fechaWsFinalizada", "");
//        jsonObjectPago.put("hash", "");
//        jsonObjectPago.put("id", "");
//        jsonObjectPago.put("intencionPagoId", null);
//        jsonObjectPago.put("numeroDocumento", "");
//        jsonObjectPago.put("pseCodigoBanco", codigoBanco);
//        jsonObjectPago.put("pseFechaProceso", "");
//        jsonObjectPago.put("pseNombreBanco", "");
//        jsonObjectPago.put("pseUserType", "0");
//        jsonObjectPago.put("pseudocuenta", null);
//        jsonObjectPago.put("randomUuid", null);
//        jsonObjectPago.put("referenciaPago1", codigoAutopagos);
//        jsonObjectPago.put("referenciaPago2", "");
//        jsonObjectPago.put("referenciaPago3", "");
//        jsonObjectPago.put("referenciaPago4", "");
//        jsonObjectPago.put("tipoDocumentoId", "");
//        jsonObjectPago.put("tipoPagoId", "1");
//        jsonObjectPago.put("transaccionAvalId", "");
//        jsonObjectPago.put("transaccionConvenioId", "");
//        jsonObjectPago.put("urlRespuesta", "");
//        jsonObjectPago.put("usuarioId", "");
//        jsonObjectPago.put("valor", saldo);

        //JSONArray jSONObjectDetalles = new JSONArray("[{\"reg\":{\"id\":null,\"activo\":true,\"pagoId\":0,\"fecha\":null,\"convenioId\":4355,\"baseDatosDetalleId\":89644539,\"referenciaPago1\":\"3744075\",\"referenciaPago2\":null,\"referenciaPago3\":null,\"referenciaPago4\":null,\"convenioConceptoId\":null,\"tipoDocumentoId\":null,\"numeroDocumento\":null,\"intencionPagoId\":null,\"pseudocuenta\":null,\"valor\":"+saldo+",\"estadoRegistro\":1},\"informacionAdicional\":[],\"convenio\":{\"id\":4355,\"activo\":true,\"recaudadorId\":19499,\"nombre\":\"FULLCARGA COLOMBIA SA\",\"numero\":\"3821031591\",\"municipioId\":760,\"direccion\":\"AV 19 108 45 OFI 201\",\"contacto\":\"Fredy Alberto LEON HERNANDEZ\",\"telefono\":\"7454242\",\"extension\":\"146\",\"correo\":\"fredy.leon@fullcarga.com.co\",\"tipoCuentaId\":2,\"cuentaRecaudo\":\"382005809\",\"descripcion\":\"AV 19 108 45 OFI 201\",\"categoriaConvenioId\":10,\"palabrasClave\":\"FULLCARGA COLOMBIA SA\",\"usaBaseDatos\":true,\"variosPagosPorReferencia\":true,\"referencia1\":\"CODIGO\",\"referencia1Largo\":8,\"referencia2\":null,\"referencia2Largo\":null,\"referencia2Requerida\":false,\"referencia3\":null,\"referencia3Largo\":null,\"referencia3Requerida\":false,\"referencia4\":null,\"referencia4Largo\":null,\"referencia4Requerida\":false,\"baseDatosIdActual\":6129,\"wsUrlConsultarFactura\":null,\"wsUrlVerificarFactura\":null,\"wsUrlPagarFactura\":null,\"wsTipoPago\":null,\"requiereIdentificacion\":false,\"habilitarAccesoWs\":false,\"urlRespuesta\":null,\"mostrarRespuestaCpv\":false,\"urlImagen\":null,\"ocultarEnCpv\":false,\"permitirPagoPse\":true,\"permitirPagoTdc\":false,\"codigoUnicoTdc\":null,\"claveHttps\":null,\"urlOrigenHttps\":null,\"rbmIdTerminal\":null,\"permitirAval\":false,\"sincronizacionAvalId\":null,\"fechaCreacion\":1461012176000,\"fechaActualizacion\":1461702903000,\"referenciaGrandesPagadores\":null,\"permitirMultiplesPagos\":false,\"permitirMultiplesConvenios\":false,\"mostrarPagadas\":true,\"permitirCambiarValor\":false,\"mostrarEstado\":true,\"referencia1Tipo\":2,\"referencia2Tipo\":2,\"referencia3Tipo\":2,\"referencia4Tipo\":2,\"estadoRegistro\":1}}]");
         
//        JSONArray jSONArrayDetalles=new JSONArray();
//        
//        JSONObject jSONObjectDetalles = new JSONObject();
//        
//        JSONObject jSONObjectReg = new JSONObject();
//        jSONObjectReg.put("id", null);
//        jSONObjectReg.put("activo", true);
//        jSONObjectReg.put("pagoId", 0);
//        jSONObjectReg.put("fecha", null);
//        
//        jSONObjectReg.put("convenioId", 4355);
//        jSONObjectReg.put("baseDatosDetalleId", 89644539);
//        jSONObjectReg.put("referenciaPago1", 3744075);
//        jSONObjectReg.put("referenciaPago2", null);
//        jSONObjectReg.put("referenciaPago3", null);
//        jSONObjectReg.put("referenciaPago4", null);
//        jSONObjectReg.put("convenioConceptoId", null);
//        jSONObjectReg.put("tipoDocumentoId", null);
//        jSONObjectReg.put("numeroDocumento", null);
//        jSONObjectReg.put("intencionPagoId", null);
//        jSONObjectReg.put("pseudocuenta", null);
//        jSONObjectReg.put("valor", saldo);
//        jSONObjectReg.put("estadoRegistro", 1); 
        //uno de los campso
//        jSONObjectDetalles.put("reg", jSONObjectReg);
//        //els segundo campo
//        JSONArray jSONArrayInfoAdicional = new JSONArray();
//        jSONObjectDetalles.put("informacionAdicional", jSONArrayInfoAdicional);
        //falta el campo convenio
//        JSONObject jSONObjectConvenio = new JSONObject();
//        jSONObjectConvenio.put("activo", true);
//        jSONObjectConvenio.put("baseDatosIdActual", 6129);
//        jSONObjectConvenio.put("categoriaConvenioId", 10);
//        
//        jSONObjectConvenio.put("claveHttps", null);
//        jSONObjectConvenio.put("codigoUnicoTdc", null);
//        jSONObjectConvenio.put("contacto", "Fredy Alberto LEON HERNANDEZ");
//        jSONObjectConvenio.put("correo", "fredy.leon@fullcarga.com.co");
//        jSONObjectConvenio.put("cuentaRecaudo", "382005809");
//        jSONObjectConvenio.put("descripcion", "AV 19 108 45 OFI 201");
//        jSONObjectConvenio.put("direccion", "AV 19 108 45 OFI 201");
//        jSONObjectConvenio.put("fechaActualizacion", "1461702903000");
//        jSONObjectConvenio.put("fechaCreacion", "1461012176000");
//        jSONObjectConvenio.put("habilitarAccesoWs", false);
//        jSONObjectConvenio.put("id", 4355);
//        jSONObjectConvenio.put("mostrarEstado", true);
//        jSONObjectConvenio.put("mostrarPagadas", true);
//        jSONObjectConvenio.put("mostrarRespuestaCpv", false);
//        jSONObjectConvenio.put("municipioId", 760);
//        jSONObjectConvenio.put("nombre", "FULLCARGA COLOMBIA SA");
//        
//        jSONObjectConvenio.put("numero", "3821031591");
//        jSONObjectConvenio.put("ocultarEnCpv", "false");
//        jSONObjectConvenio.put("palabrasClave", "FULLCARGA COLOMBIA SA");
//        jSONObjectConvenio.put("permitirAval", false);
//        jSONObjectConvenio.put("permitirCambiarValor", false);
//        jSONObjectConvenio.put("permitirMultiplesConvenios", false);
//        jSONObjectConvenio.put("permitirMultiplesPagos", false);
//        jSONObjectConvenio.put("permitirPagoPse",true);
//        jSONObjectConvenio.put("permitirPagoTdc", false);
//        jSONObjectConvenio.put("rbmIdTerminal", null);
//        jSONObjectConvenio.put("recaudadorId", 19499);
//        jSONObjectConvenio.put("referencia1", "CODIGO");
//        jSONObjectConvenio.put("referencia1Largo", 8);
//        jSONObjectConvenio.put("referencia1Tipo", 2);
//        jSONObjectConvenio.put("referencia2", null);
//        jSONObjectConvenio.put("referencia2Largo", null);
//        jSONObjectConvenio.put("referencia2Requerida", false);
//        jSONObjectConvenio.put("referencia2Tipo", 2);
//        jSONObjectConvenio.put("referencia3", null);
//        jSONObjectConvenio.put("referencia3Largo", null);
//        jSONObjectConvenio.put("referencia3Requerida", false);
//        jSONObjectConvenio.put("referencia3Tipo", 2);
//        jSONObjectConvenio.put("referencia4", null);
//        jSONObjectConvenio.put("referencia4Largo", null);
//        jSONObjectConvenio.put("referencia4Requerida", false);
//        jSONObjectConvenio.put("referencia4Tipo", 2);
//        jSONObjectConvenio.put("referenciaGrandesPagadores", null);
//        jSONObjectConvenio.put("requiereIdentificacion", false);
//        jSONObjectConvenio.put("sincronizacionAvalId", null);
//        jSONObjectConvenio.put("telefono", "7454242");
//        
//        jSONObjectConvenio.put("tipoCuentaId", 2);
//        jSONObjectConvenio.put("urlImagen", null);
//        jSONObjectConvenio.put("urlOrigenHttps", null);
//        jSONObjectConvenio.put("urlRespuesta", null);
//        jSONObjectConvenio.put("usaBaseDatos", true);
//        jSONObjectConvenio.put("variosPagosPorReferencia", true);
//        jSONObjectConvenio.put("wsTipoPago", null);
//        jSONObjectConvenio.put("wsUrlConsultarFactura", null);
//        jSONObjectConvenio.put("wsUrlPagarFactura", null);
//        jSONObjectConvenio.put("wsUrlVerificarFactura", null);
//        jSONObjectDetalles.put("convenio", jSONObjectConvenio);
//        
//        jSONArrayDetalles.put(jSONObjectDetalles);
        




        
                
        
        
        
        
        
        //jSONObjectGlobal.put("pago", jsonObjectPago);
        //jSONObjectGlobal.put("detalles", jSONArrayDetalles);
        
        // add header
        post.setHeader("Host", HOST);
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", Accept);
        post.setHeader("Accept-Language", languaje);
        post.setHeader("Cache-Control", cache_control);
        post.setHeader("Accept-Encoding", Accept_Encoding);
        post.setHeader("Connection", Connection);
        post.setHeader("Referer", Referer);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        post.setHeader("X-Requested-With", "XMLHttpRequest");

        StringEntity postingString = new StringEntity(jSONObject.toString());
        post.setEntity(postingString);

        HttpResponse response = client.execute(post);
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies = h.getValue().split("; ")[0] + ";";
        }
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("Sending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        String json = EntityUtils.toString(response.getEntity());
        readedComplete = json;
        post.releaseConnection();
        return readedComplete;
    }

}


