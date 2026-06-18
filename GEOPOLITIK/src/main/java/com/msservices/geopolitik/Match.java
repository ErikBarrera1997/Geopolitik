package com.msservices.geopolitik;

import java.util.List;

public class Match {
    private String map;
    private List<String> rules;
    private String flag;
    private String leader;
    private String database;

    public String getMap() { return map; }
    public void setMap(String map) { this.map = map; }
    public String getMapStringPath() { return map; }
    public List<String> getRules() { return rules; }
    public void setRules(List<String> rules) { this.rules = rules; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
    public String getLeader() { return leader; }
    public void setLeader(String leader) { this.leader = leader; }
    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
}
