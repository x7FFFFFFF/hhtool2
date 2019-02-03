package ru.alex.vic.entities.hh;



import ru.alex.vic.entities.Location;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.json.hh.HHLocationJson;

import javax.persistence.Entity;


@Entity
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
