package com.crazypug.datalineage.metadata;

import java.util.List;

public interface MetaDataProvider {

    Table getTable(String schema, String tableName);

    Column getColumn(String schema, String tableName, String columnName);

    List<Column> getColumns(String schema, String tableName);

    List<Table> getTables(String schema);

    Schemata getSchemata(String schema);

    List<Schemata> getAllSchema();

    void reload();

    void setMetaDataLoader(MetaDataLoader metaDataLoader);

    void clearContext();

    Context getContext();
}
