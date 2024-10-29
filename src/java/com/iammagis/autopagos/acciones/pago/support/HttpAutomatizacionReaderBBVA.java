/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago.support;

import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.X509TrustManager;
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
public class HttpAutomatizacionReaderBBVA {

    PropertiesAccess propertiesAcces = new PropertiesAccess();
    private String cookies;
    private HttpClient client = new DefaultHttpClient();
    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    private String token;
    private String user;
    private String pass;

    
    
    public HttpAutomatizacionReaderBBVA(String user, String pass) {

        this.user = user;
        this.pass = pass;
    }

    public String initGetToken() throws IOException, KeyManagementException, NoSuchAlgorithmException {

        HttpGet get = new HttpGet(propertiesAcces.BBVALINKLOGIN + "administracion.asp");

        // add header
        get.setHeader("Host", "www.zonapagos.com");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Language", "es-419,es;q=0.8");
        get.setHeader("Cache-Control", "no-cache");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Referer", "https://plataforma.autopagos.com/");
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String readedComplete = "";
        HttpResponse response = client.execute(get);

        Header[] headers = response.getHeaders("Set-Cookie");
//        System.out.println("headers: " + headers.length);
        for (Header h : headers) {
            cookies = h.getValue().split("; ")[0] + ";";
        }
//        System.out.println("cookies: " + cookies + " ============================= 1");
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'GET' request to URL : " + propertiesAcces.bbvaLinkLogin + "administracion.asp");
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode + "\n\n");
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";

        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
        readedComplete = readedComplete.split("ACTION=\"administracion.asp?")[1].split("\">")[0];
        get.releaseConnection();
        token = readedComplete;

        return readedComplete;
    }

    public String initLogin() throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("txt_usuario", user));
        postParams.add(new BasicNameValuePair("txt_clave", pass));
        HttpPost post = new HttpPost(propertiesAcces.BBVALINKLOGIN + "administracion.asp" + token);

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "www.zonapagos.com");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", cookies);
        post.setHeader("Referer", "https://plataforma.autopagos.com/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        post.setEntity(new UrlEncodedFormEntity(postParams));
        HttpResponse response = client.execute(post);
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies += h.getValue().split(";")[0] + "; ";
        }
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'POST' request to URL : " + propertiesAcces.bbvaLinkLogin + "administracion.asp" + token);
//        System.out.println("Response Code : " + responseCode + "\n\n");
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

    public String getTable() throws IOException {

        HttpGet get = new HttpGet(propertiesAcces.BBVALINKDATA + "administracion/pagos.asp");

        // add header
        get.setHeader("Host", "www.zonapagos.com");
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

        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'GET' request to URL : " + propertiesAcces.BBVALINKDATA + "administracion/pagos.asp");
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode + "\n\n");
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
//        System.out.println("read: " + readedComplete);
        get.releaseConnection();
        return readedComplete;
    }

    public String getData() throws IOException {

        HttpGet get = new HttpGet(propertiesAcces.BBVALINKDATA + "administracion/plantillas/bbva/centro_inicio.asp");

        // add header
        get.setHeader("Host", "www.zonapagos.com");
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

        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'GET' request to URL : " + propertiesAcces.bbvaLinkData + "administracion/plantillas/bbva/centro_inicio.asp");
//        System.out.println("Post parameters : " + postParams);
//        System.out.println("Response Code : " + responseCode + "\n\n");
        GZIPInputStream gzis = new GZIPInputStream(response.getEntity().getContent());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed = "";
        String readedComplete = "";
        in.readLine();
        while ((readed = in.readLine()) != null) {
            readedComplete = readedComplete + readed;
        }
//        System.out.println("read: " + readedComplete);
        get.releaseConnection();
        return readedComplete;
    }

    public int getBusquedaId(String id) throws IOException {

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("btn_buscar", "Buscar"));
        postParams.add(new BasicNameValuePair("lst_buscar", "2"));
        postParams.add(new BasicNameValuePair("txt_buscar", id.trim()));

        HttpPost post = new HttpPost(propertiesAcces.BBVALINKDATA + "administracion/pagos.asp?estado=buscar&codigo_seleccion=&ruta_regreso=&parametros_regreso=&info_codigo_seleccion=");

