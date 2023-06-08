package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;

import java.util.List;
@Parser(SQLInListExpr.class)
public class SQLInListExprParser implements SQLExprParser<SQLInListExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLInListExpr sqlExpr, SQLSelect select) {
        List<SQLExpr> children = sqlExpr.getTargetList();
        SQLExpr expr = sqlExpr.getExpr();
        if (expr!=null){
            SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(expr.getClass());
            sqlExprParser.findExpr(columnNode, columnName, selectItem, expr, select);
        }
        if (children!=null){
            for (SQLExpr child : children) {
                SQLExprParser sqlExprParser = SqlParseFactory.getSQLExpr(child.getClass());
                sqlExprParser.findExpr(columnNode, columnName, selectItem, (SQLExpr) child, select);
            }
        }
        //Object value = sqlExpr.getValue();
        // columnNode.addNode(TreeNode.ofConstant(String.valueOf(value)));


    }
}
