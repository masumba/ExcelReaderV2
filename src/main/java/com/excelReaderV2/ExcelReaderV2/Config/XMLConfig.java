package com.excelReaderV2.ExcelReaderV2.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class XMLConfig {

    @Autowired
    SqlMapper sqlMapper = new SqlMapper();

    public SqlMapper xmlCreate(String targetDbTableName, List targetExcelColumnName, List targetXMLFieldState, List targetDbColumnName){
        String xmlFileLocation = "ExcelReaderConfiguration.xml";
        Element xmlColumn = null;

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("ExcelUploads");
            document.appendChild(rootElement);

            Element excelUploadElement = document.createElement("ExcelUpload");
            rootElement.appendChild(excelUploadElement);

            Element excelTable = document.createElement("Table");
            excelUploadElement.appendChild(excelTable);

            Attr attrTable = document.createAttribute("tableName");
            attrTable.setValue(targetDbTableName);
            excelTable.setAttributeNode(attrTable);

            int check_box_state_position_point = 0;
            for(Object checkboxValuee : targetXMLFieldState) {
                if (checkboxValuee.toString().equals("on")){
                    int position_point = 0;
                    for(Object xmlColumnNameValuee : targetDbColumnName) {

                        if (check_box_state_position_point == position_point){
                            xmlColumn = document.createElement("Column");
                            excelTable.appendChild(xmlColumn);
                            xmlColumn.setAttribute("columnName",xmlColumnNameValuee.toString());
                            int inner_position_point = 0;
                            for(Object ExcelValue : targetExcelColumnName) {

                                if (position_point == inner_position_point){
                                    Element xmlExcelValue = document.createElement("ExcelValue");
                                    xmlExcelValue.appendChild(document.createTextNode(ExcelValue.toString()));
                                    xmlColumn.appendChild(xmlExcelValue);
                                }

                                inner_position_point++;
                            }
                        }
                        position_point++;
                    }
                }
                check_box_state_position_point++;
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFileLocation));

            transformer.transform(source,streamResult);

            System.out.println("File Created And Saved");

            sqlMapper.setSqlXmlLocation(xmlFileLocation);

        } catch (ParserConfigurationException pce){
            pce.printStackTrace();
        } catch (TransformerException tfe){
            tfe.printStackTrace();
        }

        return sqlMapper;
    }

    public SqlMapper readConfig(String xmlFileLocationName){
        String tableName = null;
        StringBuilder tableColumns = new StringBuilder();
        ArrayList<Object> excelColumnArray = new ArrayList<Object>();
        ArrayList<Object> dbColumnMap = new ArrayList<Object>();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFileLocationName);
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList tableNodeList = (NodeList)xPath.compile("//Table").evaluate(document,XPathConstants.NODESET);
            for (int i = 0; i<tableNodeList.getLength();i++){
                tableName = xPath.compile("./@tableName").evaluate(tableNodeList.item(i));
            }

            tableColumns.append("(");
            NodeList columnNodeList = (NodeList)xPath.compile("//Column").evaluate(document, XPathConstants.NODESET);
            for (int i=0;i<columnNodeList.getLength();i++){
                if (i>0){tableColumns.append(",");}
                String dbColumnName = xPath.compile("./@columnName").evaluate(columnNodeList.item(i));
                tableColumns.append(dbColumnName);
                String excelColumnName = xPath.compile("./ExcelValue").evaluate(columnNodeList.item(i));
                excelColumnArray.add(excelColumnName);
                dbColumnMap.add(dbColumnName+" = "+ excelColumnName);
            }
            tableColumns.append(")");

            //System.out.println(tableName);
            sqlMapper.setSqlTableName(tableName);
            //System.out.println(tableColumns);
            sqlMapper.setSqlColumns(tableColumns);
            //System.out.println(dbColumnMap);
            sqlMapper.setSqlColumnMap(dbColumnMap);
            //System.out.println(excelColumnArray);
            sqlMapper.setSqlExcelColumnArray(excelColumnArray);

        } catch (Exception e){
            e.printStackTrace();
        }

        return sqlMapper;
    }

    public SqlMapper DB_Columns(String xmlFileLocation){

        ArrayList<Object> newDB_Order = new ArrayList<Object>();
        ArrayList<Object> DB_List = new ArrayList<Object>();
        StringBuilder tableColumns = new StringBuilder();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFileLocation);
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList columnNodeList = (NodeList)xPath.compile("//Column").evaluate(document, XPathConstants.NODESET);

            for (Object obj: sqlMapper.getSqlExcelFileColumnOrder()){
                for (int i=0;i<columnNodeList.getLength();i++){
                    String dbColumn = xPath.compile("./@columnName").evaluate(columnNodeList.item(i));
                    String excelColumnName = xPath.compile("./ExcelValue").evaluate(columnNodeList.item(i));
                    if (obj.equals(excelColumnName)){
                        DB_List.add(dbColumn);
                    }
                }
            }

            int value_count = 0;
            tableColumns.append("(");
            for (Object obj: DB_List){
                if (value_count>0){
                    tableColumns.append(",");
                }
                tableColumns.append(obj);
                value_count++;
            }
            tableColumns.append(")");

            sqlMapper.setSqlColumns(tableColumns);

        } catch (Exception e){
            e.printStackTrace();
        }

        return sqlMapper;
    }
}
