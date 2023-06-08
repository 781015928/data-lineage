package com.crazypug.datalineage.parser.tablesource;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.core.Inject;
import com.crazypug.datalineage.metadata.Column;
import com.crazypug.datalineage.metadata.Table;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.*;
import com.crazypug.datalineage.utils.ExprUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Parser(SQLExprTableSource.class)
public class SQLExprTableSourceParser implements SQLTableSourceParser<SQLExprTableSource> {

    @Inject
    private MetaDataProvider metaDataProvider;

    public String getSchema(SQLExprTableSource tableSource) {

        return tableSource.getSchema() != null ? tableSource.getSchema() : metaDataProvider.getContext().getCurrentSchemaName();

    }

    public String getTableName(SQLExprTableSource tableSource) {
        return tableSource.getTableName();

    }

    @Override
    public void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, SQLExprTableSource tableSource, SQLWithSubqueryClause withSubqueryClause,SQLSelect select) {
        String tableName = getTableName(tableSource);
        String schema = getSchema(tableSource);

        if (owner != null) {
            if (withSubqueryClause != null) {
                //处理with 依赖
                if (ExprUtils.equalsIgnoreCase(tableSource.getAlias(),owner)){

                    findColumn(columnNode, null, columnName, selectItem, tableSource, withSubqueryClause,select);
                    return;
                }

                if (findColumnNameByWithSubquery(columnNode, owner, columnName, withSubqueryClause)) {
                    return;
                }
            }
            Column column = metaDataProvider.getColumn(schema, tableName,  ExprUtils.toExpr(columnName));
            if (column != null) {
                // 基于数据库的元数据的强验证
                columnNode.addNode(TreeNode.ofColumn(schema, tableName, ExprUtils.toExpr(columnName)));
            } else {
                // 有中情况该别名是一个二次的别名，这时候需要使用原始的名再去当前查询规则下查找
                if (withSubqueryClause != null) {
                    if (findColumnNameByWithSubquery(columnNode, tableName, columnName, withSubqueryClause)) {
                        return;
                    }
                } else {
                    throwNotFound(columnName,tableSource);
                }
            }


        } else {
            String ownerName = findOwner(columnName, selectItem, tableSource, withSubqueryClause);
            if (haveColumn(columnName, tableSource, withSubqueryClause)){
                findColumn(columnNode, ownerName, columnName, selectItem, tableSource, withSubqueryClause,select);
            }else {

              throwNotFound(columnName,tableSource);
            }

        }

    }

    private void throwNotFound(String columnName, SQLExprTableSource tableSource) {
        String tableName = getTableName(tableSource);
        String schema = getSchema(tableSource);
        if (schema==null){
            throw new RuntimeException(String.format("schema context not found:%s.%s",tableName,columnName));
        }
        Table table = metaDataProvider.getTable(schema, tableName);


    }

    @Override
    public boolean haveColumn(String columnName, SQLExprTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        String tableName = getTableName(tableSource);
        String schema = getSchema(tableSource);
        Column column = metaDataProvider.getColumn(schema, tableName,  ExprUtils.toExpr(columnName));
        //2023年04月14日14:46:08
        //当前判定的不是物理表，而是一个子查询，这时候需要判断子查询中是否有该字段

        if (withSubqueryClause!=null &&column==null){
            List<SQLWithSubqueryClause.Entry> withSubqueryClauseEntries = withSubqueryClause.getEntries();
            if (withSubqueryClauseEntries!=null){
                for (SQLWithSubqueryClause.Entry withSubqueryClauseEntry : withSubqueryClauseEntries) {
                    if (Objects.equals(tableName, withSubqueryClauseEntry.getAlias())) {
                        SQLSelect sqlSelect = withSubqueryClauseEntry.getSubQuery();
                        SQLSelectQueryBlock queryBlock = sqlSelect.getQueryBlock();
                        SQLQueryParser sqlSelectQuery = SqlParseFactory.getSqlSelectQuery(queryBlock.getClass());
                        List<SQLSelectItem> selectList = sqlSelectQuery.getSelectList(queryBlock);
                        for (SQLSelectItem sqlSelectItem : selectList) {
                            if (ExprUtils.equalsIgnoreCase(columnName, sqlSelectItem.computeAlias())) {
                                return true;
                            }
                        }

                    }
                }
            }
        }


        return column != null;
    }

    private boolean findColumnNameByWithSubquery(TreeNode columnNode, String owner, String columnName, SQLWithSubqueryClause withSubqueryClause) {
        ArrayList<SQLWithSubqueryClause.Entry> withEntry = new ArrayList<>();
        for (SQLWithSubqueryClause.Entry entry : withSubqueryClause.getEntries()) {

            withEntry.add(entry.clone());
            if (Objects.equals(owner, entry.getAlias())) {

                SQLSelect sqlSelect = entry.getSubQuery().clone();//避免污染

                //传递with依赖

                SQLWithSubqueryClause withSubQuery = sqlSelect.getWithSubQuery();
                if (withSubQuery == null) {
                    withSubQuery = new SQLWithSubqueryClause();
                }
                withSubQuery.getEntries().addAll(withEntry);


                sqlSelect.setWithSubQuery(withSubQuery);
                //递归到子查询
                AbsSqlSelectParser sqlSelectParser = SqlParseFactory.getSqlSelect(sqlSelect.getClass());
                sqlSelectParser.findColumn(columnNode, columnName, sqlSelect);

                return true;
            }
        }
        return false;
    }

    @Override
    public String findOwner(String columnName, SQLSelectItem selectItem, SQLExprTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {

        if (withSubqueryClause != null) {
            for (SQLWithSubqueryClause.Entry entry : withSubqueryClause.getEntries()) {
                if (Objects.equals(tableSource.getTableName(), entry.getAlias())) {

                    SQLSelectQuery query = entry.getSubQuery().getQuery();
                    SQLQueryParser sqlQueryParser = SqlParseFactory.getSqlSelectQuery(entry.getSubQuery().getQuery().getClass());

                    List<SQLSelectItem> selectList = sqlQueryParser.getSelectList(query);

                    long count = selectList
                            .stream()
                            .map(SQLSelectItem::computeAlias)
                            .filter(it -> Objects.equals(columnName, it)).count();
                    if (count > 0) {
                        return tableSource.getTableName();
                    } else {

                        return null;
                    }

                }
            }

        }

        String tableName = getTableName(tableSource);

        String schema = getSchema(tableSource);


        Column column = metaDataProvider.getColumn(schema, tableName, ExprUtils.toExpr(columnName));
        if (column != null) {
            return tableName;
        } else {
            return null;
        }

    }

}
