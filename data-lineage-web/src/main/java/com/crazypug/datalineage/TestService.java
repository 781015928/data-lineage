package com.crazypug.datalineage;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.crazypug.datalineage.core.BeanFactory;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLStatementParser;
import com.crazypug.datalineage.parser.SqlParseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {


    final static Logger log = LoggerFactory.getLogger(TestService.class);

    static {

        try {
            Class.forName(SqlParseFactory.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadMetadata() {
        BeanFactory.getInstance().getBean(MetaDataProvider.class)
                .reload();

    }

    public TreeNode parseSql(String sql) {
        TreeNode treeNode = TreeNode.rootNdoe();

        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);


        for (SQLStatement sqlStatement : sqlStatements) {
            try {

                SQLStatementParser sqlStatementParser = SqlParseFactory.getStatement(sqlStatement.getClass());
                sqlStatementParser.parse(treeNode, sqlStatement);

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            } finally {
                log.info("treeNode:{}", treeNode);
            }


        }


        return treeNode;

    }
}
