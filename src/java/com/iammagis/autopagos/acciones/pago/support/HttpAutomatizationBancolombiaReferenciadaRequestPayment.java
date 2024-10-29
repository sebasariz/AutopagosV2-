/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago.support;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
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
public class HttpAutomatizationBancolombiaReferenciadaRequestPayment {

    DecimalFormat decimalFormat = new DecimalFormat("#####");
    PropertiesAccess propertiesAcces = new PropertiesAccess();
    private String cookies;
    private HttpClient client = new DefaultHttpClient();
    private final String USER_AGENT = "Mozilla/5.0";
    private String retornoCode;

    private String hdInvoiceIDs;
    private String hdInvoiceValues;

    public String init() throws IOException {
        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINK);

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

    public String getInicialForm(ComprobanterecaudoPSE comprobanterecaudoPSE, Convenios convenios) throws IOException {

        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("step", "1"));
        postParams.add(new BasicNameValuePair("customer_id", comprobanterecaudoPSE.getDescripcion() + ""));
        postParams.add(new BasicNameValuePair("btnPay", "Continuar"));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINK + "DatabaseTicketOffice.aspx?ID=" + convenios.getCodigoCanalPlanPost());

        // add header
        post.setHeader("Host", "www.psepagos.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.co/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        post.setEntity(new UrlEncodedFormEntity(postParams));

        HttpResponse response = client.execute(post);

        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
//        System.out.println("readedComplete: " + readedComplete);
        post.releaseConnection();
        hdInvoiceIDs = readedComplete.split("name=\"hdPay_1\" type=\"hidden\" value=\"")[1].split("\"")[0];
        hdInvoiceValues = readedComplete.split("name=\"txtPay_1\" type=\"text\" value=\"")[1].split("\"")[0];
//        System.out.println("hdInvoiceIDs: " + hdInvoiceIDs + " hdInvoiceValues " + hdInvoiceValues);
        return readedComplete;
    }

    public String getInitialFormStep2(ComprobanterecaudoPSE comprobanterecaudoPSE, Convenios convenios) throws UnsupportedEncodingException, IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//        Persona: 0
//        Empresa: 1  

        postParams.add(new BasicNameValuePair("ID", convenios.getCodigoCanalPlanPost()));
        postParams.add(new BasicNameValuePair("hdInvoiceIDs", hdInvoiceIDs));
        postParams.add(new BasicNameValuePair("hdInvoiceValues", hdInvoiceValues));
//        System.out.println("Identificador: " + comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "_" + comprobanterecaudoPSE.getDescripcion());
        postParams.add(new BasicNameValuePair("id_cliente", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "_" + comprobanterecaudoPSE.getDescripcion()));
        postParams.add(new BasicNameValuePair("customer_id", comprobanterecaudoPSE.getDescripcion() + ""));
        postParams.add(new BasicNameValuePair("email", propertiesAcces.CORREOBANCOLOMBIA));
        postParams.add(new BasicNameValuePair("hdPay_1", hdInvoiceIDs));
        postParams.add(new BasicNameValuePair("chkPay_1", hdInvoiceIDs));
        postParams.add(new BasicNameValuePair("txtPay_1", hdInvoiceValues));
        postParams.add(new BasicNameValuePair("Step", "2"));

//        System.out.println("link: " + propertiesAcces.bancolombiaLink + "GetBankList.aspx?enc=" + retornoCode);
        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINK + "DatabaseTicketOffice.aspx?enc=" + retornoCode);
        // add header
        post.setHeader("Host", "www.psepagos.co");
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
            System.out.println("error de comunicacion con la entidad financiera aqui");
            return "error";
        }
        post.releaseConnection();
        return redirectionURL;
    }

    public String secongCall(String url, String codigoBanco, String persona) throws UnsupportedEncodingException, IOException {
        System.out.println("url: " + propertiesAcces.BANCOLOMBIALINK + url);
        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINK + url.split("/PSEHostingUI")[1]);

        // add header
        post.setHeader("Host", "www.psepagos.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.co/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        HttpResponse response = client.execute(post);

        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
//        System.out.println("readedComplete: " + readedComplete);
        post.releaseConnection();
        //cargamos el link

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//        Persona: 0
//        Empresa: 1  
        retornoCode = url.split("enc=")[1];
//        System.out.println("retornoCode: "+retornoCode);
        postParams.add(new BasicNameValuePair("listBanks", codigoBanco));
        postParams.add(new BasicNameValuePair("listUserType", persona));
        postParams.add(new BasicNameValuePair("btnContinue", "Aguarde..."));
        postParams.add(new BasicNameValuePair("enc", "retornoCode"));

//        System.out.println("link: " + propertiesAcces.bancolombiaLink + "GetBankList.aspx?enc=" + retornoCode);
        post = new HttpPost(propertiesAcces.BANCOLOMBIALINK + "GetBankListBD.aspx?enc=" + retornoCode);
        // add header
        post.setHeader("Host", "www.psepagos.co");
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
        response = client.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        String redirectionURL = "";
        if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                redirectionURL += header.getValue();
            }
        } else {
            System.out.println("error de comunicacion con la entidad financiera aqui");
            return "error";
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

        Convenios convenios = new Convenios();
        convenios.setCodigoCanalPlanPost("3511");
        convenios.setCamposObligatoriosSipar("customer_id");
        ComprobanterecaudoPSE comprobanterecaudoPSE = new ComprobanterecaudoPSE();
        comprobanterecaudoPSE.setDescripcion("1017132961");
        comprobanterecaudoPSE.setIdcomprobanteRecaudoPSE(1017132961);
        comprobanterecaudoPSE.setValorTotal(130000D);

        HttpAutomatizationBancolombiaReferenciadaRequestPayment httpAutomatization = new HttpAutomatizationBancolombiaReferenciadaRequestPayment();
        httpAutomatization.init();
        String retorno = httpAutomatization.getInicialForm(comprobanterecaudoPSE, convenios);
        retorno = httpAutomatization.getInitialFormStep2(comprobanterecaudoPSE, convenios);
//        System.out.println("retorno: " + retorno);
        retorno = httpAutomatization.secongCall(retorno, "1007", "0");
        System.out.println("\n\n\nretorno: " + retorno);
    }

}
