package com.excelReaderV2.ExcelReaderV2.Controller.Web;

import com.excelReaderV2.ExcelReaderV2.Config.SqlMapper;
import com.excelReaderV2.ExcelReaderV2.Config.XMLConfig;
import com.excelReaderV2.ExcelReaderV2.Service.ExcelReader;
import com.excelReaderV2.ExcelReaderV2.Service.StorageService;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    StorageService storageService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    SqlMapper sqlMapper;

    @Autowired
    XMLConfig xmlConfig;

    @Autowired
    ExcelReader excelReader;

    @RequestMapping("/")
    public ModelAndView homePage(){
        ModelAndView showPage = new ModelAndView("home");
        return showPage;
    }

    /*Saves To DB*/
    @RequestMapping("/save")
    public ModelAndView saveToDb(@RequestParam("clmnName") String clmnName,@RequestParam("ExcelValue") List<String> ExcelValue,@RequestParam(value = "my-checkbox", defaultValue = "off") List<String> optionCheckbox,@RequestParam("columnName") List<String> xmlColumnName){
        ModelAndView mv = new ModelAndView("redirect:/");

        sqlMapper = xmlConfig.xmlCreate(clmnName,ExcelValue,optionCheckbox,xmlColumnName);

        excelReader.excelColumnTargeter();

        //done message

        return mv;
    }

    /*Extracts From Excel Sheet Cloumn Options*/
    @RequestMapping(value = "/saveFile",method = RequestMethod.POST)
    public ModelAndView homePageFileUpload(@RequestParam("excelFile")MultipartFile file){
        ModelAndView mv = new ModelAndView("home");
        try {
            mv.addObject("msg","Submission");
            storageService.storeFile(file);
            mv.addObject("excelList",sqlMapper.getExcelColumnList());

        } catch (Exception e){
            e.printStackTrace();

            mv.addObject("msg","Failed Submission");
        }
        return mv;
    }

    @RequestMapping(value = "/databases",method = RequestMethod.POST)
    @CrossOrigin
    public ModelAndView getDatabases(@RequestParam(value = "dblist",defaultValue = "") String dblist){
        ModelAndView databaseList = new ModelAndView("optionDatabases");
        ArrayList<Object> databaseNames = new ArrayList<Object>();
        String sqlStatement = "SHOW DATABASES";
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e){
            session = sessionFactory.openSession();
        }
        SQLQuery sqlQuery = session.createSQLQuery(sqlStatement);
        sqlQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List list = sqlQuery.list();
        databaseList.addObject("dbLists",list);

        return databaseList;
    }

    @RequestMapping(value = "/tables",method = RequestMethod.POST)
    @CrossOrigin
    public ModelAndView getTables(@RequestParam(value = "tblName",defaultValue = "") String tblName){
        ModelAndView tableList = new ModelAndView("optionTables");

        ArrayList<Object> tableNames = new ArrayList<Object>();
        String sqlStatement = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='"+tblName+"'";

        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e){
            session = sessionFactory.openSession();
        }

        SQLQuery query = session.createSQLQuery(sqlStatement);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List list = query.list();

        tableList.addObject("dbTblLists",list);
        tableList.addObject("dbTblListsName",tblName);

        return tableList;
    }

    @RequestMapping(value = "/columns",method = RequestMethod.POST)
    @CrossOrigin
    public ModelAndView getColumns(@RequestParam(value = "clmnName",defaultValue = "") String clmnName){
        ModelAndView columnList = new ModelAndView("optionColumns");
        ArrayList<Object> columnNames = new ArrayList<Object>();
        String sqlStatement = "SHOW COLUMNS FROM "+clmnName;

        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e){
            session = sessionFactory.openSession();
        }

        SQLQuery query = session.createSQLQuery(sqlStatement);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List list = query.list();

        columnList.addObject("tblClmnLists",list);
        columnList.addObject("excelList",sqlMapper.getExcelColumnList());
        return columnList;
    }
}
