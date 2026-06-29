package com.mycompany.manager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseManager<T> {
    protected List<T> items = new ArrayList<>();

    public BaseManager() {}

    public List<T> getAll() {
        return new ArrayList<>(this.items);
    }

    public void addLoadedItem(T item) {
        this.items.add(item);
    }
}
