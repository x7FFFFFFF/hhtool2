package ru.alex.vic.rest.vk;

import ru.alex.vic.client.vk.VkClient;
import ru.alex.vic.dao.vk.VkLocationDao;
import ru.alex.vic.entities.vk.VkLocation;
import ru.alex.vic.json.Response;
import ru.alex.vic.json.vk.*;
import ru.alex.vic.rest.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.*;
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


    @Inject
    public VkService(VkLocationDao vkLocationDao, VkClient vkClient, Service service) {
        this.vkLocationDao = vkLocationDao;
        this.vkClient = vkClient;
        this.service = service;
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

        //vkLocationDao.deleteAll();

        final GetCountriesResponse countriesByCode = vkClient.getCountriesByCode(countryCode);
        vkLocationDao.save(countriesByCode.getItems(), VkLocation::from);
        List<Region> regions = new ArrayList<>();
        for (Country country : countriesByCode.getItems()) {
            final GetRegionsResponse regionsByCountryCode = vkClient.getRegionsByCountryCode(country.getId());
            regions.addAll(regionsByCountryCode.getItems());
            vkLocationDao.save(regionsByCountryCode.getItems(), country, VkLocation::from);
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


    @GET
    @Path("loadCities/{vkRegionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<VkLocation> loadCities(@PathParam("vkRegionId") Long vkRegionId) {
        final VkLocation region = vkLocationDao.findByFieldSingle("vendorId", vkRegionId);
        final VkLocation country = vkLocationDao.findByFieldSingle("vendorId", region.getParentVendorId());
        final List<City> citiesByCountryAndRegion = vkClient.getCitiesByCountryAndRegion(country.getVendorId(), region.getVendorId());
        final List<VkLocation> vkLocations = vkLocationDao.save(citiesByCountryAndRegion, region, VkLocation::from);
        return  Response.of(vkLocations);
    }


}
