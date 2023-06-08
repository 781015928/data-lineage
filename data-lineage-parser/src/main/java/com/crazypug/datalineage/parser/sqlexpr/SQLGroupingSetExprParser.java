package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;

import java.util.List;

@Parser(SQLGroupingSetExpr.class)
public class SQLGroupingSetExprParser implements SQLExprParser<SQLGroupingSetExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLGroupingSetExpr sqlExpr, SQLSelect select) {
        List<SQLExpr> parameters =
                sqlExpr.getParameters();

        if (parameters!=null){
            for (SQLExpr parameter : parameters) {
                SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(parameter.getClass());
                sqlExprParser.findExpr(columnNode, columnName, selectItem, parameter, select);
            }
        }




    }
}
