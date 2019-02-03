package ru.alex.vic.entities;


import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class Location {


    @Id
    @GeneratedValue
    private Long id;

    @UpdateTimestamp
    private LocalDateTime modified;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean hasChilds;


    @Column(name = "parentvendorid")
    private Long parentVendorId;

    @Column(name = "vendorid", nullable = false)
    private Long vendorId;

    @Column(name = "locationtype", length = 10)
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    public Location() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentVendorId() {
        return parentVendorId;
    }

    public void setParentVendorId(Long parentVendorId) {
        this.parentVendorId = parentVendorId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Boolean getHasChilds() {
        return hasChilds;
    }

    public void setHasChilds(Boolean hasChilds) {
        this.hasChilds = hasChilds;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", hasChilds=" + hasChilds +
                ", parentVendorId=" + parentVendorId +
                ", vendorId=" + vendorId +
                ", locationType=" + locationType +
                '}';
    }

    protected static String parenthesesRemove(String name) {
        final int start = name.indexOf('(');
        if (start != -1) {
            return name.substring(0, start).trim();
        }
        return name;
    }
}
