package org.com.deputatbot.bot;



import org.com.deputatbot.domain.*;
import org.com.deputatbot.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
/*
* postgres://scimigdnkpbyzr:3a3320c57b1d16ca45b49f5a6f0bdd8cfe8daf92577544b1cd689620e04d6331@ec2-54-228-243-238.eu-west-1.compute.amazonaws.com:5432/dcku3g1n2qlfrp
*scimigdnkpbyzr
* 3a3320c57b1d16ca45b49f5a6f0bdd8cfe8daf92577544b1cd689620e04d6331
 * */

@Service
public class Bot extends TelegramLongPollingBot {

    @Value("${token.bot}")
    private String tokenBot;
    @Value(("${tokenname.bot}"))
    private String tokenNameBot;
private String info="Щоб з’ясувати, які депутати представляють " +
        "твій населений пункт в Верховній Раді, а " +
        "також в місцевих радах - міській, обласній, " +
        "сільській або селищній раді, потрібно " +
        "вказати свою адресу за формою: місто, село " +
        "або ж смт, а також вулицю. **Наприклад, " +
        "Верхньодніпровськ, Дніпровська-1**" +
        "\n" +
        "Після цього я надішлю ФІО народного " +
        "депутата України від твого округу, а також " +
        "депутата обласної, міської, селищної чи " +
        "сільської ради, в залежності від твого місця " +
        "проживання. Крім того, ти дізнаєшся, хто " +
        "очолює твоє місто чи то ОТГ, або ж село.";
private String start="Привіт, друже, я бот. Допоможу знайти твого депутата \uD83D\uDE0E";

String sorry="Напевно ви мали на увазі :";

    @Autowired
     private UserRepo userRepo;
    @Autowired
    private DeputatRepo deputatRepo;
    @Autowired
    private OkrugNduRepo okrugNduRepo;
    @Autowired
    private DilniziaRepo dilniziaRepo;
    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private MerRepo merRepo;
    @Autowired
    private OkrugCityRepo okrugCityRepo;
    @Autowired
    private OkrugOblRepo okrugOblRepo;

