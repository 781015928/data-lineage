package com.crazypug.datalineage.parser.sqlselect;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.AbsSqlSelectParser;
import com.crazypug.datalineage.parser.SQLQueryParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

@Parser(SQLSelect.class)
public class SqlSelectParser implements AbsSqlSelectParser<SQLSelect> {


    @Override
    public void findColumn(TreeNode columnNode, String columnName, SQLSelect select) {



        SQLSelectQuery query = select.getQuery();


        SQLQueryParser sqlSelectQuery = SqlParseFactory.getSqlSelectQuery(query.getClass());


        sqlSelectQuery.findColumn(columnNode,columnName,query,select);




    }

    @Override
    public void findColumnByIndex(TreeNode columnNode, int index, SQLSelect select) {

        SQLSelectQuery query = select.getQuery();


        SQLQueryParser sqlSelectQuery = SqlParseFactory.getSqlSelectQuery(query.getClass());


        sqlSelectQuery.findColumnByIndex(columnNode,index,query,select);


    }
}
