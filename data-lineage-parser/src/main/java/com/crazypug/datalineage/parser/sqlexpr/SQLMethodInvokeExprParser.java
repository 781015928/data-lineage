package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

import java.util.List;
@Parser(SQLMethodInvokeExpr.class)
public class SQLMethodInvokeExprParser implements SQLExprParser<SQLMethodInvokeExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLMethodInvokeExpr sqlExpr, SQLSelect select) {
        List<SQLExpr> arguments = sqlExpr.getArguments();
        for (SQLExpr argument : arguments) {
            SQLExprParser parser = SqlParseFactory.getSQLExpr(argument.getClass());
            parser.findExpr(columnNode, argument.toString(), selectItem, argument, select);

        }


    }
}