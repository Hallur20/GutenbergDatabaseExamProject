/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg;

/**
 *
 * @author hallur
 */
public class City {
private int id;
private String cityName;
private Double latitude;
private Double longitude;
private int population;
private String countryCode;
private String continent;

    public City(int id, String cityName, Double latitude, Double longitude, int population, String countryCode, String continent) {
        this.id = id;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
        this.countryCode = countryCode;
        this.continent = continent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityname) {
        this.cityName = cityname;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "City{" + "id=" + id + ", cityname=" + cityName + ", latitude=" + latitude + ", longitude=" + longitude + ", population=" + population + ", countryCode=" + countryCode + ", continent=" + continent + '}';
    }
    
    
    
}
