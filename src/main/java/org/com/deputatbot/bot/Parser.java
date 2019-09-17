package org.com.deputatbot.bot;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.com.deputatbot.domain.*;
import org.com.deputatbot.repos.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class Parser {



    public void allNduOkrug(OkrugNduRepo okrugNduRepo, DilniziaRepo dilniziaRepo, DeputatRepo  deputatRepo)
    {

        for (Integer i=24;i<41;i++)
        {
            ParserSite(i, okrugNduRepo,dilniziaRepo,deputatRepo);
        }
    }

    public  void ParserSite (Integer number_nduokrug, OkrugNduRepo okrugNduRepo, DilniziaRepo dilniziaRepo, DeputatRepo deputatRepo)  {
        String mylink ="https://www.drv.gov.ua/ords/portal/!cm_core.cm_index?option=ext_dvk&pid100=12&pf5271="+number_nduokrug+"&prejim=3";
        OkrugNdu okrugNdu =new OkrugNdu();
        okrugNdu.setNumber(number_nduokrug);

        okrugNduRepo.saveAndFlush(okrugNdu);

        Set<Dilnizia> dilniziaArrayList=new HashSet<>();
        Connection connection = Jsoup.connect(mylink);
        connection.userAgent("Mozilla/5.0");
        Document doc = null;
        try {
            doc = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element form = doc.getElementById("tab1");
        Map<Long,String>dil=new HashMap<>();
        ArrayList<Long>num=new ArrayList<>();
        ArrayList<String>reg=new ArrayList<>();
        for (Element el:form.getElementsByTag("tr"))
        {
            System.out.println(el.tagName());
            int count=0;
            for(Element al:el.getElementsByTag("td"))
            {
                if (count<2) {
                    if (count==0) {
                        num.add(Long.valueOf(al.text()));
                        System.out.println(al.text());

                    }
                    if (count==1) {
                            System.out.println(al.text().toCharArray().length);
                            reg.add(al.text());

                        }


                    count++;

                }else break;


            }

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        int con=0;
        OkrugNdu ok= okrugNduRepo.findByNumber(number_nduokrug);
        for (Long l:num)
        {Dilnizia dilnizia=new Dilnizia();
            dilnizia.setNumber(l);
            dilnizia.setRegion(reg.get(con++).toLowerCase());
            dilnizia.setOkrugNdu(ok);
            dilniziaRepo.saveAndFlush(dilnizia);
                   }


    }

    public void ParserExelMer(CityRepo cityRepo, MerRepo merRepo) throws IOException {
        File myFile = new File("src/main/resources/testfile.xls");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();

        // Traversi ng over each row of XLSX file
        OkrugNdu okrugNdu = new OkrugNdu();
        Row rows = rowIterator.next();
        Row rowe = rowIterator.next();
        Row rowr = rowIterator.next();
        int count=0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();


        }
        System.out.println(cityRepo.findAll().size());
        System.out.println(count);
    }
    public  void ParserExelNDU(OkrugNduRepo okrugNduRepo,DeputatRepo deputatRepo) throws IOException {

        File myFile = new File("src/main/resources/testfile.xls");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();

         // Traversi ng over each row of XLSX file
        OkrugNdu okrugNdu = new OkrugNdu();
        Row rows = rowIterator.next();
        Row rowe = rowIterator.next();
        Row rowr = rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();

            if (row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                okrugNdu=okrugNduRepo.findByNumber(Integer.valueOf(String.valueOf(row.getCell(2).getNumericCellValue()).split("\\.")[0]));

                if (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
                    System.out.println(row.getCell(2));
                    System.out.println(row.getCell(4));
                    Deputat deputat = new Deputat();
                    deputat.setName(row.getCell(4).toString().split(" ")[1]);
                    deputat.setSurname(row.getCell(4).toString().split(" ")[0]);
                    deputat.setPartion(row.getCell(4).toString().split(" ")[2]);
                    String []arr= row.getCell(4).toString().split(" ");
                    if (arr.length>3&&arr.length<5) {
                        deputat.setPartia(setPartiaSearch(arr[3]
                                .split("\\(")[1]
                                .split("\\)")[0]));
                    }else
                    {
                        deputat.setPartia(setPartiaSearch(" "));
                    }
                   deputat.setTypeOk(TypeOk.NDY);
                    deputatRepo.saveAndFlush(deputat);
                    okrugNdu.setDeputat(deputat);

                }
                okrugNduRepo.saveAndFlush(okrugNdu);
            }

        }
    }
    public Partia setPartiaSearch(String str)
    {Partia p= Partia.GP;
        switch (str)
        {
            case"БПП":
                return Partia.BPP ;
            case"ОБ":
                return Partia.OB ;
            case"УКРОП":
                return Partia.YKROP;
            case"БЮТ":
                return Partia.BUT;
            case"ВДР":
                return Partia.VDR;
            case"ГС":
                return Partia.GC;
            case"РПЛ":
                return Partia.RPL;
            case"ГП":
                return Partia.GP;
            case"ПЗУ":
                return Partia.PZY;
            case"НК":
                return Partia.NK;
            case"Смпч":
                return Partia.SMPH;
            case"ПЗВ":
                return Partia.PZV;
            case"ПМС":
                return Partia.PMC;
            case"СЛ":
                return Partia.CL;
            case"СВБД":
                return Partia.CVBD;
            case"АП":
                return Partia.AP;
            case"ЗЖ":
                return Partia.ZG;
            case"Слуга":
                return Partia.C;
            case"ЄС":
                return Partia.ES;
            case"СУ":
                return Partia.CY;
            case"НФ":
                return Partia.NF;
            case"ЗАСТ":
                return Partia.ZACT;
            case"КПУ":
                return Partia.KPY;
            case"НДПУ":
                return Partia.NDPY;
            case"ПС":
                return Partia.PC;
            case"НРУ":
                return Partia.NPY;
            case"СЖУ":
                return Partia.SZY;
            case"УС":
                return Partia.YS;
            case"ЗП":
                return Partia.ZP;
            case"НП":
                return Partia.NP;
            case"БЛСУ":
                return Partia.BLCY;
            case"ОПЗЖ":
                return Partia.OPZG;
            case"СОБОР":
                return Partia.COBOR;
            case"ЛПУ":
                return Partia.LPY;
        }
        System.out.println("______________________________");
        return p;
    }
    public  void ParserExelCITY(CityRepo cityRepo,OkrugCityRepo okrugCityRepo,DeputatRepo deputatRepo,MerRepo merRepo) throws IOException {
        File myFile = new File("src/main/resources/testfile.xls");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();

        // Traversi ng over each row of XLSX file

        Row rows = rowIterator.next();
        Row rowe = rowIterator.next();
        Row rowr = rowIterator.next();
        Row rowy = rowIterator.next();
        Map<Integer,String>  okrugobl=new HashMap<Integer, String>();
        String city="";
        City citys=new City();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getCell(9).getCellType()==Cell.CELL_TYPE_STRING
                    && !row.getCell(9).getStringCellValue().equals("")
                    && !row.getCell(9).getStringCellValue().equals(null))
            {
                citys=new City();
                int count = 0;
                count++;
                String str=row.getCell(9).getStringCellValue();
                String str1=row.getCell(11).getStringCellValue();

                if (str.indexOf("м. ")>=0) {

                    System.out.println(cityRepo.findByName(str.split("м. ")[1]));
                    if (cityRepo.findByName(str.split("м. ")[1]) == null)
                    {
                        citys.setName(str.split("м. ")[1].toLowerCase());
                        citys.setTypeCity(TypeCity.city);

                        Mer mer=new Mer();

                        mer.setSurname(str1.split(" ")[0]);
                        mer.setName(str1.split(" ")[1]);
                        mer.setPartion(str1.split(" ")[2]);
                        merRepo.save(mer);
                        citys.setMer(mer);
                        cityRepo.save(citys);
                    }
                }
                else
                {
                    if (cityRepo.findByName(str.split(" ")[0]) == null) {
                        citys.setName(str.split(" ")[0].toLowerCase());
                        if (str.indexOf("сільська") > 1) {
                            citys.setTypeCity(TypeCity.country);

                            Mer mer=new Mer();

                            mer.setSurname(str1.split(" ")[0]);
                            mer.setName(str1.split(" ")[1]);
                            mer.setPartion(str1.split(" ")[2]);
                            merRepo.save(mer);

                            citys.setMer(mer);
                            cityRepo.save(citys);
                        }
                        if (str.indexOf("селищна") > 1) {
                            citys.setTypeCity(TypeCity.city_country);
                            cityRepo.save(citys);
                            Mer mer=new Mer();

                            mer.setSurname(str1.split(" ")[0]);
                            mer.setName(str1.split(" ")[1]);
                            mer.setPartion(str1.split(" ")[2]);
                            merRepo.save(mer);
                            citys.setMer(mer);
                            cityRepo.save(citys);
                        }
                        if (str.indexOf("міська") > 1) {
                            citys.setTypeCity(TypeCity.city_all);

                            Mer mer=new Mer();

                            mer.setSurname(str1.split(" ")[0]);
                            mer.setName(str1.split(" ")[1]);
                            mer.setPartion(str1.split(" ")[2]);
                            merRepo.save(mer);
                            citys.setMer(mer);
                            cityRepo.save(citys);
                        }
                    }


                }

            }

            if (row.getCell(18).getCellType() == Cell.CELL_TYPE_NUMERIC
                    &&row.getCell(20).getCellType()==Cell.CELL_TYPE_STRING) {

                    OkrugCity okrugCity = new OkrugCity();

                    okrugCity.setNumber(Integer
                                    .valueOf(String
                                    .valueOf(row.getCell(18)
                                    .getNumericCellValue())
                                    .split("\\.")[0]));
                    if (city.contains("м. ")) {
                       okrugCity.setCity(citys);
                      try {
                          System.out.println(citys.getName());
                      }catch (Exception e)
                      {}
                    }
                    else
                    {
                     okrugCity.setCity(citys);
                        try {
                            System.out.println(citys.getName());
                        }catch (Exception e)
                        {}
                    }
                       if (!row.getCell(20).getStringCellValue().equals(null) &&
                           !row.getCell(20).getStringCellValue().equals("")   &&
                           !row.getCell(20).getStringCellValue().contains("-")   ){

                           String []arr=row.getCell(20).getStringCellValue().split(" ");
                           Deputat deputat=new Deputat(arr[1],arr[0],arr[2]);
                           if (arr.length>3&&arr.length<5) {
                               deputat.setPartia(setPartiaSearch(arr[3]
                                       .split("\\(")[1]
                                       .split("\\)")[0]));
                           }else
                           {
                               deputat.setPartia(setPartiaSearch(" "));
                           }
                           deputat.setTypeOk(TypeOk.CITY);
                           deputatRepo.saveAndFlush(deputat);
                           okrugCity.setDeputat(deputat);

                           if (row.getCell(37).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                               okrugCity.setRegion(String.valueOf(row.getCell(37).getNumericCellValue()).split("\\.")[0]);
                               System.out.println(city + "|||||||||" + Integer.valueOf(String.valueOf(row.getCell(18).getNumericCellValue()).split("\\.")[0])
                                       + "__________" + row.getCell(20).getStringCellValue() + "||" + row.getCell(37).getNumericCellValue());

                           } else {
                               String str =String.valueOf(row.getCell(37).getStringCellValue());
                               okrugCity.setRegion(WriteDilnizia(str));
                               System.out.println(city + "|||||||||" + Integer.valueOf(String.valueOf(row.getCell(18).getNumericCellValue()).split("\\.")[0])
                                       + "__________" + row.getCell(20).getStringCellValue() + "||" + row.getCell(37).getStringCellValue());
                           }
                           okrugCityRepo.saveAndFlush(okrugCity);
                       }else {

                       }
            }

        }

    }

    public  void ParserExelOBL(OkrugOblRepo okrugOblRepo,DeputatRepo deputatRepo) throws IOException
    {
        File myFile = new File("src/main/resources/testfile.xls");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();

        // Traversi ng over each row of XLSX file

        Row rows = rowIterator.next();
        Row rowe = rowIterator.next();
        Row rowr = rowIterator.next();
        Row rowy = rowIterator.next();
        Map<Integer,String>  okrugobl=new HashMap<Integer, String>();
        String city="";
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getCell(13).getCellType()==Cell.CELL_TYPE_NUMERIC) {
                // For each row, iterate through each columns
                OkrugObl okrugObl = new OkrugObl();
                okrugObl.setNumber(Integer.valueOf(
                        String.valueOf(
                                row.getCell(13)
                                        .getNumericCellValue())
                                .split("\\.")[0]));
                String[] arr = row.getCell(15).getStringCellValue().split(" ");
                Deputat deputat;
                if (arr.length>2)
                {
                 deputat = new Deputat(
                arr[1],arr[0],arr[2]);
                }
                else
                {
                    arr=row.getCell(14).getStringCellValue().split(" ");
                     deputat = new Deputat(
                            arr[1],arr[0],arr[2]);
                }


                deputat.setTypeOk(TypeOk.OBLAST);

                if (arr.length > 3 && arr.length < 5) {
                    deputat.setPartia(setPartiaSearch(arr[3]
                            .split("\\(")[1]
                            .split("\\)")[0]));
                } else {
                    deputat.setPartia(setPartiaSearch(" "));
                }
                deputatRepo.saveAndFlush(deputat);
                okrugObl.setDeputat(deputat);
                if (row.getCell(34).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    okrugObl.setRegion(String.valueOf(row.getCell(34).getNumericCellValue()).split("\\.")[0]);
                    System.out.println(city + "|||||||||" + Integer.valueOf(String.valueOf(row.getCell(13).getNumericCellValue()).split("\\.")[0])
                            + "__________" + row.getCell(15).getStringCellValue() + "||" + row.getCell(34).getNumericCellValue());
                } else {
                String str =String.valueOf(row.getCell(34).getStringCellValue());
                    okrugObl.setRegion(WriteDilnizia(str));
                    System.out.println(city + "|||||||||" + Integer.valueOf(String.valueOf(row.getCell(13).getNumericCellValue()).split("\\.")[0])
                            + "__________" + row.getCell(15).getStringCellValue() + "||" + row.getCell(34).getStringCellValue());
                }

                okrugOblRepo.saveAndFlush(okrugObl);
            }
        }
