/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago.support;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sebasariz
 */
public class HttpAutomatizacionReaderBancolombia {

    PropertiesAccess propertiesAcces = new PropertiesAccess();
    private String cookies;
    private HttpClient client = new DefaultHttpClient();
    private final String USER_AGENT = "Mozilla/5.0";
    private String __VIEWSTATE = "";
    private String __VIEWSTATEGENERATOR = "";
    private String __EVENTVALIDATION = "";
    private Convenios convenios;

    public HttpAutomatizacionReaderBancolombia(Convenios convenios) {
        System.out.println("id comcercio:" + convenios.getCodigoCanalPlanPost() + " user:" + convenios.getUsuarioCanalPlanPost() + " pass_:" + convenios.getPassCanalPlanPost() + " campo: " + convenios.getCampoIdentificadorSipar());
        this.convenios = convenios;
    }

    public String initGetToken() throws IOException {

        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINKLOGIN + "framesets/header.aspx");

        // add header
        get.setHeader("Host", "ui.pse.com.co");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Cache-Control", "no-cache");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Referer", "https://plataforma.autopagos.com/");
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        HttpResponse response = client.execute(get);

        Header[] headers = response.getHeaders("Set-Cookie");
//        System.out.println("headers: " + headers.length);
        for (Header h : headers) {
            cookies = h.getValue().split("; ")[0] + ";";
        }
//        System.out.println("cookies: " + cookies + " =============================");
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'GET' request to URL : " + propertiesAcces.bancolombiaLogin + "framesets/header.aspx");
//        System.out.println("Response Code : " + responseCode + "\n\n");
//        System.out.println("response.getEntity().getContent(): "+response.getEntity().getContent());
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
//        System.out.println("readedComplete: "+readedComplete);
        __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
        __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);
        __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];
//        System.out.println("__EVENTVALIDATION: " + __EVENTVALIDATION);
        get.releaseConnection();
        return readedComplete;
    }

    public boolean login() throws IOException {
        boolean sesion = false;
//        "PSE.2016"
//        System.out.println("Init login");
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("txtUserName", convenios.getUsuarioCanalPlanPost()));
        postParams.add(new BasicNameValuePair("txtPassword", convenios.getPassCanalPlanPost()));
        postParams.add(new BasicNameValuePair("btnDoLogin.x", "17"));
        postParams.add(new BasicNameValuePair("btnDoLogin.y", "11"));
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "framesets/header.aspx");

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", cookies);
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.com/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
        HttpResponse response = client.execute(post);
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies += h.getValue().split(";")[0] + "; ";
        }
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'POST' request to URL : " + propertiesAcces.bancolombiaLogin + "framesets/header.aspx");
        System.out.println("Response Code : " + responseCode +" :"+response.getStatusLine()+" ==== LOGIN ====");
        for (org.apache.http.Header header : response.getHeaders("Location")) {
                String redirectionURLCode = header.getValue();
                System.out.println("redirectionURLCode: " + redirectionURLCode);
        }
        //nuevo llamado
        
        if (responseCode == 200 || responseCode == 302) {
            GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);

            String readed = "";
            String readedComplete = "";
            in.readLine();
            while ((readed = in.readLine()) != null) {
                readedComplete = readedComplete + readed;
            }
            if (readedComplete.contains("Por seguridad, su clave está bloqueada. Entre en contacto con su administrador para el desbloqueo.")
                    || readedComplete.contains("Código de acceso o clave inválidos. Intente nuevamente")) {
                sesion = false;
                return sesion;
            } else {
                sesion = true;
            }
//        System.out.println("readedComplete: " + readedComplete);
            __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
            __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);
//        __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];
//        System.out.println("__EVENTVALIDATION: " + __EVENTVALIDATION);
            post.releaseConnection();
            return sesion;
        } else {
            return false;
        }
    }

    public String getReportSearch() throws IOException {
//        System.out.println("\n\n\n================GetReportSearch");
        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");

        // add header
        get.setHeader("Host", "ui.pse.com.co");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Cache-Control", "no-cache");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Cookie", cookies);
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Referer", "https://plataforma.autopagos.com/");
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

