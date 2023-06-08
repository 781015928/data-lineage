package com.crazypug.datalineage.parser.sqllselect;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.AbsSQLSelectItemParser;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
@Parser(SQLSelectItem.class)
public class SQLSelectItemParser implements AbsSQLSelectItemParser<SQLSelectItem> {

    @Override
    public void findSelectItem(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLSelect select) {
        SQLExpr expr = selectItem.getExpr();
        SQLExprParser sqlExpr = SqlParseFactory.getSQLExpr(expr.getClass());
        sqlExpr.findExpr(columnNode, columnName, selectItem, expr, select);

    }
}
