package de.merlinw.twasi.utilities;

import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Plugin extends TwasiPlugin {
    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return Userplugin.class;
    }

    private static int timeout = 3;

    public static String getApiContent(String url) throws IOException {
        return getApiContent(new HttpGet(url));
    }

    public static String putApiContent(String url, Map<String, String> headers) throws IOException {
        return putApiContent(url, headers, null);
    }

    public static String putApiContent(String url, Map<String, String> headers, String body) throws IOException {
        HttpPut put = new HttpPut(url);
        if (body != null) put.setEntity(new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8)));
        for (String header : headers.keySet()) put.addHeader(header, headers.get(header));
        return getApiContent(put);
    }

    public static String getApiContent(HttpRequestBase request) throws IOException {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }

    public static String getCommandArgs(String command){
        String rt = "";
        String[] arr = command.split(" ");
        if(arr.length > 1) {
            String[] copArr = new String[arr.length - 1];
            System.arraycopy(arr, 1, copArr, 0, arr.length - 1);
            for (String s : copArr) if(s != null && !s.equals("")) rt += s + " ";
            return rt.substring(0, rt.length() - 1);
        } else return null;
    }
}
