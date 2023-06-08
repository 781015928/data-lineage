package com.crazypug.datalineage.parser.statement;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLStatementParser;
@Parser(SQLCreateTableStatement.class)
public class SQLCreateTableAsStatementParser implements SQLStatementParser<SQLCreateTableStatement> {


    @Override
    public void parse(TreeNode rootNode, SQLCreateTableStatement statement) {

    }
}
