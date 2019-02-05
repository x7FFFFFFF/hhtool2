package ru.alex.vic.entities.merge;


import ru.alex.vic.entities.BaseEntity;
import ru.alex.vic.entities.LocationType;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.vk.VkLocation;

import javax.persistence.*;
import java.util.List;

@Entity
public class MergeVk extends BaseEntity {


    public MergeVk() {
    }

    @Column(name = "locationtype", length = 10)
    @Enumerated(EnumType.STRING)
    private LocationType locationType;



    @OneToOne
    @JoinColumn(name = "hhLocation_id", referencedColumnName = "id")
    private HHLocation hhLocation;


    @OneToMany
    //@JoinColumn(name = "mergeVk_id", referencedColumnName = "id")
    @JoinTable(
            name = "vkLoc_merge",
            joinColumns = @JoinColumn(
                    name = "vk_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "merge_id", referencedColumnName = "id"))
    private List<VkLocation> vkLocations;

    @Column
    private boolean resolved;


    @Column
    private int distance;

    public HHLocation getHhLocation() {
        return hhLocation;
    }

    public void setHhLocation(HHLocation hhLocation) {
        this.hhLocation = hhLocation;
    }

    public List<VkLocation> getVkLocations() {
        return vkLocations;
    }

    public void setVkLocations(List<VkLocation> vkLocations) {
        this.vkLocations = vkLocations;
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

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }
}
