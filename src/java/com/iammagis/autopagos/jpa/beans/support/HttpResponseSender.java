/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger; 
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class HttpResponseSender {
    
    private String cookies;
    private final String USER_AGENT = "Mozilla/11.0";
    final HttpParams httpParams = new BasicHttpParams();
    
    public void sendJson(String url, JSONObject jSONObject) throws UnsupportedEncodingException, IOException {
        HttpConnectionParams.setConnectionTimeout(httpParams, 50000);
        HttpClient client = new DefaultHttpClient(httpParams);
        
        System.out.println("jSONObject_ " + jSONObject);
        try {
            HttpGet request = new HttpGet(url + "?id=" + jSONObject.getString("id")
                    + "&estado=" + jSONObject.getInt("estado")
                    + "&valor=" + jSONObject.getDouble("valor")
                    + "&referencia=" + jSONObject.getString("referencia"));
            
            request.setHeader("User-Agent", USER_AGENT);
            request.setHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            request.setHeader("Accept-Language", "en-US,en;q=0.5");            
            HttpResponse response = client.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (JSONException ex) {
            Logger.getLogger(HttpResponseSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getCookies() {
        return cookies;
    }

    
}
