package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLNumberExpr.class)
public class SQLNumberExprParser implements SQLExprParser<SQLNumberExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLNumberExpr sqlExpr, SQLSelect select) {


    }
}