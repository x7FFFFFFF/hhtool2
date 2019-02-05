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
    @Produces(TEXT_HTML)
    public String  clearMergeTable(){
        final List<MergeVk> mergeVks = mergeDao.getAll();
        for (MergeVk mergeVk : mergeVks) {
            final List<VkLocation> vkLocations = mergeVk.getVkLocations();
            //vkLocationDao.update(mergeVk.getVkLocations(), loc->loc.s);
        }


        return "OK";
    }





    @GET
    @Path("mergeCountries")
    @Produces({TEXT_HTML}) //2019 Московская область  //Россия 113
    public String mergeCountries() {
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.COUNTRY);
        final List<HHLocation> hhRegions = hhLocationDao.findByFields(params);
        StringBuilder builder = new StringBuilder();
        for (HHLocation hhRegion : hhRegions) {
            builder.append(mergeCountry(hhRegion));
        }

        return builder.toString();

    }

    private String mergeCountry(HHLocation hhCountry) {
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
            return "Success:" + hhCountry +"\n";
        } else {
            return "Fail:" + hhCountry + "; variants found = " + vkCountries.size()+"\n";
        }
    }

    private void clear(HHLocation location) {
        final List<MergeVk> locations = mergeDao.findByField("hhLocation", location);
        mergeDao.delete(locations);
    }


    @GET
    @Path("mergeRegions")
    @Produces(TEXT_HTML) //2019 Московская область  //Россия 113
    public String mergeRegions(@QueryParam("id") Integer hhCountryCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.REGION);
        params.put("parentVendorId", hhCountryCode);
        final List<HHLocation> hhRegions = hhLocationDao.findByFields(params);
        StringBuilder builder = new StringBuilder();
        for (HHLocation hhRegion : hhRegions) {
            builder.append(merge(hhRegion));
        }



      /*  vkLocationDao.update(vkLocationList, loc -> loc.setResolved(true));
        if (hasOneVariant && hhRegion.getHasChilds()) {
            final List<HHLocation> cities = hhLocationDao.findByField("parentVendorId", hhRegion.getVendorId());
            for (HHLocation city : cities) {
                mergeCity(city, vkLocationList.get(0));
            }
        }*/
        return builder.toString();
    }

    private String merge(HHLocation hhRegion) {
        clear(hhRegion);
        final List<VkLocation> vkLocationList = vkLocationDao.findByField("name", hhRegion.getName());
        final boolean hasOneVariant = vkLocationList.size() == 1;
        MergeVk mergeVk = createMergeVk(hhRegion, vkLocationList, hasOneVariant);
        mergeDao.save(mergeVk);

        if (hasOneVariant) {
            hhRegion.setResolved(true);
            hhLocationDao.save(hhRegion);
            return "Success:" + hhRegion;
        } else {
            return "Fail:" + hhRegion + "; variants found = " + vkLocationList.size();
        }
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
