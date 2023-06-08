package com.crazypug.datalineage.parser.tablesource;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLQueryParser;
import com.crazypug.datalineage.parser.SQLTableSourceParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.utils.ExprUtils;

import java.util.List;


@Parser(SQLUnionQueryTableSource.class)
public class SQLUnionQueryTableSourceParser implements SQLTableSourceParser<SQLUnionQueryTableSource> {
    @Override
    public void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, SQLUnionQueryTableSource tableSource, SQLWithSubqueryClause withSubqueryClause, SQLSelect select) {
        //tableSource.findColumn()
        SQLUnionQuery tableSourceUnion = tableSource.getUnion();

        SqlParseFactory.getSqlSelectQuery(tableSourceUnion.getClass()).findColumn(columnNode, columnName, tableSourceUnion, select);

    }

    @Override
    public boolean haveColumn(String columnName, SQLUnionQueryTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        SQLUnionQuery tableSourceUnion = tableSource.getUnion();


        SQLQueryParser sqlSelectQuery = SqlParseFactory.getSqlSelectQuery(tableSourceUnion.getClass());
        List<SQLSelectItem> selectList = sqlSelectQuery.getSelectList(tableSourceUnion);
        for (SQLSelectItem selectItem : selectList) {
            if ( ExprUtils.equalsIgnoreCase(selectItem.computeAlias(),columnName)) {
                return true;
            }
        }


        return false;
    }

    @Override
    public String findOwner(String columnName, SQLSelectItem selectItem, SQLUnionQueryTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {


        return  tableSource.getAlias();
    }
}
