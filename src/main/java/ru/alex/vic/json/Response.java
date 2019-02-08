package ru.alex.vic.json;

import java.util.List;

public class Response<T> {
    private int count;
    private List<T> items;

    public Response() {
    }

    public Response(int count, List<T> items) {
        this.count = count;
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public static <T> Response<T> of(List<T> list){
        return new Response<>(list.size(), list);
    }

}
