package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

import java.util.List;

@Parser(SQLBinaryOpExprGroup.class)
public class SQLBinaryOpExprGroupParser implements SQLExprParser<SQLBinaryOpExprGroup> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLBinaryOpExprGroup sqlExpr, SQLSelect select) {
        List<SQLExpr> arguments = sqlExpr.getItems();
        for (SQLExpr argument : arguments) {
            SQLExprParser parser = SqlParseFactory.getSQLExpr(argument.getClass());
            parser.findExpr(columnNode, columnName, selectItem, argument, select);

        }
    }
}
