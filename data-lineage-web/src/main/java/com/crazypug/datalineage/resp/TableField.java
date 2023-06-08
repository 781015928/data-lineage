package com.crazypug.datalineage.resp;

import java.util.ArrayList;
import java.util.List;

public class TableField {

    private String dbName;
    private String tableName;
    private String columnName;

    private List<TableField> dependFields=new ArrayList<>();


    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<TableField> getDependFields() {
        return dependFields;
    }

    public void setDependFields(List<TableField> dependFields) {
        this.dependFields = dependFields;
    }
}
