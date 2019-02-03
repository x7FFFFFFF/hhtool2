package ru.alex.vic.entities.vk;

import ru.alex.vic.entities.Location;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.json.vk.City;
import ru.alex.vic.json.vk.Country;
import ru.alex.vic.json.vk.Region;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "vklocation",indexes = {
        @Index(name = "index_vklocation_name", columnList = "name"),
        @Index(name = "index_vklocation_parentvendorid", columnList = "parentvendorid"),
        @Index(name = "index_vklocation_vendorid", columnList = "vendorid"),
        @Index(name = "index_vklocation_locationtype", columnList = "locationtype")
})
public class VkLocation extends Location {

    public VkLocation() {
    }

    public VkLocation(Country json) {
        this.setVendorId(json.getId().longValue());
        this.setName(parenthesesRemove(json.getTitle()));
        this.setHasChilds(true);
        this.setLocationType(LocationType.COUNTRY);
    }

    public VkLocation(Region json, Country country) {
        this.setVendorId(json.getId().longValue());
        this.setName(parenthesesRemove(json.getTitle()));
        this.setHasChilds(true);
        this.setLocationType(LocationType.REGION);
        this.setParentVendorId(country.getId().longValue());
    }

    public VkLocation(City json, Region region) {
        this.setVendorId(json.getId().longValue());
        this.setName(parenthesesRemove(json.getTitle()));
        this.setHasChilds(false);
        this.setLocationType(LocationType.CITY);
        this.setParentVendorId(region.getId().longValue());
    }

}