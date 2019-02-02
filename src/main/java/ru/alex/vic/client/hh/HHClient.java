package ru.alex.vic.client.hh;

import ru.alex.vic.client.HttpClient;
import ru.alex.vic.json.hh.HHLocationJson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HHClient {
    private final HttpClient httpClient;

    @Inject
    public HHClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    public HHLocationJson[] getLocations(){
        return httpClient.get("https://api.hh.ru/areas", HHLocationJson[].class);
    }








}
