package com.crazypug.datalineage.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.crazypug.datalineage.model.TreeNode;

public interface SQLStatementParser<T extends SQLStatement> extends SQLParser<SQLStatement> {

    void parse(TreeNode rootNode, T statement);


}
