package com.crazypug.datalineage.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;

public interface AbsSQLSelectItemParser<T extends SQLSelectItem> extends SQLParser<T> {


    void findSelectItem(TreeNode columnNode, String columnName, T selectItem, SQLSelect select);



}
