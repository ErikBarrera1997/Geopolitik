package com.msservices.geopolitik.connection.Data;

public class Country {

    private int idCountry;
    private String name;
    private String government;
    private String religion;
    private String ethnicGroup;
    private String description;

    public Country(int idCountry, String name, String government, String religion, String ethnicGroup, String description) {
        this.idCountry = idCountry;
        this.name = name;
        this.government = government;
        this.religion = religion;
        this.ethnicGroup = ethnicGroup;
        this.description = description;
    }

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGovernment() {
        return government;
    }

    public void setGovernment(String government) {
        this.government = government;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getEthnicGroup() {
        return ethnicGroup;
    }

    public void setEthnicGroup(String ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Country{" +
                "idCountry=" + idCountry +
                ", name='" + name + '\'' +
                ", government='" + government + '\'' +
                ", religion='" + religion + '\'' +
                ", ethnicGroup='" + ethnicGroup + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
