package com.splider.rule;

import java.util.List;
import java.util.Map;

public class ContentPage extends PageRule{

    private List<Entity> entities;

    private Map<String,String> meta;


    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void add(Entity e){
        entities.add(e);
    }
}
