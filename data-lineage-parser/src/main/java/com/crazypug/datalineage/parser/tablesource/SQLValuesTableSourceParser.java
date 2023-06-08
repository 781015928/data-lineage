package com.crazypug.datalineage.parser.tablesource;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLTableSourceParser;

//@Parser(SQLValuesTableSource.class)
public class SQLValuesTableSourceParser implements SQLTableSourceParser<SQLValuesTableSource> {
    @Override
    public void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, SQLValuesTableSource tableSource, SQLWithSubqueryClause withSubqueryClause,SQLSelect select) {

    }

    @Override
    public String findOwner(String columnName, SQLSelectItem selectItem, SQLValuesTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        return null;
    }

    @Override
    public boolean haveColumn(String columnName, SQLValuesTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        return false;
    }
}
