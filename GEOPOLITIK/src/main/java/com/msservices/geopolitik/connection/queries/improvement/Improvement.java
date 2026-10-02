package com.msservices.geopolitik.connection.queries.improvement;

public record Improvement(
        int idImprovement,
        String name,
        Double productionValue,
        Double price,
        String type,
        String description,
        int duration
) {
}