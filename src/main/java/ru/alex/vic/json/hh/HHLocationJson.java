package ru.alex.vic.json.hh;

import com.google.gson.annotations.SerializedName;


public class HHLocationJson {

    @SerializedName("name")
    String name;

    @SerializedName("id")
    private Long id;


    @SerializedName("parent_id")
    private Long parentId;

    @SerializedName("areas")
    private HHLocationJson[] areas;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isHasChilds() {
        return areas != null && areas.length > 0;
    }

    public HHLocationJson[] getAreas() {
        return areas;
    }

    public void setAreas(HHLocationJson[] areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "HHLocationJson{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", parentId=" + parentId +
                '}';
    }
}
