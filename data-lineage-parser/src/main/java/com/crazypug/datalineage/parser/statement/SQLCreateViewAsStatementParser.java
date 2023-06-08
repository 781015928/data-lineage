package com.crazypug.datalineage.parser.statement;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.core.Inject;
import com.crazypug.datalineage.exception.NotFoundException;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.parser.AbsSqlSelectParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLStatementParser;

import java.util.List;

@Parser(SQLCreateViewStatement.class)
public class SQLCreateViewAsStatementParser implements SQLStatementParser<SQLCreateViewStatement> {


    @Inject
    private MetaDataProvider metaDataProvider;

    public String getSchema(SQLExprTableSource tableSource) {
        return tableSource.getSchema() != null ? tableSource.getSchema() : metaDataProvider.getContext().getCurrentSchemaName();

    }

    public String getTableName(SQLExprTableSource tableSource) {
        return tableSource.getTableName();

    }

    @Override
    public void parse(TreeNode rootNode, SQLCreateViewStatement statement) {
        SQLExprTableSource tableSource = statement.getTableSource();

        String schema = getSchema(tableSource);
        String tableName = getTableName(tableSource);
        if (statement.getSubQuery() == null) {
            return;
        }
        if (schema == null) {
            throw new NotFoundException(String.format("schema %s not found", schema));
        }


        rootNode.setSchema(schema);
        rootNode.setTableName(tableName);

        if (statement.getColumns().isEmpty()){
            SQLSelect subQuery = statement.getSubQuery();
            if (subQuery!=null){
                SQLSelectQuery subQueryQuery = subQuery.getQuery();
                List<SQLSelectItem> selectList = SqlParseFactory.getSqlSelectQuery(subQueryQuery.getClass()).getSelectList(subQueryQuery);
                selectList .stream()
                        .map(it -> TreeNode.ofColumn(schema, tableName, it.computeAlias()))
                        .forEach(rootNode::addNode);

            }

        }else{
            statement.getColumns()
                    .stream().map(it -> TreeNode.ofColumn(schema, tableName, String.valueOf(it)))
                    .forEach(rootNode::addNode);
        }



        SQLSelect select = statement.getSubQuery().clone();


        AbsSqlSelectParser sqlSelectParser = SqlParseFactory.getSqlSelect(select.getClass());
        List<TreeNode> columnNodes = rootNode.getNodes();
        for (int i = 0; i < columnNodes.size(); i++) {
            TreeNode treeNode = columnNodes.get(i);
            sqlSelectParser.findColumnByIndex(treeNode, i, select);
        }
    }

}
