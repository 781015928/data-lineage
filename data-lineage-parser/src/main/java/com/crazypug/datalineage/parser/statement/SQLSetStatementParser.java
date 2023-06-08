package com.crazypug.datalineage.parser.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.crazypug.datalineage.core.Inject;
import com.crazypug.datalineage.metadata.Context;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLStatementParser;
import com.crazypug.datalineage.parser.annotation.Parser;

import java.util.List;

@Parser(SQLSetStatement.class)
public class SQLSetStatementParser implements SQLStatementParser<SQLSetStatement> {
    @Inject
    private MetaDataProvider metaDataProvider;
    @Override
    public void parse(TreeNode rootNode, SQLSetStatement statement) {
        Context context = metaDataProvider.getContext();
        List<SQLAssignItem> items = statement.getItems();
        for (SQLAssignItem sqlAssignItem : items) {
            SQLExpr target = sqlAssignItem.getTarget();
            SQLExpr value = sqlAssignItem.getValue();
            context.addEnvProperties(target.toString(), value.toString());
        }
    }
}
