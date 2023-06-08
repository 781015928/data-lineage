package com.crazypug.datalineage.metadata;

import com.crazypug.datalineage.core.Bean;
import com.crazypug.datalineage.core.Inject;

import java.util.*;
import java.util.stream.Collectors;

@Bean
public class MetaDataProviderImpl implements MetaDataProvider {


    @Inject
    public MetaDataLoader metaDataLoader;

    private boolean isFirstLoad=false;

    private Map<String, Schemata> schematas = new LinkedHashMap<>();


    @Override
    public Table getTable(String schema, String tableName) {
        checkLoad();
        Schemata schemata = schematas.get(schema);
        if (schemata != null) {
            Table table = schemata.getTable(tableName);
            return table;

        }
        return null;
    }

    @Override
    public Column getColumn(String schema, String tableName, String columnName) {
        checkLoad();
        if (columnName != null) {
            columnName = columnName.replace('`', ' ').trim();
        }

        Schemata schemata = schematas.get(schema);
        if (schemata != null) {
            Table table = schemata.getTable(tableName);
            if (table != null) {
                return table.getColumn(columnName);
            }
        }
        return null;
    }

    @Override
    public List<Column> getColumns(String schema, String tableName) {
        checkLoad();
        Schemata schemata = schematas.get(schema);
        if (schemata != null) {
            Table table = schemata.getTable(tableName);
            if (table != null) {
                return table.getColumns();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<Table> getTables(String schema) {
        checkLoad();
        Schemata schemata = schematas.get(schema);
        if (schemata != null) {
            return schemata.getTables();
        }

        return new ArrayList<>();
    }

    @Override
    public Schemata getSchemata(String schema) {
        checkLoad();
        return schematas.get(schema);
    }

    @Override
    public List<Schemata> getAllSchema() {
        checkLoad();
        return schematas.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public void reload() {

        if (metaDataLoader!=null){

            List<Column> columns = metaDataLoader.loadMetaData();
            List<Table> tables = new ArrayList<>();
            Map<Tuple3<String, String,String>, List<Column>> tableTuples = columns
                    .stream()
                    .collect(Collectors.groupingBy(it -> new Tuple3<String, String,String>(it.getSchema(), it.getTableName(),it.getTableComment() )));

            for (Map.Entry<Tuple3<String, String,String>, List<Column>> entry : tableTuples.entrySet()) {
                Tuple3<String, String,String> dbtb = entry.getKey();
                Table table = new Table(dbtb.v1, dbtb.v2, dbtb.v3);
                table.addColumns(entry.getValue());
                tables.add(table);
            }
            Map<String, List<Table>> schemaTuples = tables
                    .stream()
                    .collect(Collectors.groupingBy(Table::getSchema));
            for (Map.Entry<String, List<Table>> entry : schemaTuples.entrySet()) {
                Schemata schemata = new Schemata();
                schemata.setSchema(entry.getKey());
                entry.getValue().stream().forEach(schemata::addTable);
                schematas.put(entry.getKey(), schemata);
            }
            isFirstLoad=true;
        }
    }

    private void checkLoad(){
        if (!isFirstLoad){
            reload();
        }
    }



    @Override
    public void setMetaDataLoader(MetaDataLoader metaDataLoader) {
        this.metaDataLoader = metaDataLoader;
    }

    @Override
    public void clearContext() {
        ContextHolder.clearContext();
    }

    @Override
    public Context getContext() {
        Context context = ContextHolder.getContext();
        if (context == null) {
            context = new Context();
            ContextHolder.setContext(context);
        }
        return context;
    }

    static class Tuple3<T1,T2,T3>{
        private T1 v1;
        private  T2 v2;
        private T3 v3;
        Tuple3(T1 t1,T2 t2 ,T3 t3 ){
            this.v1=t1;
            this.v2=t2;
            this.v3=t3;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
            return Objects.equals(v1, tuple3.v1) && Objects.equals(v2, tuple3.v2) && Objects.equals(v3, tuple3.v3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2, v3);
        }
    }
}
