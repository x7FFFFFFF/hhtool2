package ru.alex.vic.client.vk;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import ru.alex.vic.client.HttpClient;
import ru.alex.vic.json.vk.GetCitiesResponse;
import ru.alex.vic.json.vk.GetCountriesResponse;
import ru.alex.vic.json.vk.GetRegionsResponse;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class VkClient {

    private final HttpClient httpClient;
    private final Provider<VkUrlBuilder> urlBuilder;


    @Inject
    public VkClient(HttpClient httpClient, Provider<VkUrlBuilder> urlBuilder) {
        this.httpClient = httpClient;
        this.urlBuilder = urlBuilder;
    }


    public GetCountriesResponse getCountriesByCode(String... codes) {
        final String url = urlBuilder.get().getCountries().code(codes).build();
        return executeGet(url, GetCountriesResponse.class);
    }

    public GetRegionsResponse getRegionsByCountryCode(Integer code) {
        final String url = urlBuilder.get().getRegions().countryId(code).build();
        return executeGet(url, GetRegionsResponse.class);
    }

    public GetCitiesResponse getCitiesByCountryAndRegion(Integer countryId, Integer regionId) {
        final String url = urlBuilder.get().getRegions()
                .countryId(countryId)
                .regionId( regionId)
                .needAll()
                .build();
        return executeGet(url, GetCitiesResponse.class);
    }


    private <T> T executeGet(String url, Class<T> responseClass) {
        return httpClient.get(url, (gson, json) -> {
            if (json.has("error")) {
                JsonElement errorElement = json.get("error");
                Error error;
                try {
                    error = gson.fromJson(errorElement, Error.class);
                } catch (JsonSyntaxException e) {
                    throw new RuntimeException(e);
                }
                throw error;
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
