package com.excelReaderV2.ExcelReaderV2.Test;

import com.excelReaderV2.ExcelReaderV2.Config.SqlMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;


@Service
public class ExcelTest {
    @Autowired
    SqlMapper sqlMapper;


    private ArrayList<Object> excelColumn = new ArrayList<Object>();

    public void excelReader(String fileLocation, int rowNum){
        try {
            System.out.println(fileLocation);

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

                    //
                    excelColumn.add(currentCell.getStringCellValue());
                    //System.out.println(currentCell.getStringCellValue());
                    //
                }
                intCounter++;
            }
            sqlMapper.setExcelColumnList(excelColumn);
            //excelColumn.clear();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
