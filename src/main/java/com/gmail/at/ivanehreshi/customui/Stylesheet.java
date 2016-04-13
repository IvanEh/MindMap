package com.gmail.at.ivanehreshi.customui;

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

public class Stylesheet{
    private Map<String, Object> properties;

    public Stylesheet() {
        properties = new HashMap<>();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object get(String prop) {
        return properties.get(prop);
    }

    public Object put(String prop, Object val) {
        return properties.put(prop, val);
    }

    public Integer getInteger(String prop) {
        Object o = get(prop);
        if(o instanceof Integer) {
            return (Integer) o;
        }

        return null;
    }
   public Color getColor(String prop) {
        Object o = get(prop);
        if(o instanceof Color) {
            return (Color) o;
        }

        return null;
    }

   public String getString(String prop) {
    Object o = get(prop);
    if(o instanceof String) {
        return (String) o;
    }

    return null;
    }
}
