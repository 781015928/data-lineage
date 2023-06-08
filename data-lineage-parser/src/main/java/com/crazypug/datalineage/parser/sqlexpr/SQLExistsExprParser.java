package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLExistsExpr.class)
public class SQLExistsExprParser implements SQLExprParser<SQLExistsExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLExistsExpr sqlExpr, SQLSelect select) {
        SQLSelect subQuery = sqlExpr.getSubQuery();

        //TODO 该语法暂不支持


    }
}
