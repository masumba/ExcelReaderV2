package com.excelReaderV2.ExcelReaderV2.Config;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class SqlMapper {

    private ArrayList<Object> excelColumnList;
    private ArrayList<Object> sqlExcelColumnArray;
    private String excelLocation;
    private String sqlXmlLocation;
    private ArrayList<Object> sqlExcelFileColumnOrder;
    private ArrayList<Object> sqlColumnMap;
    private StringBuilder sqlColumns;
    private String sqlTableName;

    public SqlMapper() {
        excelColumnList = new ArrayList<>();
        sqlColumns = new StringBuilder();
        sqlExcelColumnArray = new ArrayList<>();
        sqlColumnMap = new ArrayList<>();
        sqlExcelFileColumnOrder = new ArrayList<>();
    }

    public ArrayList<Object> getExcelColumnList() {
        return excelColumnList;
    }

    public void setExcelColumnList(ArrayList<Object> excelColumnList) {
        this.excelColumnList = excelColumnList;
    }

    public ArrayList<Object> getSqlExcelColumnArray() {
        return sqlExcelColumnArray;
    }

    public void setSqlExcelColumnArray(ArrayList<Object> sqlExcelColumnArray) {
        this.sqlExcelColumnArray = sqlExcelColumnArray;
    }

    public String getExcelLocation() {
        return excelLocation;
    }

    public void setExcelLocation(String excelLocation) {
        this.excelLocation = excelLocation;
    }

    public String getSqlXmlLocation() {
        return sqlXmlLocation;
    }

    public void setSqlXmlLocation(String sqlXmlLocation) {
        this.sqlXmlLocation = sqlXmlLocation;
    }

    public ArrayList<Object> getSqlExcelFileColumnOrder() {
        return sqlExcelFileColumnOrder;
    }

    public void setSqlExcelFileColumnOrder(ArrayList<Object> sqlExcelFileColumnOrder) {
        this.sqlExcelFileColumnOrder = sqlExcelFileColumnOrder;
    }

    public ArrayList<Object> getSqlColumnMap() {
        return sqlColumnMap;
    }

    public void setSqlColumnMap(ArrayList<Object> sqlColumnMap) {
        this.sqlColumnMap = sqlColumnMap;
    }

    public StringBuilder getSqlColumns() {
        return sqlColumns;
    }

    public void setSqlColumns(StringBuilder sqlColumns) {
        this.sqlColumns = sqlColumns;
    }

    public String getSqlTableName() {
        return sqlTableName;
    }

    public void setSqlTableName(String sqlTableName) {
        this.sqlTableName = sqlTableName;
    }

    @Override
    public String toString() {
        return "SqlMapper{" +
                "excelColumnList=" + excelColumnList +
                ", sqlExcelColumnArray=" + sqlExcelColumnArray +
                ", excelLocation='" + excelLocation + '\'' +
                ", sqlXmlLocation='" + sqlXmlLocation + '\'' +
                ", sqlExcelFileColumnOrder=" + sqlExcelFileColumnOrder +
                ", sqlColumnMap=" + sqlColumnMap +
                ", sqlColumns=" + sqlColumns +
                ", sqlTableName='" + sqlTableName + '\'' +
                '}';
    }
}
