package ru.alex.vic.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Singleton
public class HttpClient {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";
    public static final String APP_HTTPCLIENT_CONN_TIME_TO_LIVE = "app.httpclient.conn_time_to_live";
    private final int connTimeToLive;
    private final Gson gson;

    @Inject
    public HttpClient(@Named(APP_HTTPCLIENT_CONN_TIME_TO_LIVE) int connTimeToLive, Gson gson) {
        this.connTimeToLive = connTimeToLive;
        this.gson = gson;
    }

    public <T> T get(String url, Class<T> clz) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setConnectionTimeToLive(connTimeToLive, TimeUnit.MINUTES).build();
             CloseableHttpResponse response = httpclient.execute(httpGet);
             Reader reader = new InputStreamReader(response.getEntity().getContent(), UTF_8)
        ) {
            return gson.fromJson(reader, clz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(String url, BiFunction<Gson, JsonObject, T> function) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setConnectionTimeToLive(connTimeToLive, TimeUnit.MINUTES).build();
             CloseableHttpResponse response = httpclient.execute(httpGet);
             Reader reader = new InputStreamReader(response.getEntity().getContent(), UTF_8);
             JsonReader jsonReader = new JsonReader(reader)
        ) {
            JsonObject json = (JsonObject) new JsonParser().parse(jsonReader);
            return function.apply(getGson(), json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Gson getGson() {
        return gson;
    }
}
