package com.excelReaderV2.ExcelReaderV2.Service;

import com.excelReaderV2.ExcelReaderV2.Config.SqlMapper;
import com.excelReaderV2.ExcelReaderV2.Config.XMLConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelReader {

    @Autowired
    SqlMapper sqlMapper;

    @Autowired
    XMLConfig xmlConfig;

    @Autowired
    private SessionFactory sessionFactory;

    private ArrayList<Object> excelColumn = new ArrayList<Object>();
    private List<String>uploadFiles = new ArrayList<String>();
    private ArrayList<Object> targetCellNums = new ArrayList<Object>();
    private ArrayList<Object> targetCellNames = new ArrayList<Object>();
    private ArrayList<Object> targetSqlScript = new ArrayList<Object>();
    private int results;


    public void excelColumnReader(String fileLocation, int rowNum){
        sqlMapper.setExcelLocation(fileLocation);

        try {
            FileInputStream excelFile = new FileInputStream(new File(fileLocation));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

            Row row;
            row = (Row)xssfSheet.getRow(rowNum);
            Iterator<Row> iterator = xssfSheet.iterator();
            int intCounter = 0;

            excelColumn.clear();

            while (iterator.hasNext() && intCounter<1){
                Row currentRow = iterator.next();
                Iterator<Cell>cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()){
                    Cell currentCell = cellIterator.next();
                    currentCell.getRow();
                    excelColumn.add(currentCell.getStringCellValue());
                }
                intCounter++;
            }
            sqlMapper.setExcelColumnList(excelColumn);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void excelColumnTargeter(){
        try {
            uploadFiles.clear();
            uploadFiles.add(sqlMapper.getExcelLocation());
            sqlMapper = xmlConfig.readConfig(sqlMapper.getSqlXmlLocation());
            String DirFileLocation = sqlMapper.getExcelLocation();
            FileInputStream excelFile = new FileInputStream(new File(DirFileLocation));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

            Row row;
            row = (Row)xssfSheet.getRow(0);
            Iterator iterator = xssfSheet.iterator();

            //target cell numbers
            excelDocumentColumnsTargetCellNamesAndNums(iterator,row);
            sqlMapper = xmlConfig.DB_Columns(sqlMapper.getSqlXmlLocation());
            excelDocumentInsertScript(row,xssfSheet);



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void excelDocumentColumnsTargetCellNamesAndNums(Iterator iterator,Row row){

        //Numbers
        targetCellNums.clear();
        int intCounter = 0;
        while (iterator.hasNext() && intCounter<1){
            Row currentRow = (Row) iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            while (cellIterator.hasNext()){
                Cell currentCell = cellIterator.next();
                currentCell.getRow();

                for (Object obj:sqlMapper.getSqlExcelColumnArray()){
                    if (obj.getClass() == String.class){
                        if (obj.equals(currentCell.getStringCellValue())){
                            targetCellNums.add(currentCell.getColumnIndex());
                        }
                    } else if (obj.getClass() == Integer.class){
                        if (obj.equals(currentCell.getNumericCellValue())){
                            targetCellNums.add(currentCell.getColumnIndex());
                        }
                    }
                }
            }
            intCounter++;
        }

        /*Names*/
        targetCellNames.clear();
        Iterator iterator1 = targetCellNums.iterator();
        for (int i=1;i<=1;i++) {
            int intCounter1 = 0;
            while (iterator1.hasNext()) {
                Integer columnNumValue = (Integer) iterator1.next();
                if (row.getCell(columnNumValue) == null) {
                    targetCellNames.add("Null");
                } else {
                    targetCellNames.add(row.getCell(columnNumValue).toString());
                }
                intCounter1++;
            }
        }

        sqlMapper.setSqlExcelFileColumnOrder(targetCellNames);

    }

    public void excelDocumentInsertScript(Row row, XSSFSheet xssfSheet){
        //Row Data (input)
        targetSqlScript.clear();
        Row row1;
        for (int i=1; i<=xssfSheet.getLastRowNum();i++){
            row1 = (Row)xssfSheet.getRow(i);
            Iterator iterator1 = targetCellNums.iterator();
            StringBuilder strBuild = new StringBuilder();
            strBuild.append("(\"");
            int strcount = 0;
            while (iterator1.hasNext()){
                if (strcount>0){
                    strBuild.append("\",\"");
                }
                Integer testNum = (Integer)iterator1.next();

                Integer rowNumValue = (Integer)testNum;
                if (row1.getCell(rowNumValue) == null){
                    strBuild.append("null");
                } else {
                    String excelInput = row1.getCell(rowNumValue).toString();
                    excelInput = excelInput.replace("`","");
                    excelInput = excelInput.replace("\"","");

                    strBuild.append(excelInput);
                }
                strcount++;
            }
            strBuild.append("\")");
            targetSqlScript.add(strBuild);
        }
        //Insert Scripts
        int targetNumCount = 1;
        for (Object obj: targetSqlScript){
            String sql = "INSERT INTO "+sqlMapper.getSqlTableName()+" "+sqlMapper.getSqlColumns()+" VALUES "+obj;

            System.out.println(targetNumCount+" = "+sql);

            targetNumCount++;

            Session session;
            try {
                session = sessionFactory.getCurrentSession();
            } catch (HibernateException e){
                session = sessionFactory.openSession();
            }

            try {
                SQLQuery query = session.createSQLQuery(sql);
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                results = query.executeUpdate();
                session.close();
            } catch (Throwable ex){
                throw new ExceptionInInitializerError(ex);
            }
        }
        System.out.println(targetNumCount);
    }

}
