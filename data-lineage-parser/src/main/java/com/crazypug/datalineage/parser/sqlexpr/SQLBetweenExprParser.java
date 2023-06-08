package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLBetweenExpr.class)
public class SQLBetweenExprParser implements SQLExprParser<SQLBetweenExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLBetweenExpr sqlExpr, SQLSelect select) {
        SQLExpr testExpr = sqlExpr.getTestExpr();

        SQLExpr beginExpr = sqlExpr.getBeginExpr();

        SQLExpr endExpr = sqlExpr.getEndExpr();


        if (testExpr!=null){
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(testExpr.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, testExpr, select);
        }

        if (beginExpr!=null){
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(beginExpr.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, beginExpr, select);
        }
        if (endExpr!=null){
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(endExpr.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, endExpr, select);
        }


    }
}
