package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SQLTableSourceParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
@Parser(SQLIdentifierExpr.class)
public class SQLIdentifierExprParser implements SQLExprParser<SQLIdentifierExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLIdentifierExpr sqlExpr, SQLSelect select) {
        SQLSelectQueryBlock queryBlock = select.getQueryBlock();
        SQLTableSource queryBlockFrom = queryBlock.getFrom();
        SQLTableSourceParser sqlSelectSource = SqlParseFactory.getSqlSelectSource(queryBlockFrom.getClass());

        //TODO 无别名问题
        sqlSelectSource.findColumn(columnNode, null, sqlExpr.getName(), selectItem, queryBlockFrom,select.getWithSubQuery(),select);

    }
}