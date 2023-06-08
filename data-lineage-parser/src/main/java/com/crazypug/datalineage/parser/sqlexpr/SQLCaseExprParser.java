package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

import java.util.List;

@Parser(SQLCaseExpr.class)
public class SQLCaseExprParser implements SQLExprParser<SQLCaseExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLCaseExpr sqlExpr, SQLSelect select) {

        SQLExpr valueExpr = sqlExpr.getValueExpr();

        List<SQLCaseExpr.Item> items = sqlExpr.getItems();
        if (items != null) {
            for (SQLCaseExpr.Item item : items) {
                SQLExpr condition = item.getConditionExpr();
                if (condition != null) {
                    SQLExprParser conditionParser = SqlParseFactory.getSQLExpr(condition.getClass());
                    conditionParser.findExpr(columnNode, columnName, selectItem, condition, select);
                }

                SQLExpr value = item.getValueExpr();
                if (value != null) {
                    SQLExprParser valueParser = SqlParseFactory.getSQLExpr(value.getClass());
                    valueParser.findExpr(columnNode, columnName, selectItem, value, select);
                }
            }
        }
        SQLExpr elseExpr = sqlExpr.getElseExpr();
        if (valueExpr != null) {
            SQLExprParser valueExprParser = SqlParseFactory.getSQLExpr(valueExpr.getClass());
            valueExprParser.findExpr(columnNode, columnName, selectItem, valueExpr, select);
        }

        if (elseExpr != null) {
            SQLExprParser elseExprParser = SqlParseFactory.getSQLExpr(elseExpr.getClass());


            elseExprParser.findExpr(columnNode, columnName, selectItem, elseExpr, select);


        }


    }
}
