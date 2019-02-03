package ru.alex.vic.rest.vk;

import com.google.inject.persist.Transactional;
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

import static javax.ws.rs.core.MediaType.TEXT_HTML;

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
    @Path("loadLocations")
    @Produces(TEXT_HTML)
    @Transactional
    public String loadLocations() {
        //final String[] codes = {"RU", "UA", "KZ", "AZ", "BY", "GE", "KG", "UZ"};
        final TaskStatus task = service.startTask(LOAD_LOCATIONS);
        if (task.isRunning()) {
            return task.toString();
        }



        vkLocationDao.deleteAll();
        final String[] codes = {"RU"};
        final GetCountriesResponse countriesByCode = vkClient.getCountriesByCode(codes);
        for (Country country : countriesByCode.getItems()) {
            vkLocationDao.save(new VkLocation(country));
        }

        for (Country country : countriesByCode.getItems()) {
            final GetRegionsResponse regionsByCountryCode = vkClient.getRegionsByCountryCode(country.getId());
            for (Region region : regionsByCountryCode.getItems()) {
                vkLocationDao.save(new VkLocation(region, country));
            }
            final Integer regionsTotal = regionsByCountryCode.getCount();
            int count = 0;
            for (Region region : regionsByCountryCode.getItems()) {
                System.out.println("region = " + region);
                final GetCitiesResponse citiesByCountryAndRegion = vkClient.getCitiesByCountryAndRegion(country.getId(), region.getId());
                sleep(timeout);
                if (citiesByCountryAndRegion.getCount() == 1000) {
                    System.out.println("Limit! region = " + region);
                }
                for (City city : citiesByCountryAndRegion.getItems()) {
                    sleep(timeout);
                    vkLocationDao.save(new VkLocation(city, region));
                }

                count++;
                task.setComplete(count / regionsTotal * 100);
            }
        }
        task.stop();
        return task.toString();
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
