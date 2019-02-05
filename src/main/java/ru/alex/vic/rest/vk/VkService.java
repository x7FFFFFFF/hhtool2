package ru.alex.vic.rest.vk;

import ru.alex.vic.client.vk.VkClient;
import ru.alex.vic.dao.vk.VkLocationDao;
import ru.alex.vic.entities.vk.VkLocation;
import ru.alex.vic.json.vk.*;
import ru.alex.vic.rest.Service;
import ru.alex.vic.rest.TaskStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static ru.alex.vic.Utils.sleep;

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
    @Produces(TEXT_HTML)
    /*@Transactional*/
    public String loadRegions(@QueryParam("id") String countryCode) {
        //final String[] codes = {"RU", "UA", "KZ", "AZ", "BY", "GE", "KG", "UZ"};
        final TaskStatus task = service.startTask(LOAD_LOCATIONS);
        if (task.isRunning()) {
            return task.toString();
        }
        task.start();


        vkLocationDao.deleteAll();
        //final String[] codes = {"RU"};
        final GetCountriesResponse countriesByCode = vkClient.getCountriesByCode(countryCode);
        for (Country country : countriesByCode.getItems()) {
            vkLocationDao.save(new VkLocation(country));
        }
        int count = 0;

        for (Country country : countriesByCode.getItems()) {
            final GetRegionsResponse regionsByCountryCode = vkClient.getRegionsByCountryCode(country.getId());
            //sleep(timeout);
            for (Region region : regionsByCountryCode.getItems()) {
                vkLocationDao.save(new VkLocation(region, country));
                count++;
                task.setComplete(count);
            }
       /*     //final Integer regionsTotal = regionsByCountryCode.getCount();

            for (Region region : regionsByCountryCode.getItems()) {
                System.out.println("region = " + region);
                final List<City> citiesByCountryAndRegion = vkClient.getCitiesByCountryAndRegion(country.getId(), region.getId());
                sleep(timeout);
                for (City city : citiesByCountryAndRegion) {
                    vkLocationDao.save(new VkLocation(city, region));
                }
                count++;
                task.setComplete(count);
            }*/

        }
        task.stop();
        return task.toString();
    }


}
