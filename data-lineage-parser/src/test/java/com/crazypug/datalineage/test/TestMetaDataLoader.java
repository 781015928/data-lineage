package com.crazypug.datalineage.test;

import com.crazypug.datalineage.core.Bean;
import com.crazypug.datalineage.core.Inject;
import com.crazypug.datalineage.metadata.Column;
import com.crazypug.datalineage.metadata.MetaDataLoader;

import java.util.List;
import java.util.stream.Collectors;


@Bean
public class TestMetaDataLoader implements MetaDataLoader {

    @Inject
    private TestDorisJdbcClient jdbcClient;


    @Override
    public List<Column> loadMetaData() {
        return jdbcClient.select("select  t.*,t2.TABLE_COMMENT from information_schema.COLUMNS t\n" +
                        "left join information_schema.TABLES t2 on t.TABLE_NAME=t2.TABLE_NAME;")
                .stream()
                .map(it -> {
                    return new Column(it.get("TABLE_SCHEMA").toString()
                            , it.get("TABLE_NAME").toString()
                            , it.get("TABLE_COMMENT")!=null?it.get("TABLE_COMMENT").toString():null
                            , it.get("COLUMN_NAME").toString()
                            , it.get("DATA_TYPE").toString(),
                            Long.parseLong(it.get("ORDINAL_POSITION").toString()),
                            it.get("NUMERIC_PRECISION") != null ? Long.parseLong(it.get("NUMERIC_PRECISION").toString()) : -1,
                            it.get("NUMERIC_SCALE") != null ? Long.parseLong(it.get("NUMERIC_SCALE").toString()) : -1,
                            it.get("COLUMN_COMMENT").toString(),
                            it.get("COLUMN_SIZE") != null ? Long.parseLong(it.get("COLUMN_SIZE").toString()) : -1
                    );
                }).collect(Collectors.toList());



    }
}
