package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Estate {

    private int id = -1;
    private int zip;
    private int number;

    private String city;
    private String street;
    private String area;
    private String fk_agent;

    public int getId() {
        return id;
    }

    public int getzip() {
        return zip;
    }

    public int getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public String getFk_agent() {
        return fk_agent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setFk_agent(String fk_agent) {
        this.fk_agent = fk_agent;
    }
}