//        HttpParams params = new BasicHttpParams();
//        params.setParameter("estado", "value1"); 
        HttpResponse response = client.execute(get);
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies += h.getValue().split(";")[0] + "; ";
        }
        int responseCode = response.getStatusLine().getStatusCode();
        String readedComplete = "";
//                System.out.println("Response Code : " + responseCode + "\n\n");
        if (responseCode == 200) {
//        System.out.println("Sending 'GET' request to URL : " + propertiesAcces.bancolombiaLogin + "Reports/InvoiceReportSearch.aspx");
//        System.out.println("Post parameters : " + postParams);

            GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);

            String readed = "";

            in.readLine();
            while ((readed = in.readLine()) != null) {
                readedComplete = readedComplete + readed;
            }
//            System.out.println("readedComplete: " + readedComplete);
            __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
            __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);
            __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];
//        System.out.println("__EVENTVALIDATION: " + __EVENTVALIDATION);

        } else if (responseCode == 302) {
            System.out.println("entre por 302");
        }
        get.releaseConnection();
        return readedComplete;
    }

    public String selectConvenio() throws IOException {
        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("__EVENTTARGET", "ddTaquilla"));
        postParams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        postParams.add(new BasicNameValuePair("__LASTFOCUS", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("ddTaquilla", convenios.getCodigoCanalPlanPost()));
 
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpField", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpVal1", "PSE"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpField", "Estado"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpVal1", "Paga"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$dpField", "Fecha Pago"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$dpOperator", "between"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$txtVal1", "09/04/2018 00:00:00"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$txtVal2", "09/04/2018 23:59:59"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField1", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField2", "Estado"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField3", ""));

        postParams.add(new BasicNameValuePair("ddRegistros", "20"));
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");
 
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8,en;q=0.6,gl;q=0.4");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Cookie", cookies);
        post.setHeader("Origin", "https://ui.pse.com.co");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx?ID=" + convenios.getCodigoCanalPlanPost());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
        HttpResponse response = client.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("responseCode select convenio: " + responseCode);

        String readedComplete = "";
        if (responseCode == 200 && response != null) {

            GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);

            String readed = "";

            in.readLine();
            while ((readed = in.readLine()) != null) {
                readedComplete = readedComplete + readed;
            }
//                System.out.println("readedComplete: " + readedComplete);
            __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
            __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);

            __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];

        } else if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                String redirectionURLCode = header.getValue();
//                System.out.println("redirectionURLCode: " + redirectionURLCode);
            }
        }
        post.releaseConnection();
        return readedComplete;
    }

    public String deleteCriteriosBusqueda4() throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("__EVENTTARGET", ""));
        postParams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        postParams.add(new BasicNameValuePair("__LASTFOCUS", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("ddTaquilla", convenios.getCodigoCanalPlanPost()));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpField", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpVal1", "PSE"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpField", "Estado"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpVal1", "Paga"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$dpField", "Fecha Pago"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$dpOperator", "between"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$txtVal1", "12/07/2015 00:00:00"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$txtVal2", "12/07/2016 23:59:59"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$ImageButton2.x", "2"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl04$ImageButton2.y", "10"));

        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField1", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField2", "Estado"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField3", ""));

        postParams.add(new BasicNameValuePair("ddRegistros", ""));
        
        
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8,en;q=0.6,gl;q=0.4");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Cookie", cookies);
        post.setHeader("Origin", "https://ui.pse.com.co");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
        HttpResponse response = client.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("responseCode delete: " + responseCode);

        String readedComplete = "";
        if (responseCode == 200) {
            if (response != null) {

                GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
                InputStreamReader reader = new InputStreamReader(gzis);
                BufferedReader in = new BufferedReader(reader);

                String readed = "";

                in.readLine();
                while ((readed = in.readLine()) != null) {
                    readedComplete = readedComplete + readed;
                }
//                System.out.println("readedComplete: " + readedComplete);
                __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
                __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);

                __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];

            }
        } else if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                String redirectionURLCode = header.getValue();
