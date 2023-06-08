package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

import java.util.List;

@Parser(SQLAggregateExpr.class)
public class SQLAggregateExprParser implements SQLExprParser<SQLAggregateExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLAggregateExpr sqlExpr, SQLSelect select) {
        List<SQLExpr> arguments = sqlExpr.getArguments();
        for (SQLExpr argument : arguments) {
            SQLExprParser parser = SqlParseFactory.getSQLExpr(argument.getClass());
            parser.findExpr(columnNode, columnName, selectItem, argument, select);

        }
    }
}
