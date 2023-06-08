package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;

@Parser(SQLIntegerExpr.class)
public class SQLIntegerExprParser implements SQLExprParser<SQLIntegerExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLIntegerExpr sqlExpr, SQLSelect select) {


        // 常量 System.out.println(sqlExpr);

    }
}
