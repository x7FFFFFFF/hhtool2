package ru.alex.vic.client.vk;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import ru.alex.vic.Utils;
import ru.alex.vic.client.HttpClient;
import ru.alex.vic.json.vk.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VkClient {

    private final HttpClient httpClient;
    private final Provider<VkUrlBuilder> urlBuilder;
    private final int timeout;


    @Inject
    public VkClient(HttpClient httpClient, Provider<VkUrlBuilder> urlBuilder, @Named("vk.api.request_timeout.ms") int timeout) {
        this.httpClient = httpClient;
        this.urlBuilder = urlBuilder;
        this.timeout = timeout;
    }


    public GetCountriesResponse getCountriesByCode(String... codes) {
        final String url = urlBuilder.get().getCountries().code(codes).build();
        return executeGet(url, GetCountriesResponse.class);
    }

    public GetRegionsResponse getRegionsByCountryCode(Integer code) {
        final String url = urlBuilder.get().getRegions().countryId(code).build();
        return executeGet(url, GetRegionsResponse.class);
    }

    public List<City> getCitiesByCountryAndRegion(Integer countryId, Integer regionId) {
        int offset = 0;
        final int count = 100;
        final VkUrlBuilder urlBuilder = this.urlBuilder.get().getCities()
                .countryId(countryId)
                .regionId(regionId)
                .offset(offset)
                .count(count);
        GetCitiesResponse response = executeGet(urlBuilder
                .build(), GetCitiesResponse.class);
        List<City> res = new ArrayList<>(response.getItems());
        while (response.getItems().size() != 0) {
            Utils.sleep(timeout);
            offset += count;
            urlBuilder.offset(offset);
            response = executeGet(urlBuilder.build(), GetCitiesResponse.class);
            res.addAll(response.getItems());
            if (res.size() == response.getCount()) {
                break;
            }
        }
        return res;
    }


    private <T> T executeGet(String url, Class<T> responseClass) {
        return httpClient.get(url, (gson, json) -> {
            if (json.has("error")) {
                JsonElement errorElement = json.get("error");
                try {
                    if (errorElement.isJsonObject()) {
                        final JsonObject errorJson = errorElement.getAsJsonObject();
                        if (errorJson.has("error_msg")) {
                            throw new RuntimeException(errorJson.get("error_msg").getAsString() + " ; url=" + url);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    throw new RuntimeException(e);
                }
                throw new RuntimeException("url=" + url);
            }

            JsonElement response = json;
            if (json.has("response")) {
                response = json.get("response");
            }

            try {
                return gson.fromJson(response, responseClass);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }

        });


    }


}
