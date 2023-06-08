package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLIntervalExpr.class)
public class SQLIntervalExprParser implements SQLExprParser<SQLIntervalExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLIntervalExpr sqlExpr, SQLSelect select) {
        SQLExpr value = sqlExpr.getValue();
        if (value != null) {
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(value.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, value, select);
        }

    }
}
