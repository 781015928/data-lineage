package com.crazypug.datalineage.parser.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.crazypug.datalineage.core.Inject;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLStatementParser;

@Parser(SQLUseStatement.class)
public class SQLUseStatementParser implements SQLStatementParser<SQLUseStatement> {
    @Inject
    private MetaDataProvider metaDataProvider;

    @Override
    public void parse(TreeNode rootNode, SQLUseStatement statement) {

        SQLName database = statement.getDatabase();

        String schema = database.toString();

        metaDataProvider.getContext().setCurrentSchemaName(schema);

    }
}