//        System.out.println("cookies: " + cookies);
        // add header
        post.setHeader("Host", "www.zonapagos.com");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "es-419,es;q=0.8");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Cookie", cookies);
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://plataforma.autopagos.com/");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

//        System.out.println("enviando");
        post.setEntity(new UrlEncodedFormEntity(postParams));
        HttpResponse response = client.execute(post);
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            cookies += h.getValue().split(";")[0] + "; ";
        }
        int responseCode = response.getStatusLine().getStatusCode();

//        System.out.println("Sending 'POST' request to URL : " + propertiesAcces.bbvaLinkData + "administracion/pagos.asp?estado=buscar&codigo_seleccion=&ruta_regreso=&parametros_regreso=&info_codigo_seleccion=");
//        System.out.println("Response Code : " + responseCode + "\n\n");
        int estadoInt = 0;
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
            post.releaseConnection();
            String[] split = readedComplete.split("<TD CLASS=\"celda_info_mr\" ALIGN=\"LEFT\"><FONT CLASS=\"texto_normal\">");
            if (split.length > 7) {
                String estado = split[7].split("</FONT><FONT CLASS=\"texto_muy_pequeno\">&nbsp;</FONT></TD>")[0];
                System.out.println("estado: " + estado);
                if (estado.equals("Pago Rechazado")) {
                    estadoInt = 3;
                } else if (estado.equals("Pago Pendiente")) {
                    estadoInt = 4;
                } else if (estado.equals("Aprobada")) {
                    estadoInt = 2;
                }
            }
        }
        return estadoInt;
    }

    public String salidaSegura() throws IOException {

        HttpGet get = new HttpGet(propertiesAcces.BBVALINKDATA + "administracion/plantillas/bbva/salidad_segura.asp?estado=salida");

        // add header
        get.setHeader("Host", "www.zonapagos.com");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "*/*");
        get.setHeader("Upgrade-Insecure-Requests", "1");
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

//        System.out.println("\nSending 'GET' request to URL : " + propertiesAcces.bbvaLinkData + "administracion/plantillas/bbva/salidad_segura.asp?estado=salida");
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

    public static void main(String[] args) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        PropertiesAccess propertiesAcces = new PropertiesAccess();
        JSONArray array = new JSONArray();
        array.put(new JSONObject().put("id", 228));
        HttpAutomatizacionReaderBBVA httpAutomatizacionReader = new HttpAutomatizacionReaderBBVA(propertiesAcces.BBVAUSER, "Ag20sa16.");
        httpAutomatizacionReader.initGetToken();
        String retorno = httpAutomatizacionReader.initLogin();
        if (!retorno.contains("En este momento se encuentra un")) {
            httpAutomatizacionReader.getData();
            httpAutomatizacionReader.getTable();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jSONObjectResult = array.getJSONObject(i);
                System.out.println("haciendo busqueda");
                int estado = httpAutomatizacionReader.getBusquedaId(jSONObjectResult.getInt("id") + "");
                jSONObjectResult.put("estado", estado);
                array.put(i, jSONObjectResult);
            }
            String retornoSalida = httpAutomatizacionReader.salidaSegura();
            System.out.println("retorno salida: " + retornoSalida);
            System.out.println("array: " + array);
        } else {
            System.out.println("problema con el cierre de sesion");
        }
    }

    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string) throws java.security.cert.CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private String get(String link) throws KeyManagementException, NoSuchAlgorithmException {
        // configure the SSLContext with a TrustManager
//        SSLContext ctx = SSLContext.getInstance("TLS");
//        ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
//        SSLContext.setDefault(ctx);
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(link);

            conn = (HttpsURLConnection) url.openConnection();
//            conn.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String string, SSLSession ssls) {
//                    return true;
//                }
//            });
            //data
            conn.setRequestProperty("Host", "www.zonapagos.com");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Language", "es-419,es;q=0.8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("Referer", "https://plataforma.autopagos.com/");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            conn.setRequestMethod("GET");
//            conn.setUseCaches(false);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);

//            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String webPage = "", data = "";
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        cookies = cookie + ";";
                    }
                }
                while ((data = reader.readLine()) != null) {
                    webPage += data + "\n";
                }
            } else if (responseCode == 302) {
                System.out.println("codigo 302");
            }
            //Log.e("resultado: ","resultado :"+webPage);
            return webPage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return "{}";
    }

    private String post(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
