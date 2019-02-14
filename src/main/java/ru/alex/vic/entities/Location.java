package ru.alex.vic.entities;


import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Location extends BaseEntity {


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


    @Column(name = "resolved")
    private boolean resolved;

    public Location() {
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

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }


    @Override
    public String toString() {
        return "Location{" +
                "id=" + getId() +
                ", modified=" + getModified() +
                ", name='" + name + '\'' +
                ", hasChilds=" + hasChilds +
                ", parentVendorId=" + parentVendorId +
                ", vendorId=" + vendorId +
                ", locationType=" + locationType +
                ", resolved=" + resolved +
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
