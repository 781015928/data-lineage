package com.crazypug.datalineage.parser.tablesource;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLTableSourceParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

import java.util.List;
import java.util.Objects;

@Parser(SQLLateralViewTableSource.class)
public class SQLLateralViewTableParser implements SQLTableSourceParser<SQLLateralViewTableSource> {


    /**
     * <code>
     * ods_wdt_oms_trade_query_di
     * LATERAL VIEW explode_split(json_to_string_split(goods_list, '|||'), '|||') details_list_json1 AS goods_list_json_row
     *
     * </code>
     *
     * @param columnNode
     * @param owner
     * @param columnName
     * @param selectItem
     * @param sqlLateralViewTableSource
     * @param withSubqueryClause
     */
    @Override
    public void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, SQLLateralViewTableSource sqlLateralViewTableSource, SQLWithSubqueryClause withSubqueryClause,SQLSelect select) {
        SQLTableSource sqlTableSource = sqlLateralViewTableSource.getTableSource();
        SqlParseFactory.getSqlSelectSource(sqlTableSource.getClass())
                .findColumn(columnNode, owner, columnName, selectItem, sqlTableSource, withSubqueryClause,select);

        List<SQLName> columns = sqlLateralViewTableSource.getColumns();

        for (SQLName column : columns) {
            if (Objects.equals(column.toString(), columnName)) {
                SQLMethodInvokeExpr tableFunction = sqlLateralViewTableSource.getMethod();

                SqlParseFactory.getSQLExpr(tableFunction.getClass())
                         .findExpr(columnNode, columnName, selectItem, tableFunction, select);

                //对于表函数

            }
        }
    }

    @Override
    public boolean haveColumn(String columnName, SQLLateralViewTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {

        SQLTableSource sqlTableSource = tableSource.getTableSource();

        if (SqlParseFactory.getSqlSelectSource(sqlTableSource.getClass()).haveColumn(columnName, sqlTableSource, withSubqueryClause)) {
            return true;
        }


        List<SQLName> columns = tableSource.getColumns();
        for (SQLName column : columns) {
            if (Objects.equals(column.toString(), columnName)) {
                return true;
            }
        }


        return false;
    }

    @Override
    public String findOwner(String columnName, SQLSelectItem selectItem, SQLLateralViewTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        return null;
    }
}