//                System.out.println("redirectionURLCode: " + redirectionURLCode);
            }
        }
        post.releaseConnection();
        return readedComplete;
    }

    public String deleteCriteriosBusqueda3() throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("__EVENTTARGET", ""));
        postParams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        postParams.add(new BasicNameValuePair("__LASTFOCUS", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("ddTaquilla", convenios.getCodigoCanalPlanPost()));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpField", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpVal1", "PSE"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpField", "Estado"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$dpVal1", "Paga"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$ImageButton2.x", "6"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl03$ImageButton2.y", "10"));

        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField1", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField2", "Estado"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField3", ""));

        postParams.add(new BasicNameValuePair("ddRegistros", ""));
        
        postParams.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8,en;q=0.6,gl;q=0.4");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Cookie", cookies);
        post.setHeader("Origin", "https://ui.pse.com.co");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx?ID=3948");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(postParams, "UTF-8");
//        System.out.println("encodedFormEntity: "+encodedFormEntity);
        post.setEntity(encodedFormEntity);

//        System.out.println("post: "+EntityUtils.toString(post.getEntity()));
        HttpResponse response = client.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("deleteCriteriosBusqueda3 responseCode: " + responseCode);

        String readedComplete = "";
        if (responseCode == 200) {
            if (response != null) {

                GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
                InputStreamReader reader = new InputStreamReader(gzis);
                BufferedReader in = new BufferedReader(reader);

                String readed = "";

                in.readLine();
                while ((readed = in.readLine()) != null) {
                    readedComplete = readedComplete + readed;
                }
                System.out.println("readedComplete: " + readedComplete);
                __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
                __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);

                __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];

            }
        } else if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                String redirectionURLCode = header.getValue();
//                System.out.println("redirectionURLCode: " + redirectionURLCode);
            }
        }
        post.releaseConnection();
        return readedComplete;
    }

    public String selectCriteriosBusqueda() throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("__EVENTTARGET", "dynCriterias$dtCriterias$ctl02$dpField"));
        postParams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        postParams.add(new BasicNameValuePair("__LASTFOCUS", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("ddTaquilla", convenios.getCodigoCanalPlanPost()));

        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpField", convenios.getCampoIdentificadorSipar()));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpVal1", "PSE"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField1", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField2", "Estado"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField3", ""));

        postParams.add(new BasicNameValuePair("ddRegistros", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8,en;q=0.6,gl;q=0.4");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Cookie", cookies);
        post.setHeader("Origin", "https://ui.pse.com.co");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx?ID=" + convenios.getCodigoCanalPlanPost());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(postParams, "UTF-8");
//        System.out.println("encodedFormEntity: "+encodedFormEntity);
        post.setEntity(encodedFormEntity);

//        System.out.println("post: "+EntityUtils.toString(post.getEntity()));
        HttpResponse response = client.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
//        System.out.println("selectCriteriosBusqueda responseCode: " + responseCode);
//
        String readedComplete = "";
        if (responseCode == 200) {
            if (response != null) {

                GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
                InputStreamReader reader = new InputStreamReader(gzis);
                BufferedReader in = new BufferedReader(reader);

                String readed = "";

                in.readLine();
                while ((readed = in.readLine()) != null) {
                    readedComplete = readedComplete + readed;
                }
//                System.out.println("readedComplete: " + readedComplete);
                __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
                __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);

                __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];

            }
        } else if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                String redirectionURLCode = header.getValue();
//                System.out.println("redirectionURLCode: " + redirectionURLCode);
            }
        }
        post.releaseConnection();
        return readedComplete;
    }

    public void getBusquedaId(String id) throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("__EVENTTARGET", ""));
        postParams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        postParams.add(new BasicNameValuePair("__LASTFOCUS", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("ddTaquilla", convenios.getCodigoCanalPlanPost()));
//        System.out.println("id: "+id);
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpField", convenios.getCampoIdentificadorSipar()));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$dpOperator", "equals"));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$txtVal1", id));
        postParams.add(new BasicNameValuePair("dynCriterias$dtCriterias$ctl02$rdConector", "and"));

        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField1", "Modalidad de Pago"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField2", "Estado"));
        postParams.add(new BasicNameValuePair("dynOrderBy$ddSortField3", ""));

        postParams.add(new BasicNameValuePair("ddRegistros", "20"));
        
        postParams.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
        postParams.add(new BasicNameValuePair("btnConsultar", "Consultar"));
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx?ID=" + convenios.getCodigoCanalPlanPost());

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8,en;q=0.6,gl;q=0.4");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Cookie", cookies);
        post.setHeader("Origin", "https://ui.pse.com.co");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSearch.aspx");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String redirectionURLCode = "";
