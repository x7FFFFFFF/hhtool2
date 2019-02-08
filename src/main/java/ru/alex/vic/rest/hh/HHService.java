package ru.alex.vic.rest.hh;

import com.google.inject.persist.Transactional;
import ru.alex.vic.client.hh.HHClient;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.json.Response;
import ru.alex.vic.json.hh.HHLocationJson;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.function.Consumer;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Singleton
@Path("hh")
public class HHService {


    private final Dao<Long, HHLocation> dao;
    private final HHClient hhClient;

    @Inject
    public HHService(Dao<Long, HHLocation> dao, HHClient hhClient) {
        this.dao = dao;
        this.hhClient = hhClient;
    }

    /**  Другие страны  1001 ? TODO: поправить locationType, дочергими элементами "Другие страны" являются страны, но сейчас им ставится locationType = REGION
     *
     *
     */
    @GET
    @Path("loadLocations")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response<HHLocationJson> loadLocations() {
        this.dao.deleteAll();
        final HHLocationJson[] locations = hhClient.getLocations();
        iterate(locations, this.dao::save);
        return Response.of(locations);
    }


    private static void iterate(HHLocationJson[] hhLocationJsons, Consumer<HHLocation> consumer) {
        for (HHLocationJson hhLocationJson : hhLocationJsons) {
            consumer.accept(new HHLocation(hhLocationJson));
            iterate(hhLocationJson.getAreas(), consumer);
        }
    }
}
