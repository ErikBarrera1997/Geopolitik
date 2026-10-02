package com.msservices.geopolitik.functions.improvementsFunctions;

import com.msservices.geopolitik.connection.queries.improvement.Improvement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Rows per country
 */
public sealed class investigations permits countryInvestigations {

    //ALl rows
    private final Map<Integer, Row> rows = new HashMap<>();

    public void add(int index, String name, int value) {
        if (rows.containsKey(index)) {
            return;
        }
        rows.put(index, new Row(index, name, value));
    }

    /*
    Saves a reference to the selected improvement, the row keeps the whole improvement as value
    Returns false when the improvement was already saved
     */
    public boolean addImprovement(Improvement improvement) {
        if (improvement == null || rows.containsKey(improvement.idImprovement())) {
            return false;
        }
        rows.put(improvement.idImprovement(),
                new Row(improvement.idImprovement(), improvement.name(), improvement));
        return true;
    }

    public List<Improvement> getImprovements() {
        List<Improvement> improvements = new ArrayList<>();
        for (Row fila : rows.values()) {
            if (fila.getValue() instanceof Improvement improvement) {
                improvements.add(improvement);
            }
        }
        improvements.sort(Comparator.comparingInt(Improvement::idImprovement));
        return improvements;
    }

    public boolean contains(int index) {
        return rows.containsKey(index);
    }

    public void changeRow(int index, int newRow) {
        Row fila = rows.get(index);

        if (fila != null) {
            fila.setValue(newRow);
        }
    }

    public void removeRow(int index) {
        rows.remove(index);
    }

    public void showRows() {
        for (Row fila : rows.values()) {
            System.out.println(fila);
        }
    }

    public Row getRow(int index) {
        return rows.get(index);
    }

    public void changeCountValue(int index, int newValue){
        rows.get(index).setValue(newValue);
    }
}

class Row {
    private int index;
    private String name;
    private Object value;

    public Row(int index, String name, Object value) {
        this.index = index;
        this.name = name;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object valor) {
        this.value = valor;
    }

    @Override
    public String toString() {
        return "[" + index + "] [" + name + "] [" + value + "]";
    }
}