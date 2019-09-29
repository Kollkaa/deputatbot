package org.com.deputatbot.bot;



import org.com.deputatbot.domain.City;
import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.domain.Mer;
import org.com.deputatbot.domain.TypeCity;
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
private String info="Щоб з’ясувати, які депутати представляють \n" +
        "твій населений пункт в Верховній Раді, а \n" +
        "також в місцевих радах - міській, обласній, \n" +
        "сільській або селищній раді, потрібно \n" +
        "вказати свою адресу за формою: місто, село \n" +
        "або ж смт, а також вулицю. Наприклад, \n" +
        "Верхньодніпровськ, Дніпровська-1\n" +
        "\n" +
        "Після цього я надішлю ФІО народного \n" +
        "депутата України від твого округу, а також \n" +
        "депутата обласної, міської, селищної чи \n" +
        "сільської ради, в залежності від твого місця \n" +
        "проживання. Крім того, ти дізнаєшся, хто \n" +
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
    parser.ParserExelOBL(okrugOblRepo,deputatRepo);
    parser.ParserExelCITY(cityRepo,okrugCityRepo,deputatRepo,merRepo);
    parser.ParserDilnuzia_Okrugs(okrugOblRepo,okrugCityRepo,dilniziaRepo);

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

                            Dilnizia str=new Dilnizia();
                            str=searchDilnizia(update.getMessage().getText());
                           String name="";
                           String number="";

                            if (str==null)
                            {String [] arrs=update.getMessage().getText().split(",");
                                String city=arrs[0];
                                System.out.println("Можливо ви мали на увазі");
                               List<Dilnizia>dilnizias= dilniziaRepo.findAllByRegionContaining(city);
                               if (dilnizias.size()>1)
                               {

                                   sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Можливо ви мали на увазі:"+city+"-\n"));
                                   for (Dilnizia dilnizia:dilnizias)
                                   {
                                       try {
                                           String [] street=arrs[1].split("-");
                                           name=street[0].trim();
                                           number = street[1].trim();
                                           if (dilnizia.getRegion().indexOf(name)>0)
                                           {
                                               for (String sre:dilnizia.getRegion().split(";")) {

                                                   if (sre.indexOf(name)>0) {
                                                       System.out.println("111");
                                                       sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(sre+" "));
                                                   }
                                               }
                                           }

                                       }catch (Exception e)
                                       {
                                           name=arrs[1].trim();
                                           if (dilnizia.getRegion().indexOf(name)>0)
                                           {
                                               for (String sre:dilnizia.getRegion().split(";")) {

                                                   if (sre.indexOf(name)>0) {
                                                       System.out.println("111");
                                                       sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(sre+" "));
                                                   }
                                                   }
                                           }

                                       }
                                   }
                                   System.out.println("Можливо ви мали на увазі finish");
                               }

                            }else {
                                String info = "";
                                try {
                                    if (str.getOkrugCity().getCity().getTypeCity() == TypeCity.city.GetTitle()) {
                                        System.out.println("City");
                                        info += "Твій депутат Верховної Ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += str.getOkrugNdu().getNumber() + "\n";
                                            try {
                                                info += str.getOkrugNdu().getDeputat().getSurname().toUpperCase() + " "
                                                        + str.getOkrugNdu().getDeputat().getName().toUpperCase() + " "
                                                        + str.getOkrugNdu().getDeputat().getPartion().toUpperCase() +
                                                        "\n /id_" + str.getOkrugNdu().getDeputat().getId() + "\n";

                                            } catch (Exception e) {
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {
                                        }
                                        info += "\nТвій мер " +
                                                "м." + str.getOkrugCity().getCity().getName() + "\n";
                                        try {
                                            City city = str.getOkrugCity().getCity();
                                            info += city.getMer().getSurname().toUpperCase() + " "
                                                    + city.getMer().getName().toUpperCase() + " "
                                                    + city.getMer().getPartion().toUpperCase() +
                                                    "\n /id_" + city.getMer().getId() + "\n";
                                            ;
                                        } catch (Exception e) {
                                        }
                                        info += "\nТвій депутат обласної ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += str.getOkrugObl().getNumber() + "\n";
                                            try {
                                                info += str.getOkrugObl().getDeputat().getSurname().toUpperCase() + " "
                                                        + str.getOkrugObl().getDeputat().getName().toUpperCase() + " "
                                                        + str.getOkrugObl().getDeputat().getPartion().toUpperCase() +
                                                        "\n /id_" + str.getOkrugObl().getDeputat().getId() + "\n";

                                            } catch (Exception e) {
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {
                                        }
                                        info += "\nТвій депутат міської ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += str.getOkrugCity().getNumber() + "\n";
                                            try {
                                                info += str.getOkrugCity().getDeputat().getSurname().toUpperCase() + " "
                                                        + str.getOkrugCity().getDeputat().getName().toUpperCase() + " "
                                                        + str.getOkrugCity().getDeputat().getPartion().toUpperCase() +
                                                        "\n /id_" + str.getOkrugCity().getDeputat().getId() + "\n";

                                            } catch (Exception e) {
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        System.out.println("Country");
                                        info += "\nТвій депутат Верховної Ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += str.getOkrugNdu().getNumber() + "\n";
                                            try {
                                                info += str.getOkrugNdu().getDeputat().getSurname().toUpperCase() + " "
                                                        + str.getOkrugNdu().getDeputat().getName().toUpperCase() + " "
                                                        + str.getOkrugNdu().getDeputat().getPartion().toUpperCase() +
                                                        "\n /id_" + str.getOkrugNdu().getDeputat().getId() + "\n";

                                            } catch (Exception e) {
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {
                                        }
                                        info += "\nТвій голова селищної ради\n"
                                                + str.getOkrugCity().getCity().getName() + " " + str.getOkrugCity().getCity().getTypeCity() + "\n";
                                        try {
                                            City city = str.getOkrugCity().getCity();
                                            info += city.getMer().getSurname().toUpperCase() + " "
                                                    + city.getMer().getName().toUpperCase() + " "
                                                    + city.getMer().getPartion().toUpperCase() +
                                                    "\n /id_" + city.getMer().getId() + "\n";
                                        } catch (Exception e) {
                                        }
                                        info += "\nТвій депутат обласної ради\n" +
                                                "Округ № - ";
                                        try {
                                            info += str.getOkrugObl().getNumber() + "\n";
                                            try {
                                                info += str.getOkrugObl().getDeputat().getSurname().toUpperCase() + " "
                                                        + str.getOkrugObl().getDeputat().getName().toUpperCase() + " "
                                                        + str.getOkrugObl().getDeputat().getPartion().toUpperCase() +
                                                        "\n /id_" + str.getOkrugObl().getDeputat().getId() + "\n";

                                            } catch (Exception e) {
                                                info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                        "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                        "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                            }
                                        } catch (Exception e) {
                                        }
                                        info += "\nТвій депутат селищної ради\n" +
                                                "Округ № -в розробці-\n" +
                                                "ФІО -в розробці-";
                                    }
                                }catch (Exception er)
                                {
                                    info += "\nТвій депутат Верховної Ради\n" +
                                            "Округ № - ";
                                    try {
                                        info += str.getOkrugNdu().getNumber() + "\n";
                                        try {
                                            info += str.getOkrugNdu().getDeputat().getSurname().toUpperCase() + " "
                                                    + str.getOkrugNdu().getDeputat().getName().toUpperCase() + " "
                                                    + str.getOkrugNdu().getDeputat().getPartion().toUpperCase() +
                                                    "\n /id_" + str.getOkrugNdu().getDeputat().getId() + "\n";

                                        } catch (Exception e) {
                                            info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                    "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                    "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                        info="";
                                        }
                                    } catch (Exception e) {
                                    }
                                    try {
                                        info += "\nТвій голова селищної ради\n"
                                                + str.getOkrugCity().getCity().getName() + " " + str.getOkrugCity().getCity().getTypeCity() + "\n";
                                    }catch (Exception e){}
                                    try {
                                        City city = str.getOkrugCity().getCity();
                                        info += city.getMer().getSurname().toUpperCase() + " "
                                                + city.getMer().getName().toUpperCase() + " "
                                                + city.getMer().getPartion().toUpperCase() +
                                                "\n /id_" + city.getMer().getId() + "\n";
                                    } catch (Exception e) {
                                    }
                                    info += "\nТвій депутат обласної ради\n" +
                                            "Округ № - ";
                                    try {
                                        info += str.getOkrugObl().getNumber() + "\n";
                                        try {
                                            info += str.getOkrugObl().getDeputat().getSurname().toUpperCase() + " "
                                                    + str.getOkrugObl().getDeputat().getName().toUpperCase() + " "
                                                    + str.getOkrugObl().getDeputat().getPartion().toUpperCase() +
                                                    "\n /id_" + str.getOkrugObl().getDeputat().getId() + "\n";
                                            info += "\nТвій депутат селищної ради\n" +
                                                    "Округ № -в розробці-\n" +
                                                    "ФІО -в розробці-";
                                        } catch (Exception e) {
                                            info += "Депутата не обрано \uD83D\uDE22 \n" +
                                                    "/ссылка на Закон України «Про місцеві вибори» \n" +
                                                    "https://zakon.rada.gov.ua/laws/show/595-19#n102 (https://zakon.rada.gov.ua/laws/show/595-19#n102)";
                                        }
                                    } catch (Exception e) {
                                    }

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
    public Dilnizia searchDilnizia(String str)
    {
        String [] arr=str.split(",");
        String city=arr[0].trim().toLowerCase();
        String name="";
        String number="";
        List<Dilnizia>cities=new ArrayList<>();
        List<Dilnizia>names=new ArrayList<>();
        Dilnizia numbers=new Dilnizia();
        String address="";
        try {
            String [] street=arr[1].split("-");
            name=street[0].trim().toLowerCase();
            number=street[1].trim().toLowerCase();
        }catch (Exception e)
        {
            name=arr[1].trim().toLowerCase();
        }
        cities=dilniziaRepo.findAllByRegionContaining(city);
        if (cities.size()==1)
        {
            numbers=cities.get(0);
         return numbers;
        }
        if (cities.size()==0)
        {
            System.out.println("null1");
            System.out.println("null1");
            System.out.println("null1");
            return null;
        }
        for (Dilnizia dilnizia:cities)
        {
            if (dilnizia.getRegion().indexOf(name)>0)
            {
                names.add(dilnizia);
            }
        }
        if (names.size()==1)
        {
            System.out.println("names==1");
            System.out.println("names==1");
            System.out.println("names==1");
            System.out.println(names.get(0).getNumber());
            numbers=names.get(0);
            return numbers;
        }
        if (names.size()==0)
        {
            System.out.println("names==0");
            System.out.println("names==0");
            System.out.println("names==0");
            numbers=names.get(0);
            return numbers;
        }
        for (Dilnizia dilnizia: names)
        {
            String regions=dilnizia.getRegion();
            try {
                String[]adress= regions.split(";");
                for (String ad:adress)
                {
                    if (ad.indexOf(name)>0)
                    {
                        System.out.println(ad);
                        System.out.println(ad);
                        System.out.println(ad);
                        System.out.println("ad.indexof(name)>0");
                        System.out.println("ad.indexof(name)>0");
                        System.out.println("ad.indexof(name)>0");
                       address=ad;
                       address=address.split(":")[1];
                        try {

                            for (String rty:address.split(","))
                            {
                                System.out.println(rty);
                                System.out.println(rty);
                                System.out.println(rty);
                                try {
                                    System.out.println("??????????????");
                                    System.out.println("??????????????");
                                    System.out.println("??????????????");
                                    String[]ert=rty.trim().split("–");
                                    System.out.println(number+" "+ert[0]+" "+ert[1]);
                                    int num=Integer.valueOf(number.trim());
                                    int num1=Integer.valueOf(ert[0].trim());
                                    int num2=Integer.valueOf(ert[1].trim());
                                    System.out.println(num+" "+num1+" "+num2);
                                    if(num<num2&&num>num1)
                                    {
                                        numbers=dilnizia;
                                        return numbers;
                                    }
                                    if (num==num1||num==num2)
                                    {
                                        numbers=dilnizia;
                                        return numbers;
                                    }
                                }catch (Exception er)
                                {
                                    if(address.indexOf(number.trim())>0)
                                    {
                                        numbers=dilnizia;
                                        return numbers;
                                    }
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
                                    numbers=dilnizia;
                                    return numbers;
                                }
                                if (num==num1||num==num2)
                                {
                                    numbers=dilnizia;
                                    return numbers;
                                }
                            }catch (Exception er)
                            {
                             if(address.indexOf(number.trim())>0)
                             {
                                 numbers=dilnizia;
                                 return numbers;
                             }
                            }
                        }
                    }
                    else {
                        System.out.println("null2");
                        System.out.println("null2");
                        System.out.println("null2");

                    }
                }

            }catch (Exception e)
            {
                numbers=dilnizia;
                return numbers;
            }
        }
        return null;

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
