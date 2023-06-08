package com.crazypug.datalineage.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.crazypug.datalineage.model.TreeNode;

public interface AbsSqlSelectParser<T extends SQLSelect> extends SQLParser<T> {

    void findColumn(TreeNode columnNode, String columnName, SQLSelect select);

    void findColumnByIndex(TreeNode columnNode, int index, SQLSelect select);


}
