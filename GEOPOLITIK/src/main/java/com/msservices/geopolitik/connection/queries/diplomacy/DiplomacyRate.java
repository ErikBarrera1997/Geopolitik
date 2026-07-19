package com.msservices.geopolitik.connection.queries.diplomacy;

public record DiplomacyRate(int idCountry, String countryName, int neighborId, String neighborName, double morale, double friendshipRate) {
}