    private boolean support_admin =false;
    @PostConstruct
    public void construct() throws IOException { TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    Parser parser =new Parser();

    parser.allNduOkrug(okrugNduRepo,dilniziaRepo,deputatRepo);
    parser.ParserExelNDU(okrugNduRepo,deputatRepo);
    parser.ParserExelOBL(okrugOblRepo,deputatRepo,dilniziaRepo);
    parser.ParserExelCITY(cityRepo,okrugCityRepo,deputatRepo,merRepo,dilniziaRepo);

    }



    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage())
        if(update.getMessage().getText()!=null)
        {System.out.println(update.getMessage().getChatId());
            String text=update.getMessage().getText();
            switch (text){

                case"/start":
                    List<KeyboardRow> arr=new ArrayList<>();
                    KeyboardRow l=new KeyboardRow();
                    l.add(new KeyboardButton("@ObranetsBot скажи, хто мій депутат?"));
                    arr.add(l);
                    ReplyKeyboard j=new ReplyKeyboardMarkup();
                    ((ReplyKeyboardMarkup) j)
                            .setKeyboard(arr)
                            .setSelective(true)
                            .setResizeKeyboard(true)
                            .setOneTimeKeyboard(true);
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(start).setReplyMarkup(j));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "@ObranetsBot скажи, хто мій депутат?":
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(info));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:

                       if (update.getMessage().getText().indexOf("/id_")>=0)
                       {
                            try {
                                sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText("в розробці"));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                       }else
                       try {

                           OkrugNdu okrugNdu=new OkrugNdu();
                           OkrugObl okrugObl=new OkrugObl();
                           OkrugCity okrugCity=new OkrugCity();
                           Mer mer=new Mer();
                           City city=new City();
                           TypeCity typeCity;
                           Deputat deputat;
                            Dilnizia str=new Dilnizia();
                            str=searchDilnizia(update.getMessage().getText(),update.getMessage().getChatId().toString());
                           if(str!=null){
                               System.out.println(str.getNumber());
                           String info = "";
                           okrugNdu=str.getOkrugNdu();
                           okrugObl=str.getOkrugObl();
                           okrugCity=str.getOkrugCity();
                           try {
                               city=okrugCity.getCity();
                           }catch (Exception e){}
                           mer=city.getMer();
                           typeCity=city.getTypeCity();
                                try {
                                        info += "Твій депутат Верховної Ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += str.getOkrugNdu().getNumber() + "\n";
                                            try {
                                                deputat=okrugNdu.getDeputat();
                                                info += deputat.getSurname().toUpperCase() + " "
                                                        + deputat.getName().toUpperCase() + " "
                                                        + deputat.getPartion().toUpperCase() +
                                                        "\n /id_" + deputat.getId() + "\n";

                                            } catch (Exception e) {
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {     }
                                        if (typeCity == TypeCity.city||typeCity==TypeCity.city_all) {
                                        info += "\nТвій мер " +
                                                "м." + city.getName() + "\n";
                                    }
                                        else{
                                        info += "\nТвій голова селищної ради\n"
                                                + city.getName() + " " + typeCity.GetTitle() + "\n";
                                    }
                                        try {
                                            info += mer.getSurname().toUpperCase() + " "
                                                    + mer.getName().toUpperCase() + " "
                                                    + mer.getPartion().toUpperCase() +
                                                    "\n /id_" + mer.getId() + "\n";
                                            ;
                                        } catch (Exception e) {      }
                                        info += "\nТвій депутат обласної ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += okrugObl.getNumber() + "\n";
                                            try {
                                                for (Deputat dep: deputatRepo.findAllByOkrugObl(okrugObl)) {
                                                   deputat=dep;
                                                   info += deputat.getSurname().toUpperCase() + " "
                                                         + deputat.getName().toUpperCase() + " "
                                                         + deputat.getPartion().toUpperCase() +
                                                         "\n /id_" + deputat.getId() + "\n";
                                                }

                                            } catch (Exception e) {
                                                if (deputatRepo.findAllByOkrugObl(okrugObl).size()==0)
                                                    info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {     }
                                        if (typeCity == TypeCity.city||typeCity==TypeCity.city_all) {
                                        info += "\nТвій депутат міської ради\n" +
                                                "Округ №" +okrugCity.getNumber()+ "\n";
                                    }
                                        else{
                                        info += "\nТвій депутат селищної ради\n" +
                                                "Округ "+okrugCity.getNumber() + "\n";
                                             }
                                        try {
                                            try {
                                                if(deputatRepo.findAllByOkrugCity(okrugCity).size()<1)
                                                    Integer.valueOf("asd");
                                                for (Deputat dep: deputatRepo.findAllByOkrugCity(okrugCity))
                                                {
                                                    deputat=dep;
                                                    info += deputat.getSurname().toUpperCase() + " "
                                                            + deputat.getName().toUpperCase() + " "
                                                            + deputat.getPartion().toUpperCase() +
                                                            "\n /id_" + deputat.getId() + "\n";
                                                }
                                            } catch (Exception e) {
                                                if (deputatRepo.findAllByOkrugCity(okrugCity).size()==0)
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {       }




                                }catch (Exception er)
                                {

                                }
                                sendApiMethod(new SendMessage().setText(info).setChatId(update.getMessage().getChatId()));
                            }

                       }catch (Exception e)
                       {e.printStackTrace();
                       }
                       break;



                }
            }
        }




    private static List<Dilnizia> intersectArrays(List<Dilnizia> a, List<Dilnizia> b) {
        List<Dilnizia>er=new ArrayList<>();
        for (Dilnizia ar:a)
        {
            for (Dilnizia ar1:b)
            {
                System.out.println(ar.getNumber()+"||||||||||||||"+ar1.getNumber());
                if (ar.getNumber().equals(ar1.getNumber()))
                {
                    System.out.println("equals");
                    er.add(ar);
                    System.out.println(ar.getNumber()+"++++++++++++++++++++++"+ar1.getNumber());
                }
            }
        }


        return er;
    }
    public Dilnizia searchDilnizia(String str,String chat)
    {
        Dilnizia dilnizia;
        str=str.toLowerCase();


        if (str.split(" ").length==1)
        {
            System.out.println(str.split(" ").length);
            List<Dilnizia> dilnizias=dilniziaRepo.findAllByRegionContaining(str.trim().toLowerCase());

            if (dilnizias.size()!=0)
                return dilnizias.get(0);
            else {
                try {
                    sendApiMethod(new SendMessage().setChatId(chat).setText("Ваша адреса не знайдена, введіть за прикладом:**Верхньодніпровськ, Дніпровська-1**\n"+
                            "Якщо ви проживаєте в селі або в селищі, скористайтесть таким записом: **Терни**"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        String city="";
        String name="";
        String number="";
        if (str.split(" ").length==2)
        {
            city=str.split(",")[0].trim();
            name=str.split(",")[1].trim();
        }

        try {
            String[] allStr=allStr(str);

         city=allStr[0].trim();
         name=allStr[1].trim();
         number=allStr[2].trim();
        }catch (Exception e){}
        System.out.println(city+" "+name+" "+number);
        List<Dilnizia>cities=new ArrayList<>();
        List<Dilnizia>names=new ArrayList<>();


        cities=dilniziaRepo.findAllByRegionContaining(city);
        if (cities.size()==1)
        {
            dilnizia=cities.get(0);
         return dilnizia;
        }
        if (cities.size()==0)
        {
            return null;
        }

        for (Dilnizia dilnizias:cities)
        {
            if (dilnizias.getRegion().indexOf(name)>0)
            {
                names.add(dilnizias);
            }
        }
        if (names.size()==1)
        {
            dilnizia=names.get(0);
            return dilnizia;
        }
        if (names.size()==0)
        {
            return null;
        }
        for (Dilnizia dilnizias: names)
        {
            String regions=dilnizias.getRegion();
            try {
                String[]adress= regions.split(";");
                for (String ad:adress)
                {
                    if (ad.indexOf(name)>0)
                    { System.out.println(dilnizias.getNumber()+" "+ad);
                      String address=ad;
                       String adess=address.split(":")[1];
                        try {
                            if (adess.indexOf(number.trim())>=0){

                                return dilnizias;
                            }
                            for (String rty:adess.split(","))
                            {


                                if (rty.indexOf(number.trim())>=0)
                                    return dilnizias;
                                try {
                                    String[]ert=rty.trim().split("–");
                                    int num=Integer.valueOf(number.trim());
                                    int num1=Integer.valueOf(ert[0].trim());
                                    int num2=Integer.valueOf(ert[1].trim());
                                    System.out.println(num+" "+num1+" "+num2);
                                    if(num<num2&&num>num1)
                                    {
                                        dilnizia=dilnizias;
                                        return dilnizia;
                                    }
                                    if (num==num1||num==num2)
                                    {
                                        dilnizia=dilnizias;
                                        return dilnizia;
                                    }
                                }catch (Exception er)
                                {
                                }
                            }
                        }catch (Exception e)
                        {
                            try {
                                String[]ert=address.split("-");
                                int num=Integer.valueOf(number.trim());
                                int num1=Integer.valueOf(ert[0].trim());
                                int num2=Integer.valueOf(ert[1].trim());
                                if(num<num2&&num>num1)
                                {
                                    dilnizia=dilnizias;
                                    return dilnizia;
                                }
                                if (num==num1||num==num2)
                                {
                                    dilnizia=dilnizias;
                                    return dilnizia;
                                }
                            }catch (Exception er)
                            {
                             if(address.indexOf(number.trim())>=0)
                             {
                                 dilnizia=dilnizias;
                                 return dilnizia;
                             }
                            }
                        }
                    }
                    else {
                        System.out.println("null2");


                    }
                }

            }catch (Exception e)
            {
                dilnizia=dilnizias;
                return dilnizia;
            }
        }
        return null;

    }
    public String[] allStr(String request)
    {

        String[] split1=request.split(",");
        String[] split2=new String[]{};
        try {
            split2=split1[1].split("-");


        }catch (Exception e){
            return new String[]{split1[0],split1[1]};
        }
        System.out.println(split1[0]+" "+split2[0] +" "+split2[1]);
        return new String[]{split1[0],split2[0],split2[1]};
    }
    @Override
    public String getBotUsername() {
        return tokenNameBot;///""@Documents_in_Poland_bot;@warsaww_bot
    }

    @Override
    public String getBotToken() {
        return tokenBot;// 808617170:AAF58eibRG7whQZkJAI3ounVnN__2TRbFEo|||827804459:AAEhCYbx6DhbZDsoUroynFmqf2f57yDqzaw
    }
}
