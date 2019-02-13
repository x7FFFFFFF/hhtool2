package ru.alex.vic.entities.merge;

import ru.alex.vic.entities.Location;

import javax.persistence.Embeddable;

@Embeddable
public class Reference {
    Long id;
    String name;

    public Reference() {
    }

    public static Reference from(Location location) {
        if (location == null) {
            return new Reference(null, null);
        }
        return new Reference(location.getId(), location.getName());
    }

    public Reference(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
