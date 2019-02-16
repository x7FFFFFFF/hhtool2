package ru.alex.vic.entities.vk;

import ru.alex.vic.entities.Location;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.json.vk.City;
import ru.alex.vic.json.vk.Country;
import ru.alex.vic.json.vk.Region;

import javax.persistence.*;

@Entity
@Table(name = "vklocation", indexes = {
        @Index(name = "index_vklocation_name", columnList = "name"),
        @Index(name = "index_vklocation_parentvendorid", columnList = "parentvendorid"),
        @Index(name = "index_vklocation_vendorid", columnList = "vendorid"),
        @Index(name = "index_vklocation_locationtype", columnList = "locationtype")
})
public class VkLocation extends Location {

    public VkLocation() {
    }

    @Column
    private String area;

    @Column
    private String region;


    public static VkLocation from(Country json) {
        VkLocation vkLocation = new VkLocation();
        vkLocation.setVendorId(json.getId().longValue());
        vkLocation.setName(parenthesesRemove(json.getTitle()));
        vkLocation.setHasChilds(true);
        vkLocation.setLocationType(LocationType.COUNTRY);
        return vkLocation;
    }

    public static VkLocation from(Country parent, Region json) {
        VkLocation vkLocation = new VkLocation();
        vkLocation.setVendorId(json.getId().longValue());
        vkLocation.setName(parenthesesRemove(json.getTitle()));
        vkLocation.setHasChilds(true);
        vkLocation.setLocationType(LocationType.REGION);
        vkLocation.setParentVendorId(parent.getId().longValue());
        return vkLocation;
    }

    public static VkLocation from(VkLocation parent, City json) {
        VkLocation vkLocation = new VkLocation();
        vkLocation.setVendorId(json.getId().longValue());
        vkLocation.setName(parenthesesRemove(json.getTitle()));
        vkLocation.setHasChilds(false);
        vkLocation.setLocationType(LocationType.CITY);
        vkLocation.setParentVendorId(parent.getVendorId());
        vkLocation.setRegion(json.getRegion());
        vkLocation.setArea(json.getArea());
        return vkLocation;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
