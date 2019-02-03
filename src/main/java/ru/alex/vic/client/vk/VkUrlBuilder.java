package ru.alex.vic.client.vk;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VkUrlBuilder {

    private final String apiBaseUrl;
    private final String apiVersion;
    private final String userId;
    private final String accessToken;
    private String method;
    private final Map<String, String> params = new HashMap<>();


    //"https://api.vk.com/method/METHOD_NAME?PARAMETERS&access_token=ACCESS_TOKEN&v=V"
    @Inject
    public VkUrlBuilder(@Named("vk.api.base_url") String apiBaseUrl, @Named("vk.api.version") String apiVersion, @Named("vk.user_id") String userId, @Named("vk.access_token") String accessToken) {
        this.apiBaseUrl = apiBaseUrl;
        this.apiVersion = apiVersion;
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public VkUrlBuilder getCountries() {
        this.method = "database.getCountries";
        return this;
    }

    public VkUrlBuilder getRegions() {
        this.method = "database.getRegions";
        return this;
    }
    public VkUrlBuilder getCities() {
        this.method = "database.getCities";
        return this;
    }


    public VkUrlBuilder method(String method) {
        this.method = method;
        return this;
    }

    VkUrlBuilder param(String key, String value) {
        params.put(key, value);
        return this;
    }

    VkUrlBuilder param(String key, Integer value) {
        params.put(key, value.toString());
        return this;
    }

    public VkUrlBuilder offset(int offset) {
        return param("offset", String.valueOf(offset));
    }

    public VkUrlBuilder count(int count) {
        return param("count", String.valueOf(count));
    }

    public VkUrlBuilder needAll() {
        return param("need_all", "1");
    }

    public VkUrlBuilder code(String... codes) {
        return param("code", Stream.of(codes).collect(Collectors.joining(",")));
    }

    public VkUrlBuilder countryId(Integer code) {
        return param("country_id", code);
    }

    private String buildParams() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }
        return builder.toString();
    }

    public String build() {
        return apiBaseUrl + method + "?" + buildParams() + "access_token=" + accessToken + "&v=" + apiVersion;
    }

    public VkUrlBuilder regionId(Integer regionId) {
        return param("region_id", regionId);
    }
}
