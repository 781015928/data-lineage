package com.crazypug.datalineage.parser.tablesource;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.AbsSqlSelectParser;
import com.crazypug.datalineage.parser.SQLTableSourceParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.utils.ExprUtils;

import java.util.List;
import java.util.stream.Collectors;

@Parser(SQLSubqueryTableSource.class)
public class SQLSubqueryTableSourceParser implements SQLTableSourceParser<SQLSubqueryTableSource> {
    @Override
    public void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, SQLSubqueryTableSource tableSource, SQLWithSubqueryClause withSubqueryClause, SQLSelect select) {
        SQLSelect sqlSelect = tableSource.getSelect();

        AbsSqlSelectParser selectParser = SqlParseFactory.getSqlSelect(sqlSelect.getClass());

        selectParser.findColumn(columnNode, columnName, sqlSelect);

    }

    @Override
    public boolean haveColumn(String columnName, SQLSubqueryTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        SQLSelect select = tableSource.getSelect();
        if (select != null) {
            SQLSelectQuery query = select.getQuery();
            List<SQLSelectItem> selectList = SqlParseFactory.getSqlSelectQuery(query.getClass()).getSelectList(query);
            List<SQLSelectItem> sqlSelectItems = selectList.stream().filter(column ->
                    ExprUtils.equalsIgnoreCase(columnName, column.computeAlias())).collect(Collectors.toList());
            if (sqlSelectItems.size() > 0) {
                return true;
            }

        }


        return false;
    }

    @Override
    public String findOwner(String columnName, SQLSelectItem selectItem, SQLSubqueryTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        SQLSelect select = tableSource.getSelect();
        if (select != null) {
            SQLSelectQuery query = select.getQuery();
            List<SQLSelectItem> selectList = SqlParseFactory.getSqlSelectQuery(query.getClass()).getSelectList(query);
            List<SQLSelectItem> sqlSelectItems = selectList.stream().filter(column ->
                    ExprUtils.equalsIgnoreCase(columnName, column.computeAlias())).collect(Collectors.toList());
            if (sqlSelectItems.size() > 0) {
                return tableSource.getAlias();
            }
        }
        return null;
    }
}