//13,15,35

    }
    public   String WriteDilnizia(String region)
    {String str="";
        System.out.println("11");
        String [] koma=region.split(",");

        for (String ar: koma)
        {System.out.println("22");
            if(ar.contains("-"))
            {System.out.println("33");
                if (ar.split("-").length<1) {
                }else
                {
                    for (Integer i=Integer.valueOf(ar.split("-")[0].trim());i<=Integer.valueOf(ar.split("-")[1].trim());i++)
                    {
                        str+=i+" ";
                    }
                 }
            }
            else
            {
                str+=ar.trim()+" ";
                System.out.println("44");
            }
        }


        return str;
    }
    public void ParserDilnuzia_Okrugs(OkrugOblRepo okrugOblRepo,OkrugCityRepo okrugCityRepo, DilniziaRepo dilniziaRepo)
    {

        List <Dilnizia> dilnizias=dilniziaRepo.findAll();
        for (Dilnizia dilnizia: dilnizias ) {
         try{
             OkrugObl okrugObl=  okrugOblRepo.findByRegionContaining(dilnizia.getNumber().toString());
            dilnizia.setOkrugObl(okrugObl);
             dilniziaRepo.saveAndFlush(dilnizia);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        }
      dilnizias=dilniziaRepo.findAll();
        for (Dilnizia dilnizia: dilnizias ) {
         try {
             OkrugCity okrugCity = okrugCityRepo.findByRegionContaining(dilnizia.getNumber().toString());

             dilnizia.setOkrugCity(okrugCity);
             dilniziaRepo.saveAndFlush(dilnizia);
         }catch (Exception e)
         {
             e.printStackTrace();
         }


        }

    }

}
