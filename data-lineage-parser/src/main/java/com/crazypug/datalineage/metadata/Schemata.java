package com.crazypug.datalineage.metadata;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Schemata {
    private String schema;


    private Map<String, Table> tables = new LinkedHashMap<>();


    public void addTable(Table table) {
        tables.put(table.getTableName(), table);
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }



    public List<Table> getTables() {
        return tables.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }


    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }


}


