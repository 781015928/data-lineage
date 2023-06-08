package com.crazypug.datalineage.parser.sqlquery;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.AbsSQLSelectItemParser;
import com.crazypug.datalineage.parser.SQLQueryParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import com.crazypug.datalineage.utils.ExprUtils;

import java.util.List;
import java.util.stream.Collectors;

@Parser(SQLSelectQueryBlock.class)
public class SQLSelectQueryBlockParser implements SQLQueryParser<SQLSelectQueryBlock> {

    @Override
    public void findColumn(TreeNode columnNode, String columnName, SQLSelectQueryBlock queryBlock, SQLSelect select) {
        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        List<SQLSelectItem> targetItems = selectList
                .stream()
                .filter(it -> ExprUtils.equalsIgnoreCase(it.computeAlias(), columnName))
                .collect(Collectors.toList());
        if (targetItems.size() > 1) {
            throw new RuntimeException(String.format("Duplicate query fields %s", columnName));
        }

        SQLSelect cloneSelect = select.clone();

        cloneSelect.setQuery(queryBlock);
        if (targetItems.size()>0){
            SQLSelectItem sqlSelectItem = targetItems.stream().findFirst().get();
            AbsSQLSelectItemParser selectItemParser = SqlParseFactory.getSqlSelectItem(sqlSelectItem.getClass());
            selectItemParser.findSelectItem(columnNode, columnName, sqlSelectItem, cloneSelect);
        }

    }

    @Override
    public void findColumnByIndex(TreeNode columnNode, int index, SQLSelectQueryBlock queryBlock, SQLSelect select) {
        SQLSelect cloneSelect = select.clone();

        cloneSelect.setQuery(queryBlock);
        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        SQLSelectItem sqlSelectItem = selectList.get(index);
        AbsSQLSelectItemParser sqlSelectItemParser = SqlParseFactory.getSqlSelectItem(sqlSelectItem.getClass());


        sqlSelectItemParser.findSelectItem(columnNode, sqlSelectItem.computeAlias(), sqlSelectItem, cloneSelect);

    }

    @Override
    public List<SQLSelectItem> getSelectList(SQLSelectQueryBlock mySqlSelectQueryBlock) {
        return mySqlSelectQueryBlock.getSelectList();
    }

    @Override
    public int findColumnIndex(String columnName, SQLSelectQueryBlock sqlSelectQuery) {
        List<SQLSelectItem> selectList = sqlSelectQuery.getSelectList();

        return selectList.stream().map(SQLSelectItem::computeAlias).collect(Collectors.toList()).indexOf(columnName);


    }

}
