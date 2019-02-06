package ru.alex.vic.rest.merge;


import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.merge.MergeVk;
import ru.alex.vic.entities.vk.VkLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;


@Singleton
@Path("merge")
public class MergeService {

    private final Dao<Long, VkLocation> vkLocationDao;
    private final Dao<Long, HHLocation> hhLocationDao;
    private final Dao<Long, MergeVk> mergeDao;

    @Inject
    public MergeService(Dao<Long, VkLocation> vkLocationDao,
                        Dao<Long, HHLocation> hhLocationDao,
                        Dao<Long, MergeVk> mergeDao) {
        this.vkLocationDao = vkLocationDao;
        this.hhLocationDao = hhLocationDao;
        this.mergeDao = mergeDao;
    }


    @GET
    @Path("clearMergeTable")
    @Produces(MediaType.APPLICATION_JSON)
    public String clearMergeTable() {
        final List<MergeVk> mergeVks = mergeDao.getAll();
        for (MergeVk mergeVk : mergeVks) {
            final List<VkLocation> vkLocations = mergeVk.getVkLocations();
            //vkLocationDao.update(mergeVk.getVkLocations(), loc->loc.s);
        }

        return "OK";
    }


    @GET
    @Path("mergeCountries")
    @Produces(MediaType.APPLICATION_JSON) //2019 Московская область  //Россия 113
    public List<HHLocation> mergeCountries() {
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.COUNTRY);
        final List<HHLocation> hhRegions = hhLocationDao.findByFields(params);
        for (HHLocation hhRegion : hhRegions) {
            mergeCountry(hhRegion);
        }

        return hhRegions;

    }

    private void mergeCountry(HHLocation hhCountry) {
        clear(hhCountry);
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.COUNTRY);
        params.put("name", hhCountry.getName());
        final List<VkLocation> vkCountries = vkLocationDao.findByFields(params);
        final boolean hasOneVariant = vkCountries.size() == 1;
        MergeVk mergeVk = createMergeVk(hhCountry, vkCountries, hasOneVariant);
        mergeDao.save(mergeVk);
        if (hasOneVariant) {
            hhCountry.setResolved(true);
            hhLocationDao.save(hhCountry);
        }
    }

    private void clear(HHLocation location) {
        final List<MergeVk> locations = mergeDao.findByField("hhLocation", location);
        mergeDao.delete(locations);
    }


    @GET
    @Path("mergeRegions")
    @Produces(MediaType.APPLICATION_JSON) //2019 Московская область  //Россия 113
    public List<HHLocation> mergeRegions(@QueryParam("id") Integer hhCountryCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.REGION);
        params.put("parentVendorId", hhCountryCode);
        final List<HHLocation> hhRegions = hhLocationDao.findByFields(params);
        for (HHLocation hhRegion : hhRegions) {
            merge(hhRegion);
        }



      /*  vkLocationDao.update(vkLocationList, loc -> loc.setResolved(true));
        if (hasOneVariant && hhRegion.getHasChilds()) {
            final List<HHLocation> cities = hhLocationDao.findByField("parentVendorId", hhRegion.getVendorId());
            for (HHLocation city : cities) {
                mergeCity(city, vkLocationList.get(0));
            }
        }*/
        return hhRegions;
    }

    private void merge(HHLocation hhRegion) {
        clear(hhRegion);
        final List<VkLocation> vkLocationList = vkLocationDao.findByField("name", replaceRepublic(hhRegion.getName()));
        final boolean hasOneVariant = vkLocationList.size() == 1;
        MergeVk mergeVk = createMergeVk(hhRegion, vkLocationList, hasOneVariant);
        mergeDao.save(mergeVk);

        if (hasOneVariant) {
            hhRegion.setResolved(true);
            hhLocationDao.save(hhRegion);
        }
    }

    String replaceRepublic(String name) {
        final String prefix = "Республика";
        return (name.startsWith(prefix)) ? name.substring(prefix.length()).trim() : name.trim();
    }

    public MergeVk createMergeVk(HHLocation hhLocation, List<VkLocation> vkLocationList, boolean hasOneVariant) {
        MergeVk mergeVk = new MergeVk();
        mergeVk.setHhLocation(hhLocation);
        mergeVk.setLocationType(hhLocation.getLocationType());
        mergeVk.setVkLocations(vkLocationList);
        if (hasOneVariant) {
            mergeVk.setResolved(true);
        }
        mergeVk.setDistance(100);
        return mergeVk;
    }

    private void mergeCity(HHLocation hhCity, VkLocation vkRegion) {
        clear(hhCity);
        Map<String, Object> params = new HashMap<>();
        params.put("parentVendorId", vkRegion.getVendorId());
        params.put("name", hhCity.getName());
        final List<VkLocation> vkCities = vkLocationDao.findByFields(params);
        final boolean hasOneVariant = vkCities.size() == 1;
        MergeVk mergeVk = createMergeVk(hhCity, vkCities, hasOneVariant);
        mergeDao.save(mergeVk);
        if (hasOneVariant) {
            hhCity.setResolved(true);
            hhLocationDao.save(hhCity);
        }
        vkLocationDao.update(vkCities, loc -> loc.setResolved(true));

    }


}
