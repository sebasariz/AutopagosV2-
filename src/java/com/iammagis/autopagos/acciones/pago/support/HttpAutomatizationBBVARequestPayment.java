/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago.support;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author sebasariz
 */
public class HttpAutomatizationBBVARequestPayment {

    PropertiesAccess propertiesAcces = new PropertiesAccess();
    private String cookies;
    private HttpClient client = new DefaultHttpClient();
    private final String USER_AGENT = "Mozilla/5.0";

    public String init() throws IOException {
        HttpGet get = new HttpGet(propertiesAcces.BBVALINK);

        // add header
        get.setHeader("Host", "www.zonapagos.com");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Cache-Control", "no-cache");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Cookie", getCookies());
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Referer", "https://plataforma.autopagos.com/");
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        HttpResponse response = client.execute(get);

        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies = h.getValue().toString();
        }

        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("\nSending 'POST' request to URL : " + propertiesAcces.bbvaLink + "?estado_pago=enviar_datos");
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode);
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
        get.releaseConnection();
        return readedComplete;
    }

    public String getInicialForm(ComprobanterecaudoPSE comprobanterecaudoPSE) throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>(); 
        postParams.add(new BasicNameValuePair("id_pago", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE()+""));
        postParams.add(new BasicNameValuePair("total_con_iva", comprobanterecaudoPSE.getValorTotal()+""));
        postParams.add(new BasicNameValuePair("descrip_pago", comprobanterecaudoPSE.getDescripcion()));

        HttpPost post = new HttpPost(propertiesAcces.BBVALINK + "?estado_pago=enviar_datos");

        // add header
        post.setHeader("Host", "www.zonapagos.com");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.com/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        post.setEntity(new UrlEncodedFormEntity(postParams));

        HttpResponse response = client.execute(post);

        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("\nSending 'POST' request to URL : " + propertiesAcces.bbvaLink + "?estado_pago=enviar_datos");
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode);
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
        post.releaseConnection();
        return readedComplete;
    }

    public String secongCall(String codigoBanco, String persona) throws UnsupportedEncodingException, IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

//        Persona: 0
//        Empresa: 1  
        postParams.add(new BasicNameValuePair("lst_bancos_pse", codigoBanco));
        postParams.add(new BasicNameValuePair("opcion_forma_pago", "29"));
        postParams.add(new BasicNameValuePair("lst_tipo_persona", persona));

        HttpPost post = new HttpPost(propertiesAcces.BBVALINK + "?estado_pago=continuar_pago_medio_pago");
        // add header
        post.setHeader("Host", "www.zonapagos.com");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.com/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setEntity(new UrlEncodedFormEntity(postParams));

        HttpResponse response = client.execute(post);

        int responseCode = response.getStatusLine().getStatusCode();

        String redirectionURL = "";
        if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                redirectionURL += header.getValue();
            }
        } else {
            System.out.println("error de comunicacion con la entidad financiera aqui BBVA REQUEST");
        }

        post.releaseConnection();
        return redirectionURL;
    }

    public String getCookies() {
//        System.out.println("***********************************cookies: " + cookies);
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public static void main(String[] args) throws IOException {
        ComprobanterecaudoPSE comprobanterecaudoPSE = new ComprobanterecaudoPSE();
        comprobanterecaudoPSE.setDescripcion("Factura21");
        comprobanterecaudoPSE.setIdcomprobanteRecaudoPSE(7);
        comprobanterecaudoPSE.setValorTotal(130000D);
        
        HttpAutomatizationBBVARequestPayment httpAutomatization = new HttpAutomatizationBBVARequestPayment();
        httpAutomatization.init();
        String retorno = httpAutomatization.getInicialForm(comprobanterecaudoPSE);
        retorno = httpAutomatization.secongCall("1007", "0");
        System.out.println("\n\n\nretorno: " + retorno);

    }

}
