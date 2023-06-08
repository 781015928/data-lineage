package com.crazypug.datalineage.metadata;

public class Column {

    private String schema;
    private String tableName;

    private String tableComment;
    private String columnName;
    private String dataType;
    private long ordinalPosition;
    private long numericPrecision;
    private long numericScale;
    private String columnComment;
    private long columnSize;

    public Column(String schema, String tableName,String tableComment, String columnName, String dataType, long ordinalPosition, long numericPrecision, long numericScale, String columnComment, long columnSize) {
        this.schema = schema;
        this.tableName = tableName;
        this.tableComment=tableComment;
        this.columnName = columnName;
        this.dataType = dataType;
        this.ordinalPosition = ordinalPosition;
        this.numericPrecision = numericPrecision;
        this.numericScale = numericScale;
        this.columnComment = columnComment;
        this.columnSize = columnSize;
    }


    public Column(String columnName, String dataType, long ordinalPosition, long numericPrecision, long numericScale, String columnComment, long columnSize) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.ordinalPosition = ordinalPosition;
        this.numericPrecision = numericPrecision;
        this.numericScale = numericScale;
        this.columnComment = columnComment;
        this.columnSize = columnSize;
    }

    public Column(String columnName, String dataType, long ordinalPosition, String columnComment, long columnSize) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.ordinalPosition = ordinalPosition;
        this.numericPrecision = -1;
        this.numericScale = -1;
        this.columnComment = columnComment;
        this.columnSize = columnSize;
    }

    public Column(String columnName, String dataType, long ordinalPosition, String columnComment) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.ordinalPosition = ordinalPosition;
        this.numericPrecision = -1;
        this.numericScale = -1;
        this.columnComment = columnComment;
        this.columnSize = -1;
    }

    public String getColumnName() {
        return columnName;
    }

    public long getNumericPrecision() {
        return numericPrecision;
    }

    public long getNumericScale() {
        return numericScale;
    }

    public long getOrdinalPosition() {
        return ordinalPosition;
    }

    public String getDataType() {
        return dataType;
    }

    public long getColumnSize() {
        return columnSize;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableComment() {
        return tableComment;
    }

    @Override
    public String toString() {

        if (numericScale > 0) {
            return String.format("%s %s(%d,%d) comment '%s'", columnName, dataType, numericPrecision, numericScale, columnComment);
        }
        if (numericScale == 0 && numericPrecision > 0) {
            return String.format("%s %s(%d) comment '%s'", columnName, dataType, numericPrecision, columnComment);
        }
        if (columnSize > 0) {
            return String.format("%s %s(%d) comment '%s'", columnName, dataType, columnSize, columnComment);
        }
        return String.format("%s %s comment '%s'", columnName, dataType, columnComment);
    }
}
