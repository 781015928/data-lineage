package com.crazypug.datalineage.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class TreeNode {

    public LinkedHashSet<TreeNode> childNodes = new LinkedHashSet<TreeNode>();

    private String schema;
    private String tableName;

    private String columnName;

    public String getSchema() {
        return schema;
    }


    public LinkedHashSet<TreeNode> getChildNodes() {
        return childNodes;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setChildNodes(LinkedHashSet<TreeNode> childNodes) {
        this.childNodes = childNodes;
    }


    public String getColumnName() {
        return columnName;
    }

    public String getTableName() {
        return tableName;
    }

    private TreeNode() {
    }

    public boolean hasNode(TreeNode node) {
        return childNodes.contains(node);
    }

    public void addNode(TreeNode treeNode) {
        childNodes.add(treeNode);
    }

    public List<TreeNode> getNodes() {
        return new ArrayList<>(childNodes);
    }


    public static TreeNode ofColumn(String schema, String tableName, String columnName) {
        TreeNode treeNode = new TreeNode();
        treeNode.schema = schema;
        treeNode.columnName = columnName;
        treeNode.tableName = tableName;
        return treeNode;
    }

    public static TreeNode rootNdoe() {
        TreeNode treeNode = new TreeNode();
        return treeNode;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(tableName, treeNode.tableName) && Objects.equals(columnName, treeNode.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, columnName);
    }

    @Override
    public String toString() {
        if (childNodes.isEmpty()) {
            if (columnName == null) {
                return String.format("%s.%s", schema, tableName);
            }
            return String.format("%s.%s.%s", schema, tableName, columnName);
        }

        if (columnName == null) {
            return String.format("%s.%s-[%s]", schema, tableName, childNodes.stream().map(TreeNode::toString).collect(Collectors.joining("\n,")));
        }

        return String.format("%s.%s.%s-[%s]", schema, tableName, columnName, childNodes.stream().map(TreeNode::toString).collect(Collectors.joining("\n,")));
    }
}

