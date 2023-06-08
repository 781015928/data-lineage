package com.crazypug.datalineage.test;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.crazypug.datalineage.metadata.*;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLStatementParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestDataLineage {


    @Test
    public void testMetaData() {

        MetaDataProviderImpl metaDataProvider = new MetaDataProviderImpl();

        List<Schemata> allSchema = metaDataProvider.getAllSchema();


        Table table = metaDataProvider.getTable("edw_klw", "app_toc_purchase_tool_da");


        System.out.println(table);


    }

    @Test
    public void test() {

        MetaDataProviderImpl metaDataProvider = new MetaDataProviderImpl();

        List<Schemata> allSchema = metaDataProvider.getAllSchema();


        Table table = metaDataProvider.getTable("edw_klw", "app_toc_purchase_tool_da");


        System.out.println(table);


    }

    @Test
    public void testSql() throws IOException {
//        System.out.println(sql);
        byte[] bytes = Files.readAllBytes(Paths.get("/Users/czg/code/github/data-lineage/data-lineage/test4.sql"));

        String sqlText = new String(bytes, StandardCharsets.UTF_8);


        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sqlText, DbType.mysql);


        for (SQLStatement sqlStatement : sqlStatements) {
            testSql(sqlStatement.toString());
        }

        ContextHolder.clearContext();
    }

    private void testSql(String sql) {
        System.out.println("-------------------SQL------------------------");
        System.out.println(sql);
        System.out.println("-------------------SQL------------------------");

        System.out.println();
        System.out.println("-------------------linage------------------------");
        TreeNode treeNode = TreeNode.rootNdoe();

        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);

        for (SQLStatement sqlStatement : sqlStatements) {
            try {

                SQLStatementParser sqlStatementParser = SqlParseFactory.getStatement(sqlStatement.getClass());
                sqlStatementParser.parse(treeNode, sqlStatement);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                System.out.println(treeNode);
            }


        }


        System.out.println();
        System.out.println();
        System.out.println("-------------------linage------------------------");
    }
}
