package ru.alex.vic.entities.hh;



import ru.alex.vic.entities.Location;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.json.hh.HHLocationJson;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


@Entity
@Table(name = "hhlocation", indexes = {
        @Index(name = "index_hhlocation_name", columnList = "name"),
        @Index(name = "index_hhlocation_parentvendorid", columnList = "parentvendorid"),
        @Index(name = "index_hhlocation_vendorid", columnList = "vendorid"),
        @Index(name = "index_hhlocation_locationtype", columnList = "locationtype")
})
public class HHLocation extends Location {
    public HHLocation() {
    }

    public HHLocation(HHLocationJson json) {
        this.setVendorId(json.getId());
        this.setName(parenthesesRemove(json.getName()));
        this.setParentVendorId(json.getParentId());
        this.setHasChilds(json.isHasChilds());
        if (json.getParentId() == null) {
            this.setLocationType(LocationType.COUNTRY);
        } else if (json.isHasChilds()) {
            this.setLocationType(LocationType.REGION);
        } else {
            this.setLocationType(LocationType.CITY);
        }
    }




}
