package ru.alex.vic.rest.merge;


import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.vk.VkLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.List;

import static javax.ws.rs.core.MediaType.TEXT_HTML;


@Singleton
@Path("merge")
public class MergeService {

    private final Dao<Long, VkLocation> vkLocationDao;
    private final Dao<Long, HHLocation> hhLocationDao;

    @Inject
    public MergeService(Dao<Long, VkLocation> vkLocationDao, Dao<Long, HHLocation> hhLocationDao) {
        this.vkLocationDao = vkLocationDao;
        this.hhLocationDao = hhLocationDao;
    }


    @GET
    @Path("mergeRegion")
    @Produces(TEXT_HTML)
    public String mergeRegion(@QueryParam("id") long hhRegionId) {
        final List<HHLocation> hhRegions = hhLocationDao.findByField("vendorId", String.valueOf(hhRegionId));
        System.out.println("regions = " + hhRegions);
        return "OK";
    }


}
