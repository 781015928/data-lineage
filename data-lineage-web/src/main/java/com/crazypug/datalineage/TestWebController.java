package com.crazypug.datalineage;


import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.resp.TableField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(value = "血缘解析", tags = "血缘解析")
@Controller
@RequestMapping
public class TestWebController {

    @Autowired
    private TestService testService;

    @GetMapping()
    public String index() {
        return "doc.html";
    }

    @ResponseBody
    @ApiOperation(value = "解析sql", notes = "解析sql")
    @PostMapping(value = "parse")
    public List<TableField> parseSql(@RequestBody String sql) {
        if (sql.startsWith("\"")){
            sql = sql.substring(1, sql.length() - 1);
        }
        try {
            List<TableField> tableFields = new ArrayList<>();
            TreeNode treeNode = testService.parseSql(StringEscapeUtils.unescapeJson(sql));

            List<TreeNode> treeNodeNodes = treeNode.getNodes();

            for (TreeNode treeNodeNode : treeNodeNodes) {
                TableField tableField = new TableField();
                tableField.setTableName(treeNodeNode.getTableName());
                tableField.setDbName(treeNodeNode.getSchema());
                tableField.setColumnName(treeNodeNode.getColumnName());
                tableField.setDependFields(treeNodeNode.getChildNodes().stream().map(node -> {
                    TableField field = new TableField();
                    field.setDbName(node.getSchema());
                    field.setTableName(node.getTableName());
                    field.setColumnName(node.getColumnName());
                    return field;
                }).collect(Collectors.toList()));


                tableFields.add(tableField);
            }



            return tableFields;
        }catch (Exception ex){

            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        }

    }

    @ApiOperation(value = "重新加载元数据", notes = "重新加载元数据")
    @PostMapping(value = "reloadMetadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public String reloadMetadata() {
        testService.reloadMetadata();
        return "success";
    }
}
