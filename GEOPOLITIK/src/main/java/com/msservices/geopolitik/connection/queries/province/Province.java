package com.msservices.geopolitik.connection.queries.province;

public record Province(int idProvince, String name, String environment, int population, double area, boolean isCapital, boolean isCoastal, int idCountry, int morale) {
}
