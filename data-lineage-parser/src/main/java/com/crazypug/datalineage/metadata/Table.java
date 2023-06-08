package com.crazypug.datalineage.metadata;

import java.util.*;
import java.util.stream.Collectors;

public class Table {


    private String schema;
    private String tableName;
    private Map<String, Column> columns = new LinkedHashMap<>();

    private String tableComment;

    public String getTableComment() {
        return tableComment;
    }

    public Table(String schema, String tableName,String tableComment) {
        this.schema = schema;
        this.tableName = tableName;
        this.tableComment=tableComment;
    }

    public String getSchema() {
        return schema;
    }

    public void addColumn(Column column) {
        columns.put(column.getColumnName(), column);
    }

    public void addColumns(List<Column> column) {
        column.stream()
                .sorted(Comparator.comparing(Column::getOrdinalPosition))
                .forEach(it -> columns.put(it.getColumnName(), it));
    }

    public List<Column> getColumns() {
        return columns.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Column getColumn(String columnName) {
        return columns.get(columnName);
    }

    public String getTableName() {
        return tableName;
    }


    @Override
    public String toString() {

        return String.format("create table %s.%s (\n%s)", schema, tableName, columns
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map(String::valueOf)
                .collect(Collectors
                        .joining(",\n")));
    }
}
