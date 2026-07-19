package com.msservices.geopolitik.connection.queries.diplomacy;

public record CountryDiplomacyStatus(int idCountry, String countryName, int alliedId, String alliedName, int enemyId, String enemyName, int peaceId, String peaceName, int warId, String warName, int neutralId, String neutralName, int vassalId, String vassalName, int tradeId, String tradeName, int militaryId, String militaryName) {
}
