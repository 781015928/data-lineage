package com.crazypug.datalineage.parser.sqlexpr;


import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLCastExpr.class)
public class SQLCastExprParser  implements SQLExprParser<SQLCastExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLCastExpr sqlExpr, SQLSelect select) {
        SQLExpr expr = sqlExpr.getExpr();
        SQLExprParser exprParser = SqlParseFactory.getSQLExpr(expr.getClass());
        exprParser.findExpr(columnNode, columnName, selectItem, expr, select);
    }
}
