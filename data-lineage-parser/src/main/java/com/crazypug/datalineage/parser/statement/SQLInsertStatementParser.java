package com.crazypug.datalineage.parser.statement;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.crazypug.datalineage.core.Inject;
import com.crazypug.datalineage.exception.NotFoundException;
import com.crazypug.datalineage.metadata.Column;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.metadata.Table;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.AbsSqlSelectParser;
import com.crazypug.datalineage.parser.SQLStatementParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

import java.util.List;

@Parser(SQLInsertStatement.class)
public class SQLInsertStatementParser implements SQLStatementParser<SQLInsertStatement> {
    @Inject
    private MetaDataProvider metaDataProvider;

    public String getSchema(SQLExprTableSource tableSource) {
        return tableSource.getSchema() != null ? tableSource.getSchema() : metaDataProvider.getContext().getCurrentSchemaName();

    }

    public String getTableName(SQLExprTableSource tableSource) {
        return tableSource.getTableName();

    }

    @Override
    public void parse(TreeNode rootNode, SQLInsertStatement statement) {
        SQLExprTableSource tableSource = statement.getTableSource();

        String schema = getSchema(tableSource);
        String tableName = getTableName(tableSource);
        if (statement.getQuery() == null) {
            return;
        }
        if (schema==null){
            throw new NotFoundException(String.format("schema %s not found", schema));
        }


        Table table = metaDataProvider.getTable(schema, tableName);
        if (schema==null){
            throw new NotFoundException(String.format("table %s not found", tableName));
        }

        if (statement.getColumns()==null||statement.getColumns().isEmpty()){
            List<Column> columns = table.getColumns();
            columns.stream()
                    .map(Column::getColumnName)
                    .map(SQLIdentifierExpr::new)
                    .forEach(statement::addColumn);
        }

        if (statement.getColumns().size()!=table.getColumns().size()){

            throw new RuntimeException("insert columns size not equal select size");

        }

        rootNode.setSchema(schema);
        rootNode.setTableName(tableName);
        statement.getColumns()
                .stream().map(it -> TreeNode.ofColumn(schema, tableName, String.valueOf(it)))
                .forEach(rootNode::addNode);
        SQLSelect select = statement.getQuery().clone();

        if (statement.getWith() != null) {
            SQLWithSubqueryClause with = statement.getWith().clone();

            if (with != null) {
                //  兼容可以将insert 放with 下的语法
                select.setWithSubQuery(with);
            }
        }


        AbsSqlSelectParser sqlSelectParser = SqlParseFactory.getSqlSelect(select.getClass());
        List<TreeNode> columnNodes = rootNode.getNodes();
        for (int i = 0; i < columnNodes.size(); i++) {
            TreeNode treeNode = columnNodes.get(i);
            if (treeNode.getColumnName().equals("sku_count")) {
                System.out.println();
            }

            sqlSelectParser.findColumnByIndex(treeNode, i, select);
        }


    }


}
