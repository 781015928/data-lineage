package com.crazypug.datalineage.parser.sqlquery;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLQueryParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

import java.util.*;

/**
 * union all 语法中允许第二个包括第二个以后的所有查询别名与首个查询别名不一致，并且整个union all 在作为子表时
 * 会取自首个查询中的别名
 */
@Parser(SQLUnionQuery.class)
public class SQLUnionQueryParser implements SQLQueryParser<SQLUnionQuery> {
    @Override
    public void findColumn(TreeNode columnNode, String columnName, SQLUnionQuery sqlUnionQuery, SQLSelect select) {
        List<SQLSelectQuery> children = sqlUnionQuery.getChildren();
        int index = -1;
        for (int i = 0; i < children.size(); i++) {
            SQLSelectQuery sqlSelectQuery = children.get(i);
            SQLQueryParser sqlQueryParser = SqlParseFactory.getSqlSelectQuery(sqlSelectQuery.getClass());

            if (i == 0) {
                sqlQueryParser.findColumn(columnNode, columnName, sqlSelectQuery, select);
                index = sqlQueryParser.findColumnIndex(columnName, sqlSelectQuery);
                if (index==-1){
                    System.out.println();
                }
            } else {

                sqlQueryParser.findColumnByIndex(columnNode, index, sqlSelectQuery, select);
            }

        }


    }

    @Override
    public void findColumnByIndex(TreeNode columnNode, int index, SQLUnionQuery sqlUnionQuery, SQLSelect select) {
        List<SQLSelectQuery> children = sqlUnionQuery.getChildren();
        for (SQLSelectQuery sqlSelectQuery : children) {
            SQLQueryParser sqlQueryParser = SqlParseFactory.getSqlSelectQuery(sqlSelectQuery.getClass());
            sqlQueryParser.findColumnByIndex(columnNode, index, sqlSelectQuery, select);

        }


    }

    @Override
    public List<SQLSelectItem> getSelectList(SQLUnionQuery sqlUnionQuery) {
        SQLSelectQueryBlock firstQueryBlock = sqlUnionQuery.getFirstQueryBlock();
        SQLQueryParser sqlQueryParser = SqlParseFactory.getSqlSelectQuery(firstQueryBlock.getClass());
        return sqlQueryParser.getSelectList(firstQueryBlock);
    }

    @Override
    public int findColumnIndex(String columnName, SQLUnionQuery sqlSelectQuery) {
        SQLSelectQueryBlock firstQueryBlock = sqlSelectQuery.getFirstQueryBlock();
        SQLQueryParser sqlQueryParser = SqlParseFactory.getSqlSelectQuery(firstQueryBlock.getClass());
        return sqlQueryParser.findColumnIndex(columnName, firstQueryBlock);
    }


}
