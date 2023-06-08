package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

import java.util.List;

@Parser(SQLArrayExpr.class)
public class SQLArrayExprParser implements SQLExprParser<SQLArrayExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLArrayExpr sqlExpr, SQLSelect select) {
        List<SQLExpr> sqlExprValues =
                sqlExpr.getValues();


        SQLExpr expr = sqlExpr.getExpr();

        if (expr!=null){
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(expr.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, expr, select);

        }
        if (sqlExprValues!=null){
            for (SQLExpr sqlExprValue : sqlExprValues) {
                SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(sqlExprValue.getClass());
                sqlExprParser.findExpr(columnNode, columnName, selectItem, sqlExprValue, select);
            }
        }


    }
}
