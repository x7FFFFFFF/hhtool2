package ru.alex.vic.entities.merge;


import ru.alex.vic.entities.BaseEntity;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.vk.VkLocation;

import javax.persistence.*;
import java.util.List;

@Entity
/*@NamedQueries({
        @NamedQuery(name = "getHHLocation", query = "SELECT h FROM HHLocation h WHERE h.id LIKE :hh ")
})*/
public class MergeVk extends BaseEntity {


    public MergeVk() {
    }

    @Column(name = "locationtype", length = 10)
    @Enumerated(EnumType.STRING)
    private LocationType locationType;



    @OneToOne
    @JoinColumn(name = "hh_id")
    private HHLocation hhLocation;



    @ManyToOne
    @JoinColumn(name = "vk_id")
    private VkLocation vkLocation;


    @Column
    private boolean resolved;


    @Column
    private int distance;

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public HHLocation getHhLocation() {
        return hhLocation;
    }

    public void setHhLocation(HHLocation hhLocation) {
        this.hhLocation = hhLocation;
    }

    public VkLocation getVkLocation() {
        return vkLocation;
    }

    public void setVkLocation(VkLocation vkLocation) {
        this.vkLocation = vkLocation;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
