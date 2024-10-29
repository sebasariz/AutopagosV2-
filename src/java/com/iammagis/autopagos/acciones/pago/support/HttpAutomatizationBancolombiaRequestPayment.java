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
public class HttpAutomatizationBancolombiaRequestPayment {

    DecimalFormat decimalFormat = new DecimalFormat("#####");
    PropertiesAccess propertiesAcces = new PropertiesAccess();
    private String cookies;
    private HttpClient client = new DefaultHttpClient();
    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";
    private String retornoCode;
    private String CSRFToken = "";

    public String init(Convenios convenios) throws IOException {
        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINK + "/ShowTicketOffice.aspx?ID=" + convenios.getCodigoCanalPlanPost());

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

        System.out.println("Response Code : " + responseCode);
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
        System.out.println("readedComplete: " + readedComplete.contains("CSRFToken"));
        if (readedComplete.contains("CSRFToken")) {
            CSRFToken = readedComplete.split("CSRFToken\" value=\"")[1].split("\"")[0];
            System.out.println("CSRFToken: " + CSRFToken);
        }
        response.getEntity().getContent().close();
        get.releaseConnection();

        return readedComplete;
    }

    public String getInicialForm(ComprobanterecaudoPSE comprobanterecaudoPSE, Convenios convenios) throws IOException {
//        System.out.println(" inicial form");
        double iva = comprobanterecaudoPSE.getValorTotal() * propertiesAcces.IVA;
        double valor = comprobanterecaudoPSE.getValorTotal();

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("id_cliente", comprobanterecaudoPSE.getDescripcion() + ""));
        postParams.add(new BasicNameValuePair("CSRFToken", CSRFToken)); 
        postParams.add(new BasicNameValuePair("descripcion_pago", comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + "_" + comprobanterecaudoPSE.getDescripcion()));
        postParams.add(new BasicNameValuePair("total_con_iva", decimalFormat.format(valor)));
        postParams.add(new BasicNameValuePair("valor_iva", decimalFormat.format(iva)));
        postParams.add(new BasicNameValuePair("email", propertiesAcces.CORREOBANCOLOMBIA));
        postParams.add(new BasicNameValuePair("btnPay", "Pagar"));
        String[] campos = convenios.getCamposObligatoriosSipar().split(",");
        for (String campo : campos) {
            postParams.add(new BasicNameValuePair(campo, comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE() + ""));
        }
//        System.out.println("URL: " + propertiesAcces.BANCOLOMBIALINK + "BasicTicketOffice.aspx?ID=" + convenios.getCodigoCanalPlanPost());
        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINK + "BasicTicketOffice.aspx?ID=" + convenios.getCodigoCanalPlanPost());

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
        String redirectionURLCode = "";
        System.out.println("responseCode: " + responseCode);
        if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                redirectionURLCode += header.getValue();
            }
        } else {
            System.out.println("error de comunicacion con la entidad financiera aqui inicial form");
            return redirectionURLCode;
        }

        retornoCode = redirectionURLCode.split("enc=")[1];
        if (response.getEntity() != null) {
            response.getEntity().consumeContent();
        }
        response.getEntity().getContent().close();
        post.releaseConnection();
        return redirectionURLCode;
    }

    public String secongCall(String codigoBanco, String persona) throws UnsupportedEncodingException, IOException {
//        System.out.println(" second call: " + codigoBanco + " persona: " + persona + " retornoCode. " + retornoCode);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//        Persona: 0
//        Empresa: 1  

        postParams.add(new BasicNameValuePair("CSRFToken", CSRFToken)); 
        postParams.add(new BasicNameValuePair("listBanks", codigoBanco));
        postParams.add(new BasicNameValuePair("listUserType", persona));
        postParams.add(new BasicNameValuePair("btnContinue", "Aguarde..."));
        postParams.add(new BasicNameValuePair("enc", "retornoCode"));

//        System.out.println("link: " + propertiesAcces.BANCOLOMBIALINK + "GetBankList.aspx?enc=" + retornoCode);
        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINK + "GetBankList.aspx?enc=" + retornoCode);
        // add header
        post.setHeader("Origin", " https://www.psepagos.co");
        post.setHeader("Host", "www.psepagos.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Accept-Language", "es-CO,es;q=0.9,es-419;q=0.8,en;q=0.7,pt;q=0.6,gl;q=0.5");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.com/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new UrlEncodedFormEntity(postParams));
        HttpResponse response = client.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        String redirectionURL = "";
        if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                redirectionURL += header.getValue();
            }
        } else {
            System.out.println("error de comunicacion con la entidad financiera aqui second call");
            return "error";
        }
        post.releaseConnection();
        System.out.println("redirectionURL: " + redirectionURL);
        return redirectionURL;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public static void main(String[] args) throws IOException {

        Convenios convenios = new Convenios();
        convenios.setCodigoCanalPlanPost("4488");
        convenios.setCamposObligatoriosSipar("nombre_cliente,email");
        ComprobanterecaudoPSE comprobanterecaudoPSE = new ComprobanterecaudoPSE();
        comprobanterecaudoPSE.setDescripcion("PruebaAutopagos");
        comprobanterecaudoPSE.setIdcomprobanteRecaudoPSE(1017132961);
        comprobanterecaudoPSE.setValorTotal(130000D);

        HttpAutomatizationBancolombiaRequestPayment httpAutomatization = new HttpAutomatizationBancolombiaRequestPayment();
        httpAutomatization.init(convenios);
        String retorno = httpAutomatization.getInicialForm(comprobanterecaudoPSE, convenios);
        //si es ta
        System.out.println("retorno: " + retorno);
        retorno = httpAutomatization.secongCall("1007", "0");
        System.out.println("\n\n\nretorno: " + retorno);

    }

}
