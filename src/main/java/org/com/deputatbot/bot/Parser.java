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

import java.io.*;
import java.util.*;
@Service
public class Parser {
        private int counte=0;
    public void allNduOkrug(OkrugNduRepo okrugNduRepo, DilniziaRepo dilniziaRepo, DeputatRepo  deputatRepo)  {
        for (Integer i=24;i<41;i++)
        {
            ParserSite(i, okrugNduRepo,dilniziaRepo,deputatRepo);
        }
        for (Integer i=211;i<224;i++)
        {
            ParserSite(i, okrugNduRepo,dilniziaRepo,deputatRepo);
        }
    }

    public  void ParserSite (Integer number_nduokrug, OkrugNduRepo okrugNduRepo, DilniziaRepo dilniziaRepo, DeputatRepo deputatRepo)  {
        String mylink ="https://www.drv.gov.ua/ords/portal/!cm_core.cm_index?option=ext_dvk&pid100=12&pf5271="+number_nduokrug+"&prejim=3";
        OkrugNdu okrugNdu =new OkrugNdu();
        okrugNdu.setNumber(number_nduokrug);



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
        ArrayList<Integer>num=new ArrayList<>();
        ArrayList<String>reg=new ArrayList<>();
        for (Element el:form.getElementsByTag("tr"))
        {
            System.out.println(el.tagName());
            int count=0;
            for(Element al:el.getElementsByTag("td"))
            {
                if (count<2) {
                    if (count==0) {
                        num.add(Integer.valueOf(al.text()));
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

        String str="";
        int count=0;
        for (Integer l:num)
        {

            str+=l+",";
        }
        okrugNdu.setRegion(str.substring(0,str.length()-2));

        okrugNduRepo.save(okrugNdu);
        for (Integer l:num)
        {Dilnizia dilnizia=new Dilnizia();
            dilnizia.setNumber(l);
            dilnizia.setRegion(reg.get(con++).toLowerCase());
            dilnizia.setOkrugNdu(okrugNdu);
            dilniziaRepo.save(dilnizia);
        }


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

            if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                try {
                    okrugNdu=okrugNduRepo.findByNumber(Integer.valueOf(String.valueOf(row.getCell(1).getNumericCellValue()).split("\\.")[0]));
                        System.out.println(okrugNdu.getNumber());
                    if (row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
                        System.out.println(row.getCell(1));
                        System.out.println(row.getCell(2));

                        Deputat deputat = new Deputat();
                        deputat.setName(row.getCell(2).toString().split(" ")[1]);
                        deputat.setSurname(row.getCell(2).toString().split(" ")[0]);
                        deputat.setPartion(row.getCell(2).toString().split(" ")[2]);
                        String []arr= row.getCell(2).toString().split(" ");
                        if (arr.length>3&&arr.length<5) {
                            deputat.setPartia(setPartiaSearch(arr[3]
                                    .split("\\(")[1]
                                    .split("\\)")[0]));
                        }else
                        {
                            deputat.setPartia(setPartiaSearch(" "));
                        }
                        deputat.setTypeOk(TypeOk.NDY);
                        deputatRepo.save(deputat);
                        System.out.println("save1");
                        okrugNdu.setDeputat(deputat);
                        System.out.println("save2");

                    }
                    okrugNduRepo.save(okrugNdu);
                }catch (Exception e){e.printStackTrace();}
            }

        }
    }

    public  void ParserExelNDUKuev(OkrugNduRepo okrugNduRepo,DeputatRepo deputatRepo) throws IOException {

        File myFile = new File("src/main/resources/testfile1.xls");
        FileInputStream fis = new FileInputStream(myFile);

        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);

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

            if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                try {
                    okrugNdu=okrugNduRepo.findByNumber(Integer.valueOf(String.valueOf(row.getCell(2).getNumericCellValue()).split("\\.")[0]));
                    System.out.println(okrugNdu.getNumber());
                    if (row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
                        System.out.println(row.getCell(2));
                        System.out.println(row.getCell(1));

                        Deputat deputat = new Deputat();
                        deputat.setName(row.getCell(2).toString().split(" ")[1]);
                        deputat.setSurname(row.getCell(2).toString().split(" ")[0]);
                        deputat.setPartion(row.getCell(2).toString().split(" ")[2]);
                        String []arr= row.getCell(2).toString().split(" ");
                        if (arr.length>3&&arr.length<5) {
                            deputat.setPartia(setPartiaSearch(arr[3]
                                    .split("\\(")[1]
                                    .split("\\)")[0]));
                        }else
                        {
                            deputat.setPartia(setPartiaSearch(" "));
                        }
                        deputat.setTypeOk(TypeOk.NDY);
                        deputatRepo.save(deputat);
                        System.out.println("save1");
                        okrugNdu.setDeputat(deputat);
                        System.out.println("save2");

                    }
                    okrugNduRepo.save(okrugNdu);
                }catch (Exception e){e.printStackTrace();}
            }

        }
    }
    public Partia setPartiaSearch(String str){
        System.out.println(str);
        Partia p= Partia.GP;
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

        return p;
    }

    public  void ParserExelOBL(OkrugOblRepo okrugOblRepo,DeputatRepo deputatRepo, DilniziaRepo dilniziaRepo) throws IOException  {
        File myFile = new File("src/main/resources/testfile.xls");
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();

        // Traversi ng over each row of XLSX file


        OkrugObl okrug=new OkrugObl();
        double long_okrug;
        String str_deputat="";
        String str_dilnizia="";
        Row row = rowIterator.next();
        row = rowIterator.next();
        row = rowIterator.next();
        row = rowIterator.next();;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();

            Cell okruge = row.getCell(5);
            Cell deputats = row.getCell(6);
            Cell dilnizia = row.getCell(12);




            if (!deputats.getStringCellValue().equals(null)) {
                Deputat deputat=new Deputat();
                System.out.println(okrug.getNumber());
                if (dilnizia.getCellType() == Cell.CELL_TYPE_STRING) {

                    str_dilnizia = dilnizia.getStringCellValue();

                    long_okrug = okruge.getNumericCellValue();

                    okrug=okrugOblRepo.findByNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                   if (okrug!=null) {
                       okrug.setRegion(okrug.getRegion()+str_dilnizia);
                       okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                   }
                   else {
                       okrug=new OkrugObl();
                       okrug.setRegion(str_dilnizia);

                       okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));
                   }
                    okrugOblRepo.save(okrug);

                    System.out.println("dilnizia: " + str_dilnizia + " okrug: " + long_okrug);

                    str_dilnizia = WriteDilnizia(str_dilnizia);

                    for (String str:str_dilnizia.split(" "))
                    {
                        Dilnizia dilnizia1=new Dilnizia();
                        try {
                            Long number=Long.valueOf(str.trim());

                            dilnizia1= dilniziaRepo.findByNumber(Integer.valueOf(String.valueOf(number).split("\\.")[0]));

                            dilnizia1.setOkrugObl(okrug);

                            dilniziaRepo.save(dilnizia1);

                        }catch (Exception e){}


                    }

                }
                if (dilnizia.getCellType() == Cell.CELL_TYPE_NUMERIC) {

                    long_okrug = okruge.getNumericCellValue();


                    okrug=okrugOblRepo.findByNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                    if (okrug!=null) {
                        okrug.setRegion(okrug.getRegion()+str_dilnizia);

                        okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));
                    }
                    else {
                        okrug=new OkrugObl();
                        okrug.setRegion(str_dilnizia);

                        okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));
                    }
                    okrugOblRepo.save(okrug);

                    str_dilnizia = String.valueOf(dilnizia.getNumericCellValue()).split("\\.")[0];

                    Dilnizia dilnizia1=new Dilnizia();

                    dilnizia1=dilniziaRepo.findByNumber(Integer.valueOf(str_dilnizia.trim()));

                    dilnizia1.setOkrugObl(okrug);

                    dilniziaRepo.save(dilnizia1);

                    System.out.println("dilnizia: " + str_dilnizia + " okrug: " + long_okrug);


                }
                if (!str_dilnizia.equals("") && !str_dilnizia.equals(null)) {
                    str_deputat = deputats.getStringCellValue();


                    String [] array_deputat=str_deputat.split(" ");
                    if (array_deputat.length>2)
                    {
                        deputat.setSurname(array_deputat[0]);

                        deputat.setName(array_deputat[1]);

                        deputat.setPartion(array_deputat[2]);

                        deputat.setTypeOk(TypeOk.OBLAST);

                        try {
                            if (array_deputat.length>3&array_deputat.length<5)
                                deputat.setPartia(setPartiaSearch(array_deputat[3]));
                            else deputat.setPartia((setPartiaSearch(" ")));
                        }catch (Exception e)
                        {
                            deputat.setPartia(Partia.GP);
                        }

                        for (Deputat dep:deputatRepo.findAllByOkrugObl(okrug))
                        {
                            if (dep.getSurname()==deputat.getSurname())
                            {}
                            else{
                            }
                        }
                        if (deputatRepo.findAllByOkrugObl(okrug).size()==0)
                        deputat.setOkrugObl(okrug);

                        deputatRepo.save(deputat);
                        System.out.println("deputat: " + array_deputat[0] + " " + array_deputat[1] + " " + array_deputat[2]);

                    }


                }



            }
        }

    }

    public  void ParserExelCITY(CityRepo cityRepo,OkrugCityRepo okrugCityRepo,DeputatRepo deputatRepo,MerRepo merRepo, DilniziaRepo dilniziaRepo) throws IOException {
        File myFile = new File("src/main/resources/testfile.xls");
        FileInputStream fis = new FileInputStream(myFile);

        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = mySheet.iterator();





        City city=new City();
        OkrugCity okrug=new OkrugCity();
        Mer mer=new Mer();
        int count=0;
        String str_mer="";
        String str_city="";
        double long_okrug;
        String str_deputat="";
        String str_dilnizia="";
        Row row = rowIterator.next();
        row = rowIterator.next();
        row = rowIterator.next();
        row = rowIterator.next();;
        int coun=0;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell mere = row.getCell(4);
            Cell cities = row.getCell(3);
            Cell okruge = row.getCell(7);
            Cell deputats = row.getCell(8);
            Cell dilnizia = row.getCell(14);


            if (!cities.getStringCellValue().equals("") && !cities.getStringCellValue().equals(null)) {
                city=new City();
                System.out.println("#################################");

                count++;

                str_city = cities.getStringCellValue();

                str_mer = mere.getStringCellValue();

                String[] array_city = str_city.split(" ");

                String[] array_mer = str_mer.split(" ");

                mer=new Mer(array_mer[1],array_mer[0],array_mer[2]);

                merRepo.save(mer);

                System.out.println("mer: " + array_mer[0] + " " + array_mer[1] + " " + array_mer[2] + "!!!!!!!!!!");

                if (str_city.contains("громада")) {
                    city = new City();
                    city.setMer(mer);
                    city.setName(array_city[0]);

                    if (str_city.contains("сільська")) {
                        coun += 1;
                        city.setTypeCity(TypeCity.country);
                        System.out.println("СІльська громада: " + array_city[0] + " " + array_city[1] + " " + array_city[2] + "!!!!!!!!!!");

                    }
                    if (str_city.contains("селищна")) {
                        coun += 1;
                        city.setTypeCity(TypeCity.city_country);
                        System.out.println("селищна громада: " + array_city[0] + " " + array_city[1] + " " + array_city[2] + "!!!!!!!!!!");

                    } else if (str_city.contains("міська"))
                    {
                        System.out.println("міська громада: " + array_city[0] + " " + array_city[1] + " " + array_city[2] + "!!!!!!!!!!");
                        city.setTypeCity(TypeCity.city_all);
                    }
                }
                else if (array_city.length >= 2) {
                    city=new City();
                    city.setMer(mer);
                    String te="";
                    coun+=1;
                    for (int i =1;i<array_city.length;i++)
                    {
                        te+=array_city[i];
                    }
                    System.out.println("city: " + array_city[0] + " " + array_city[1] + " " + "!!!!!!!!!!");
                    city.setName(te.trim());
                    city.setTypeCity(TypeCity.city);

                }
                cityRepo.save(city);
            }


                if (okruge.getCellType()==Cell.CELL_TYPE_NUMERIC&&dilnizia.getCellType() == Cell.CELL_TYPE_STRING) {

                    str_dilnizia = dilnizia.getStringCellValue();

                    long_okrug = okruge.getNumericCellValue();

                    okrug=new OkrugCity();

                    okrug.setCity(city);

                    okrug.setRegion(str_dilnizia);

                    okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                    okrugCityRepo.save(okrug);

                    System.out.println("dilnizia: " + str_dilnizia + " okrug: " + long_okrug+"::::"+city.getName());

                    str_dilnizia = WriteDilnizia(str_dilnizia);

                    for (String str:str_dilnizia.split(" "))
                    {
                        Dilnizia dilnizia1=new Dilnizia();
                        try {
                            Long number=Long.valueOf(str.trim());

                            dilnizia1= dilniziaRepo.findByNumber(Integer.valueOf(String.valueOf(number).split("\\.")[0].trim()));

                            dilnizia1.setOkrugCity(okrug);

                            dilniziaRepo.save(dilnizia1);

                        }catch (Exception e){}


                    }

                }
                else if (okruge.getCellType()==Cell.CELL_TYPE_NUMERIC&&dilnizia.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    counte++;
                    long_okrug = okruge.getNumericCellValue();

                    okrug=new OkrugCity();

                    okrug.setCity(city);

                    okrug.setRegion(str_dilnizia);

                    okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                    okrugCityRepo.save(okrug);

                    str_dilnizia = String.valueOf(dilnizia.getNumericCellValue()).split("\\.")[0];

                    Dilnizia dilnizia1=dilniziaRepo.findByNumber(Integer.valueOf(str_dilnizia.trim()));
                    if (dilnizia1!=null) {
                        dilnizia1.setOkrugCity(okrug);
                        dilniziaRepo.save(dilnizia1);
                    }
                    try {
                        System.out.println("dilnizia:-" + dilnizia1.getNumber() + "-okrug: " + okrug.getNumber()+"::::"+city.getName());
                    }catch (Exception e){}


                }
                if (okruge.getCellType()==Cell.CELL_TYPE_NUMERIC)
                {if (Integer.valueOf(String.valueOf(okruge.getNumericCellValue()).split("\\.")[0])==0)
                {
                    okrug=new OkrugCity();
                }}
                if (!str_dilnizia.equals("") && !str_dilnizia.equals(null)) {
                    try {
                        okrug.getNumber();
                        if (!deputats.getStringCellValue().equals(null)) {
                            Deputat deputat=new Deputat();
                            str_deputat = deputats.getStringCellValue();
                            String [] array_deputat=str_deputat.split(" ");
                            if (array_deputat.length>2) {
                                deputat.setSurname(array_deputat[0]);

                                deputat.setName(array_deputat[1]);

                                deputat.setPartion(array_deputat[2]);

                                deputat.setTypeOk(TypeOk.CITY);

                                try {
                                    if (array_deputat.length > 3 & array_deputat.length < 5)
                                        deputat.setPartia(setPartiaSearch(array_deputat[3]));
                                    else deputat.setPartia((setPartiaSearch(" ")));
                                } catch (Exception e) {
                                    deputat.setPartia(Partia.AP);
                                }

                                System.out.println("deputat: " + array_deputat[0] + " " + array_deputat[1] + " " + array_deputat[2] + "::::" + city.getName());

                                deputat.setOkrugCity(okrug);

                                deputatRepo.save(deputat);
                            }
                        }
                    }catch (Exception e){

                    }

                }


        }
        System.out.println(coun);
        System.out.println(cityRepo.findAll().size());
        System.out.println(counte);
        System.out.println(dilniziaRepo.findAll().size());
    }

    public  void ParserExelCITYKuev(CityRepo cityRepo,OkrugCityRepo okrugCityRepo,DeputatRepo deputatRepo,MerRepo merRepo, DilniziaRepo dilniziaRepo) throws IOException {
        File myFile = new File("src/main/resources/testfile1.xls");
        FileInputStream fis = new FileInputStream(myFile);

        HSSFWorkbook myWorkBook = new HSSFWorkbook (fis);
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = mySheet.iterator();





        City city=new City();
        OkrugCity okrug=new OkrugCity();
        Mer mer=new Mer();
        int count=0;
        String str_mer="";
        String str_city="";
        double long_okrug;
        String str_deputat="";
        String str_dilnizia="";
        Row row = rowIterator.next();
        row = rowIterator.next();
        row = rowIterator.next();
        row = rowIterator.next();;


        city.setName("Київ");
        city.setTypeCity(TypeCity.city);
        mer.setName("Віталій");
        mer.setSurname("Кличко");
        mer.setPartion("Володимирович");
        merRepo.save(mer);
        city.setMer(mer);

        cityRepo.save(city);
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell okruge = row.getCell(4);
            Cell deputats = row.getCell(5);
            Cell dilnizia = row.getCell(9);





            if (!deputats.getStringCellValue().equals(null)) {
                Deputat deputat=new Deputat();

                if (dilnizia.getCellType() == Cell.CELL_TYPE_STRING) {

                    str_dilnizia = dilnizia.getStringCellValue();

                    long_okrug = okruge.getNumericCellValue();

                    okrug=new OkrugCity();

                    okrug.setCity(city);

                    okrug.setRegion(str_dilnizia);

                    okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                    okrugCityRepo.save(okrug);
                    System.out.println("-------------------------------------------------");
                    System.out.println("dilnizia: " + str_dilnizia + " okrug: " + long_okrug+"::::"+city.getName());

                    str_dilnizia = WriteDilnizia(str_dilnizia);

                    for (String str:str_dilnizia.split(" "))
                    {
                        Dilnizia dilnizia1=new Dilnizia();
                        try {
                            Long number=Long.valueOf(str.trim());

                            dilnizia1= dilniziaRepo.findByNumber(Integer.valueOf(String.valueOf(number).split("\\.")[0].trim()));

                            dilnizia1.setOkrugCity(okrug);

                            dilniziaRepo.save(dilnizia1);

                        }catch (Exception e){}


                    }

                }
                if (dilnizia.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    counte++;
                    long_okrug = okruge.getNumericCellValue();

                    okrug=new OkrugCity();

                    okrug.setCity(city);

                    okrug.setRegion(str_dilnizia);

                    okrug.setNumber(Integer.valueOf(String.valueOf(long_okrug).split("\\.")[0]));

                    okrugCityRepo.save(okrug);

                    str_dilnizia = String.valueOf(dilnizia.getNumericCellValue()).split("\\.")[0];

                    Dilnizia dilnizia1=dilniziaRepo.findByNumber(Integer.valueOf(str_dilnizia.trim()));
                    if (dilnizia1!=null) {
                        dilnizia1.setOkrugCity(okrug);
                        dilniziaRepo.save(dilnizia1);
                    }
                    try {
                        System.out.println("dilnizia:-" + dilnizia1.getNumber() + "-okrug: " + okrug.getNumber()+"::::"+city.getName());
                    }catch (Exception e){}


                }
                if (!str_dilnizia.equals("") && !str_dilnizia.equals(null)) {
                    str_deputat = deputats.getStringCellValue();
                    String [] array_deputat=str_deputat.split(" ");
                    if (array_deputat.length>2)
                    {
                        deputat.setSurname(array_deputat[0]);

                        deputat.setName(array_deputat[1]);

                        deputat.setPartion(array_deputat[2]);

                        deputat.setTypeOk(TypeOk.CITY);

                        try {
                            if (array_deputat.length>3&array_deputat.length<5)
                                deputat.setPartia(setPartiaSearch(array_deputat[3]));
                            else deputat.setPartia((setPartiaSearch(" ")));
                        }catch (Exception e)
                        {
                            deputat.setPartia(Partia.AP);
                        }

                        System.out.println("deputat: " + array_deputat[0] + " " + array_deputat[1] + " " + array_deputat[2]+"::::"+city.getName());

                        deputat.setOkrugCity(okrug);

                        deputatRepo.save(deputat);
                    }
                }
            }

        }

        System.out.println(cityRepo.findAll().size());
        System.out.println(counte);
        System.out.println(dilniziaRepo.findAll().size());
    }


    public   String WriteDilnizia(String dilnizias) {String str="";

        String [] koma=dilnizias.split(",");

        for (String ar: koma)
        {
            if(ar.contains("-"))
            {
                if (ar.split("-").length<1) {
                }else
                {
                    for (Integer i=Integer.valueOf(ar.split("-")[0].trim());i<=Integer.valueOf(ar.split("-")[1].trim());i++)
                    {
                        counte++;
                        str+=i+" ";
                    }
                 }
            }
            else
            {
                str+=ar.trim()+" ";
            }
        }


        return str;
    }


}
/*
spring.datasource.url=jdbc:postgresql://localhost/lions

spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create
spring.freemarker.expose-request-attributes=true

spring.jpa.properties.hibernate.jdbs.lob.non_contextual_creation=true



upload.path=src/main/resources/photos
token.bot=851210991:AAEJhjujEK7z5e_SfmPevHeWLP0KiK0AHmA
tokenname.bot=@Polled_bot

spring.datasource.url=jdbc:postgres://fprmgdvzfmwmpn:c7f8034eb9078dcbd430cad1c4ae14d35f1cf7c2afd70675b348458bf9c1afc0@ec2-54-75-230-253.eu-west-1.compute.amazonaws.com:5432/d6ggrl973ges34


spring.datasource.username=fprmgdvzfmwmpn
spring.datasource.password=c7f8034eb9078dcbd430cad1c4ae14d35f1cf7c2afd70675b348458bf9c1afc0
spring.jpa.generate-ddl=true
spring.freemarker.expose-request-attributes=true

spring.jpa.properties.hibernate.jdbs.lob.non_contextual_creation=true



upload.path=src/main/resources/photos
token.bot=874198635:AAHJYsciMaxQQOA3R96IsBc4ZWfszSXTop4
tokenname.bot=@ObranetsBot

*/