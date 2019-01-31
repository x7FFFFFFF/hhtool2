package ru.alex.vic.rest.hh;

import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.entities.hh.HHLocation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path( "hh" )
public class HHService {


    private final Dao<Long, HHLocation> dao;

    @Inject
    public HHService( Dao<Long, HHLocation> dao ) {
        this.dao = dao;
    }


    @GET
    @Path("loadLocations")
    @Produces( TEXT_HTML )
    public String loadLocations() {
        HHLocation hhLocation = new HHLocation();
        hhLocation.setId(1L);
        hhLocation.setHasChilds(true);
        hhLocation.setLocationType(LocationType.COUNTRY);
        hhLocation.setName("name");
        hhLocation.setVendorId(1111L);
        this.dao.save(hhLocation);


        System.out.println( "Requested to get All" );
        String html = "<h2>All stuff</h2><ul>";

        html += "</ul>";
        return html;
    }
}