//        System.out.println("enviando");
        post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
        HttpResponse response = client.execute(post);
//        System.out.println("cookies: " + cookies);
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'POST' request to URL : " + propertiesAcces.bancolombiaLogin + "transaction/epp/EPPQueryCriterias.aspx");
        System.out.println("Response Code : busqueda por ID: " + responseCode );
        if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                redirectionURLCode += header.getValue();
                System.out.println("redirectionURLCode InvoiceReportSummary : " + redirectionURLCode);
            }
        } else {
            System.out.println("error de comunicacion con la entidad financiera aqui busqueda por ID");
        }
        post.releaseConnection();
        //Aqui llamo la direccion por get para realizar la validacion

//        System.out.println("llamamos el get para obtener la tabla: " + propertiesAcces.bancolombiaLogin + redirectionURLCode.replace("/ui", ""));
        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINKLOGIN + redirectionURLCode.replace("/ui/", ""));

        // add header
        get.setHeader("Host", "ui.pse.com.co");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Cookie", cookies);
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Origin", "https://ui.pse.com.co");
        get.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + redirectionURLCode.replace("/ui/", ""));

        response = client.execute(get);
        responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == 200) {
            GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);

            String readed = "";
            String readedComplete = "";
            in.readLine();
            while ((readed = in.readLine()) != null) {
                readedComplete = readedComplete + readed;
            }

//            System.out.println("readedComplete: " + readedComplete + "\n\n\n\n\n\n");
            __VIEWSTATE = readedComplete.split("__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"")[1].split("\"")[0];
//        System.out.println("ViewState: " + __VIEWSTATE);
            __VIEWSTATEGENERATOR = readedComplete.split("name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"")[1].split("\"")[0];
//        System.out.println("__VIEWSTATEGENERATOR: " + __VIEWSTATEGENERATOR);
            __EVENTVALIDATION = readedComplete.split("name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"")[1].split("\"")[0];
//        System.out.println("__EVENTVALIDATION: " + __EVENTVALIDATION);
        }
        get.releaseConnection();
    }

    public int getTablaResult() throws IOException {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        postParams.add(new BasicNameValuePair("btnSearch", "Consultar Registros"));
        
        postParams.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
        postParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
        postParams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
        

        HttpPost post = new HttpPost(propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSummary.aspx?ID=" + convenios.getCodigoCanalPlanPost());
        // add header
        post.setHeader("Host", "ui.pse.com.co");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8,en;q=0.6,gl;q=0.4");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Cookie", cookies);
        post.setHeader("Origin", "https://ui.pse.com.co");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + "Reports/InvoiceReportSummary.aspx?ID=3948");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String redirectionURLCode = "";
//        System.out.println("enviando");
        post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
        HttpResponse response = client.execute(post);
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies += h.getValue().split(";")[0] + "; ";
        }
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'POST' request to URL : " + propertiesAcces.bancolombiaLogin + "transaction/epp/EPPQueryCriterias.aspx");
//        System.out.println("Response Code : " + responseCode + "\n\n");
        if (responseCode == 302) {
            for (org.apache.http.Header header : response.getHeaders("Location")) {
                redirectionURLCode += header.getValue();
                System.out.println("redirectionURLCode: " + redirectionURLCode);
            }
        } else {
            System.out.println("error de comunicacion con la entidad financiera aqui getTable result");
        }
        post.releaseConnection();
        //Aqui llamo la direccion por get para realizar la validacion

//        System.out.println("llamamos el get para obtener la tabla: " + propertiesAcces.bancolombiaLogin + redirectionURLCode.replace("/ui", ""));
        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINKLOGIN + redirectionURLCode.replace("/ui/", ""));

        // add header
        get.setHeader("Host", "ui.pse.com.co");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Cookie", cookies);
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("Connection", "keep-alive");
        post.setHeader("Origin", "https://ui.pse.com.co");
        get.setHeader("Referer", propertiesAcces.BANCOLOMBIALINKLOGIN + redirectionURLCode.replace("/ui/", ""));

