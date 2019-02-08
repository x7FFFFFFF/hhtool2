package ru.alex.vic.rest.vk;

import ru.alex.vic.client.vk.VkClient;
import ru.alex.vic.dao.vk.VkLocationDao;
import ru.alex.vic.entities.vk.VkLocation;
import ru.alex.vic.json.Response;
import ru.alex.vic.json.vk.Country;
import ru.alex.vic.json.vk.GetCountriesResponse;
import ru.alex.vic.json.vk.GetRegionsResponse;
import ru.alex.vic.json.vk.Region;
import ru.alex.vic.rest.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("vk")
public class VkService {


    public static final String LOAD_LOCATIONS = "loadLocations";
    private final VkLocationDao vkLocationDao;
    private final VkClient vkClient;
    private final Service service;
    private final int timeout;

    @Inject
    public VkService(VkLocationDao vkLocationDao, VkClient vkClient, Service service, @Named("vk.api.request_timeout.ms") int timeout) {
        this.vkLocationDao = vkLocationDao;
        this.vkClient = vkClient;
        this.service = service;
        this.timeout = timeout;
    }

    /**
     * Росссия  RU
     * Украина  UA
     * Казахстан KZ
     * Азербайджан AZ
     * Беларусь  BY
     * Другие страны  ? TODO:
     * Грузия GE
     * Кыргызстан KG
     * Узбекистан UZ
     *
     * @return
     */
    @GET
    @Path("loadRegions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<Region> loadRegions(@QueryParam("id") String countryCode) {

        vkLocationDao.deleteAll();

        final GetCountriesResponse countriesByCode = vkClient.getCountriesByCode(countryCode);
        for (Country country : countriesByCode.getItems()) {
            vkLocationDao.save(new VkLocation(country));
        }
        List<Region> regions = new ArrayList<>();
        for (Country country : countriesByCode.getItems()) {
            final GetRegionsResponse regionsByCountryCode = vkClient.getRegionsByCountryCode(country.getId());
            regions.addAll(regionsByCountryCode.getItems());
            for (Region region : regionsByCountryCode.getItems()) {
                vkLocationDao.save(new VkLocation(region, country));
            }
        }
        return Response.of(regions);
    }


    @GET
    @Path("searchLocation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<VkLocation> searchLocation(@QueryParam("id") String name) {
        final List<VkLocation> locations = vkLocationDao.findByFieldLike("name", name);
        return Response.of(locations);
    }


}
