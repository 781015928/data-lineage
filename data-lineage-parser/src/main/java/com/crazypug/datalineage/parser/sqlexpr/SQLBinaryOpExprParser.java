package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

@Parser(SQLBinaryOpExpr.class)
public class SQLBinaryOpExprParser implements SQLExprParser<SQLBinaryOpExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLBinaryOpExpr sqlExpr, SQLSelect select) {
        SQLExpr left = sqlExpr.getLeft();

        SQLExpr right = sqlExpr.getRight();

        SQLExprParser leftSQLExprParser = SqlParseFactory.getSQLExpr(left.getClass());
        SQLExprParser rightSQLExprParser = SqlParseFactory.getSQLExpr(right.getClass());


        leftSQLExprParser.findExpr(columnNode, columnName, selectItem, left, select);
        rightSQLExprParser.findExpr(columnNode, columnName, selectItem, right, select);

    }
}
