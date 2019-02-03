package ru.alex.vic.json.vk;

import java.util.List;

public interface VkResponse<T> {

    Integer getCount();

    List<T> getItems();
}
