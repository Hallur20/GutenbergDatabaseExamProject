/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.HashMap;

/**
 *
 * @author hallur
 */
public class JsonBook {
    private int id;
    private HashMap<String, Integer> cityQuantity;

    public JsonBook(int id, HashMap<String, Integer> cityQuantity) {
        this.id = id;
        this.cityQuantity = cityQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, Integer> getCityQuantity() {
        return cityQuantity;
    }

    public void setCityQuantity(HashMap<String, Integer> cityQuantity) {
        this.cityQuantity = cityQuantity;
    }

    @Override
    public String toString() {
        return "JsonBook{" + "id=" + id + ", cityQuantity=" + cityQuantity + '}';
    }
    
    
}
