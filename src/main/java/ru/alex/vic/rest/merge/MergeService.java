package ru.alex.vic.rest.merge;


import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.merge.MergeVk;
import ru.alex.vic.entities.vk.VkLocation;
import ru.alex.vic.json.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;


@Singleton
@Path("merge")
@Produces(MediaType.APPLICATION_JSON)
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
    public String clearMergeTable() {
        final List<MergeVk> mergeVks = mergeDao.getAll();
        for (MergeVk mergeVk : mergeVks) {
            final List<VkLocation> vkLocations = mergeVk.getVkLocations();
            //vkLocationDao.update(mergeVk.getVkLocations(), loc->loc.s);
        }

        return "OK";
    }

    //mapLocation
    @POST
    @Path("mapLocation/{mergeId}")
    public String mapLocation(@PathParam("mergeId") Long mergeId, @FormParam("id") Long vkId) {
        if (vkId != 0) {
            final MergeVk mergeVk = mergeDao.findById(mergeId);
            final List<VkLocation> vkLocations = mergeVk.getVkLocations();
            final VkLocation vkLocation = vkLocations.stream()
                    .filter(vk -> vk.getId().equals(vkId))
                    .findAny().orElseThrow(IllegalArgumentException::new);

            vkLocations.clear();
            vkLocations.add(vkLocation);
            mergeDao.update(mergeVk);
            return "OK";
        }


        return "FAIL";
    }


    @GET
    @Path("mergeCountries")    //2019 Московская область  //Россия 113
    public Response<MergeVk> mergeCountries() {
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.COUNTRY);
        final List<HHLocation> hhRegions = hhLocationDao.findByFields(params);
        List<MergeVk> res = new ArrayList<>();
        for (HHLocation hhRegion : hhRegions) {
            res.add(mergeCountry(hhRegion));
        }

        return Response.of(res);

    }

    private MergeVk mergeCountry(HHLocation hhCountry) {
        clear(hhCountry);
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.COUNTRY);
        params.put("name", hhCountry.getName());
        final List<VkLocation> vkCountries = vkLocationDao.findByFields(params);
        return getMergeVk(hhCountry, vkCountries, true);
    }

    private MergeVk getMergeVk(HHLocation hhLocation, List<VkLocation> vkLocations, boolean exact) {
        final boolean hasOneVariant = vkLocations.size() == 1;
        MergeVk mergeVk = createMergeVk(hhLocation, vkLocations, hasOneVariant);
        mergeDao.save(mergeVk);
        if (hasOneVariant && exact) {
            hhLocation.setResolved(true);
            hhLocationDao.save(hhLocation);
        }
        return mergeVk;
    }

    private void clear(HHLocation location) {
        final List<MergeVk> locations = mergeDao.findByField("hhLocation", location);
        mergeDao.delete(locations);
    }


    @GET
    @Path("mergeRegions")    //2019 Московская область  //Россия 113
    public Response<MergeVk> mergeRegions(@QueryParam("id") Integer hhCountryCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("locationType", LocationType.REGION);
        params.put("parentVendorId", hhCountryCode);
        final List<HHLocation> hhRegions = hhLocationDao.findByFields(params);
        List<MergeVk> res = new ArrayList<>();
        for (HHLocation hhRegion : hhRegions) {
            res.add(merge(hhRegion));
        }



      /*  vkLocationDao.update(vkLocationList, loc -> loc.setResolved(true));
        if (hasOneVariant && hhRegion.getHasChilds()) {
            final List<HHLocation> cities = hhLocationDao.findByField("parentVendorId", hhRegion.getVendorId());
            for (HHLocation city : cities) {
                mergeCity(city, vkLocationList.get(0));
            }
        }*/
        return Response.of(res);
    }

    private MergeVk merge(HHLocation hhRegion) {
        clear(hhRegion);
        String region = replaceRepublic(hhRegion.getName());
        boolean exact = true;
        final List<VkLocation> vkLocationList = vkLocationDao.findByField("name", region);
        if (vkLocationList.isEmpty()) {
            vkLocationList.addAll(vkLocationDao.findByFieldLike("name", "%" + region + "%"));
            exact = false;
        }
        return getMergeVk(hhRegion, vkLocationList, exact);
    }

    String replaceRepublic(String name) {
        final String prefix = "Республика";
        return (name.startsWith(prefix)) ? name.substring(prefix.length()).trim() : name.trim();
    }

    private MergeVk createMergeVk(HHLocation hhLocation, List<VkLocation> vkLocationList, boolean hasOneVariant) {
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

/*    private void mergeCity(HHLocation hhCity, VkLocation vkRegion) {
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

    }*/


}