//        HttpParams params = new BasicHttpParams();
//        params.setParameter("estado", "value1"); 
        response = client.execute(get);
        headers = response.getHeaders("Set-Cookie");

        responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'GET' request to URL : " + propertiesAcces.bancolombiaLogin + redirectionURLCode.replace("/ui/", ""));
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode + "\n\n");
        int respuesta = 0;
        String readedComplete = "";
        if (responseCode == 200) {
            GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);

            String readed = "";
            in.readLine();
            while ((readed = in.readLine()) != null) {
                readedComplete = readedComplete + readed;
            }
//            System.out.println("readedComplete: "+readedComplete);
            if (readedComplete.split("<td class=\"dataGridItem\">").length > 1) {
                readedComplete = readedComplete.split("<td class=\"dataGridItem\">")[1];

                if (readedComplete.split("\">").length > 1) {
                    System.out.println("Texto: " + readedComplete.split("\">")[1].split("</a>")[0]);
                    if (readedComplete.split("\">")[1].split("</a>")[0].equalsIgnoreCase("Paga")) {
                        respuesta = 2;
                    } else if (readedComplete.split("\">")[1].split("</a>")[0].equalsIgnoreCase("Rechazada") || readedComplete.split("\">")[1].split("</a>")[0].equalsIgnoreCase("Fallida")) {
                        respuesta = 3;
                    }
                } else {
                    respuesta = 1;
                }
            }
        }
        get.releaseConnection();
        return respuesta;
    }

    public String salidaSegura() throws IOException {

        HttpGet get = new HttpGet(propertiesAcces.BANCOLOMBIALINKLOGIN + "logOut.aspx");

        // add header
        get.setHeader("Host", "ui.pse.com.co");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Cache-Control", "no-cache");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Cookie", cookies);
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Referer", "https://plataforma.autopagos.com/");
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        HttpResponse response = client.execute(get);

        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies += h.getValue().toString();
        }

        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("\nSending 'GET' request to URL : " + propertiesAcces.bancolombiaLogin + "logOut.aspx");
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode);
        String readedComplete = "";
        if (responseCode == 200) {
            GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);

            String readed = "";

            in.readLine();
            while ((readed = in.readLine()) != null) {
                readedComplete = readedComplete + readed;
            }
//            System.out.println("read: " + readedComplete);
            get.releaseConnection();
        }
        return readedComplete;
    }

    public static void main(String[] args) throws IOException, JSONException {
        JSONArray array = new JSONArray();
        array.put(new JSONObject().put("id", "302"));

        Convenios convenios = new Convenios();
        convenios.setCodigoCanalPlanPost("4043");
        convenios.setCampoIdentificadorSipar("Referencia de Pago");
        convenios.setUsuarioCanalPlanPost("Autopagos");
        convenios.setPassCanalPlanPost("20HolaBanco18");
        HttpAutomatizacionReaderBancolombia httpAutomatizacionReader = new HttpAutomatizacionReaderBancolombia(convenios);
        httpAutomatizacionReader.initGetToken();
        boolean sesion = httpAutomatizacionReader.login();
        //seleccionar el convenio 
        if (sesion) {
            for (int i = 0; i < array.length(); i++) {
                httpAutomatizacionReader.getReportSearch();
                httpAutomatizacionReader.selectConvenio();
                httpAutomatizacionReader.deleteCriteriosBusqueda4();
//                httpAutomatizacionReader.deleteCriteriosBusqueda3();
//                httpAutomatizacionReader.selectCriteriosBusqueda();
//                JSONObject jSONObjectResult = array.getJSONObject(i);
//                httpAutomatizacionReader.getBusquedaId(jSONObjectResult.getString("id"));
//                int estado = httpAutomatizacionReader.getTablaResult();
//                jSONObjectResult.put("estado", estado);
//                array.put(i, jSONObjectResult);
            }
            httpAutomatizacionReader.salidaSegura();
        } else {
            System.out.println("problemas con sesion de convenio");
        }
        System.out.println("array: " + array);
    }

}
