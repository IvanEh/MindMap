package com.gmail.at.ivanehreshi.customui;

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

public class Stylesheet{
    private Stylesheet parent;

    private Map<String, Object> properties;

    public Stylesheet() {
        this(null);
    }

    public Stylesheet(Stylesheet parent) {
        properties = new HashMap<>();
        this.parent = parent;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object get(String prop) {
        Object property;
        property = properties.get(prop);

        if(property == null) {
            property = parent.get(prop);
        }

        return property;
    }

    public Object put(String prop, Object val) {
        return properties.put(prop, val);
    }

    public Object toggle(String prop) {
        Object prevVal = properties.get(prop);
        if(prevVal == null)
            return null;

        if(prevVal instanceof Boolean) {
            put(prop, !(boolean) prevVal);
        }

        return prevVal;
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
