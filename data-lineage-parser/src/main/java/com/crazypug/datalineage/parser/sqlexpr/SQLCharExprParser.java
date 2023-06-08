package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;

@Parser(SQLCharExpr.class)
public class SQLCharExprParser implements SQLExprParser<SQLCharExpr> {

    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLCharExpr sqlExpr, SQLSelect select) {
        // Object value = sqlExpr.getValue();
        // columnNode.addNode(TreeNode.ofConstant(String.valueOf(value)));

    }
}
