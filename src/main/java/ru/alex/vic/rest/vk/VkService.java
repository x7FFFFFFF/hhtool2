package ru.alex.vic.rest.vk;

import com.google.inject.persist.Transactional;
import ru.alex.vic.client.vk.VkClient;
import ru.alex.vic.json.vk.GetCountriesResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Singleton
@Path("vk")
public class VkService {

    private final VkClient vkClient;

    @Inject
    public VkService(VkClient vkClient) {
        this.vkClient = vkClient;

    }

    @GET
    @Path("loadCountries")
    @Produces(TEXT_HTML)
    @Transactional
    public String loadCountries() {
        final GetCountriesResponse countriesByCode = vkClient.getCountriesByCode("RU", "UA", "BY");


        return "OK. Found: " + countriesByCode.getCount();
    }
}
