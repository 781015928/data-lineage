package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SQLTableSourceParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

@Parser(SQLPropertyExpr.class)
public class SQLPropertyExprParser implements SQLExprParser<SQLPropertyExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLPropertyExpr sqlExpr, SQLSelect select) {
        String realName = sqlExpr.getName();
        SQLExpr owner = sqlExpr.getOwner();


        SQLSelectQueryBlock queryBlock = select.getQueryBlock();
        SQLTableSource from = queryBlock.getFrom();
        SQLTableSourceParser sqlSelectSource = SqlParseFactory.getSqlSelectSource(from.getClass());
        sqlSelectSource.findColumn(columnNode, owner.toString(), realName, selectItem, from,select.getWithSubQuery(),select);




    }
}