package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLAllColumnExpr.class)
public class SQLAllColumnExprParser implements SQLExprParser<SQLAllColumnExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLAllColumnExpr sqlAllColumnExpr, SQLSelect select) {
        System.out.println(sqlAllColumnExpr);
    }
}
