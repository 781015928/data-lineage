package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLUnaryExpr.class)
public class SQLUnaryExprParser  implements SQLExprParser<SQLUnaryExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLUnaryExpr sqlExpr, SQLSelect select) {
        SQLExpr expr = sqlExpr.getExpr();
        if (expr!=null){
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(expr.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, expr, select);

        }


    }
}
