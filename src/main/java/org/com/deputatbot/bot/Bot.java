package org.com.deputatbot.bot;



import org.apache.poi.util.SystemOutLogger;
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
import java.time.LocalDate;
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
private String info1="Щоб з’ясувати, які депутати представляють" +
        " твій населений пункт в Верховній Раді," +
        " а також в місцевих радах - міській, обласній, " +
        "сільській або селищній раді, потрібно вказати " +
        "свою адресу за формою: місто, село або ж смт," +
        " а також вулицю.";
    private String info2="Наприклад ⤵️\n" +
            "**Верхньодніпровськ, Дніпровська-1**\n";
    private String info3="Після цього я надішлю ФІО " +
            "народного депутата України від твого округу," +
            " а також депутата обласної, міської," +
            " селищної чи сільської ради, в залежності" +
            " від твого місця проживання. Крім того, ти" +
            " дізнаєшся, хто очолює твоє місто чи то ОТГ," +
            " або ж село";
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
    @Autowired
    private FeedbackRepo feedbackRepo;
    private boolean feedback=false;
    private boolean support_admin =false;
    @PostConstruct
    public void construct() throws IOException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        if (okrugNduRepo.findAll().size() > 1) {
        }else
        {
            Parser parser = new Parser();

            parser.allNduOkrug(okrugNduRepo, dilniziaRepo, deputatRepo);
            parser.ParserExelNDU(okrugNduRepo, deputatRepo);
            parser.ParserExelOBL(okrugOblRepo, deputatRepo, dilniziaRepo);
            parser.ParserExelCITY(cityRepo, okrugCityRepo, deputatRepo, merRepo, dilniziaRepo);

            parser.ParserExelNDUKuev(okrugNduRepo, deputatRepo);
            parser.ParserExelCITYKuev(cityRepo, okrugCityRepo, deputatRepo, merRepo, dilniziaRepo);

            User user = new User();
            user.setUsername("obranetc");
            user.setPassword("2019");
            user.setActive(true);
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            user.setRoles(roles);
            userRepo.save(user);
        }
    }

    Feedback feedback1=new Feedback();


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
                    KeyboardRow aer=new KeyboardRow();
                    aer.add("Відправити відгук");
                    arr.add(aer);
                    arr.add(l);
                    ReplyKeyboardMarkup j=new ReplyKeyboardMarkup();
                     j      .setKeyboard(arr)
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
                case "назад":
                    List<KeyboardRow> arre=new ArrayList<>();
                    KeyboardRow le=new KeyboardRow();
                    le.add(new KeyboardButton("@ObranetsBot скажи, хто мій депутат?"));
                    KeyboardRow aere=new KeyboardRow();
                    aere.add("Відправити відгук");
                    arre.add(aere);
                    arre.add(le);
                    ReplyKeyboardMarkup jee=new ReplyKeyboardMarkup();
                    jee      .setKeyboard(arre)
                            .setSelective(true)
                            .setResizeKeyboard(true)
                            .setOneTimeKeyboard(true);
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(start).setReplyMarkup(jee));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Відправити відгук":
                    List<KeyboardRow> arrayListr=new ArrayList<>();
                    KeyboardRow lee=new KeyboardRow();
                    lee.add(new KeyboardButton("назад"));
                    KeyboardRow aeer=new KeyboardRow();
                    aeer.add("не знайшов адрес");
                    aeer.add("неправильна інформація");
                    arrayListr.add(aeer);
                    aeer=new KeyboardRow();
                    aeer.add("мені сподобалося");
                    aeer.add("інше");
                    arrayListr.add(aeer);
                    arrayListr.add(lee);
                    ReplyKeyboardMarkup jr=new ReplyKeyboardMarkup();
                    jr      .setKeyboard(arrayListr)
                            .setSelective(true)
                            .setResizeKeyboard(true)
                            .setOneTimeKeyboard(true);
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("Виберіть тип відгука ▌↓▌").setReplyMarkup(jr));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                        break;
                case "@ObranetsBot скажи, хто мій депутат?":
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(info1));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(info2));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(info3));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:
                        if (update.getMessage().getText().equals("не знайшов адрес")){
                            try {
                                sendApiMethod(new SendMessage().setText("Напишіть відгук")
                                        .setChatId(update.getMessage().getChatId()));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            feedback=true;
                            feedback1.setDate(LocalDate.now().toString());
                            feedback1.setTypeFeedback(TypeFeedback.notFound);
                            System.out.println(TypeFeedback.notFound);
                        }
                        else if(update.getMessage().getText().equals("неправильна інформація")){
                            try {
                                sendApiMethod(new SendMessage().setText("Напишіть відгук")
                                        .setChatId(update.getMessage().getChatId()));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            feedback=true;
                            feedback1.setDate(LocalDate.now().toString());
                            feedback1.setTypeFeedback(TypeFeedback.notCorect);
                            System.out.println(TypeFeedback.notCorect);
                        }
                       else if(update.getMessage().getText().equals("мені сподобалося")){
                            try {
                                sendApiMethod(new SendMessage().setText("Напишіть відгук")
                                        .setChatId(update.getMessage().getChatId()));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            feedback=true;
                            feedback1.setDate(LocalDate.now().toString());
                            feedback1.setTypeFeedback(TypeFeedback.likeBot);
                            System.out.println(TypeFeedback.likeBot);
                        }
                        else if(update.getMessage().getText().equals("інше")){
                            try {
                                sendApiMethod(new SendMessage().setText("Напишіть відгук")
                                        .setChatId(update.getMessage().getChatId()));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            feedback=true;
                            feedback1.setDate(LocalDate.now().toString());
                            feedback1.setTypeFeedback(TypeFeedback.anythingFeedback);
                            System.out.println(TypeFeedback.anythingFeedback);
                        }
                        else if(feedback&&update.getMessage().getText()!=null){
                            feedback=false;
                            feedback1.setFeedback(update.getMessage().getText());
                            System.out.println(LocalDate.now());
                            feedback1.setDate(LocalDate.now().toString());
                            feedbackRepo.save(feedback1);
                            System.out.println(feedback1.getDate());
                            System.out.println(feedback1.getFeedback());
                            System.out.println(feedback1.getTypeFeedback().GetTypeFeedback());
                            feedback1=new Feedback();
                            try {
                                sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Дякуємо за відгук"));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (update.getMessage().getText().indexOf("/id_")>=0)
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
                            String adress=update.getMessage().getText();
                            str=searchDilnizia(adress,update.getMessage().getChatId().toString());
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
                           boolean mees=false;
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
                                                e.printStackTrace();
                                                info += "Депутата не обрано \uD83D\uDE22 \n";
                                            }
                                        } catch (Exception e) { e.printStackTrace();    }
                                        try {
                                            if (typeCity == TypeCity.city) {
                                                info += "\nТвій мер " +
                                                        "м." + city.getName() + "\n";
                                                mees=true;
                                            }
                                            else if (typeCity == TypeCity.city_country){
                                                info += "\nТвій голова селищної ради\n"
                                                        + city.getName() + " " + typeCity.GetTitle() + "\n";
                                            mees=true;
                                            }else if (typeCity == TypeCity.country){
                                                info += "\nТвій голова сільської ради\n"
                                                        + city.getName() + " " + typeCity.GetTitle() + "\n";
                                                mees=true;
                                            }else if (typeCity==TypeCity.city_all){
                                                info += "\nТвій голова міської ради\n"
                                                        + city.getName() + " " + typeCity.GetTitle() + "\n";
                                                mees=true;
                                            }

                                        try {
                                            info += mer.getSurname().toUpperCase() + " "
                                                    + mer.getName().toUpperCase() + " "
                                                    + mer.getPartion().toUpperCase() +
                                                    "\n /id_" + mer.getId() + "\n";

                                        } catch (Exception e) {  e.printStackTrace();      }
                                        }catch (Exception e){e.printStackTrace();  }
                                        try {
                                            if (okrugCity.getCity().getName().toLowerCase().equals("київ"))
                                            {}else
                                            {
                                                System.out.println("1");
                                                info += "\nТвій депутат обласної ради\n" +
                                                    "Округ № - ";
                                                try {
                                                    info += okrugObl.getNumber() + "\n";
                                                    try {
                                                        if(deputatRepo.findAllByOkrugObl(okrugObl).size()<1)
                                                            Integer.valueOf("asd");
                                                        for (Deputat dep: deputatRepo.findAllByOkrugObl(okrugObl)) {
                                                            deputat=dep;
                                                            try {
                                                                info += deputat.getSurname().toUpperCase() + " "
                                                                        + deputat.getName().toUpperCase() + " "
                                                                        + deputat.getPartion().toUpperCase() +
                                                                        "\n /id_" + deputat.getId() + "\n";
                                                            }catch (Exception e){}
                                                        }

                                                    } catch (Exception e) {e.printStackTrace();
                                                        if (deputatRepo.findAllByOkrugObl(okrugObl).size()==0)
                                                            info += "Депутата не обрано \uD83D\uDE22 \n";

                                                    }
                                                } catch (Exception e) {    e.printStackTrace();  info += "Депутата не обрано \uD83D\uDE22 \n"; }
                                            }
                                        }catch (Exception e){
                                            System.out.println("2");

                                            e.printStackTrace();
                                            info += "\nТвій депутат обласної ради\n" +
                                                    "Округ № - ";
                                            try {
                                                info += okrugObl.getNumber() + "\n";
                                                try {
                                                    if(deputatRepo.findAllByOkrugObl(okrugObl).size()<1)
                                                        Integer.valueOf("asd");
                                                    for (Deputat dep: deputatRepo.findAllByOkrugObl(okrugObl)) {
                                                        deputat=dep;
                                                        try {
                                                            info += deputat.getSurname().toUpperCase() + " "
                                                                    + deputat.getName().toUpperCase() + " "
                                                                    + deputat.getPartion().toUpperCase() +
                                                                    "\n /id_" + deputat.getId() + "\n";
                                                        }catch (Exception e1){}
                                                    }

                                                } catch (Exception e1) {e1.printStackTrace();
                                                    if (deputatRepo.findAllByOkrugObl(okrugObl).size()==0)
                                                        info += "Депутата не обрано \uD83D\uDE22 \n";

                                                }
                                            } catch (Exception e1) {    e1.printStackTrace();  info += "Депутата не обрано \uD83D\uDE22 \n"; }
                                        }
                                        try {
                                        try {
                                            try {
                                                if(deputatRepo.findAllByOkrugCity(okrugCity).size()<1)
                                                {   if (typeCity == TypeCity.city) {
                                                        info += "\nТвій депутат міської ради\n" +
                                                                "Округ № " + okrugCity.getNumber() + "\n";
                                                        mees=true;
                                                    }
                                                    else if (typeCity == TypeCity.city_country){
                                                        info += "\nТвій депутат селищної ради\n"
                                                                + "Округ №  " +okrugCity.getNumber() + "\n";
                                                        mees=true;
                                                    }else if (typeCity == TypeCity.country){
                                                        info += "\nТвій депутат сільської ради\n"
                                                                + " Округ № " + okrugCity.getNumber() + "\n";
                                                        mees=true;
                                                    }else if (typeCity==TypeCity.city_all){
                                                        info += "\nТвій депутат міської ради\n"
                                                                + "Округ №  " + okrugCity.getNumber() + "\n";
                                                        mees=true;
                                                    }
                                                    info+="Депутата не обрано \uD83D\uDE22";
                                                 }else if (deputatRepo.findAllByOkrugCity(okrugCity).size()>=1)
                                                { if (typeCity == TypeCity.city) {
                                                    info += "\nТвій депутат міської ради\n" +
                                                            "Округ № " + okrugCity.getNumber() + "\n";
                                                    mees=true;
                                                }
                                                else if (typeCity == TypeCity.city_country){
                                                    info += "\nТвій депутат селищної ради\n"
                                                            + "Округ №  " +okrugCity.getNumber() + "\n";
                                                    mees=true;
                                                }else if (typeCity == TypeCity.country){
                                                    info += "\nТвій депутат сільської ради\n"
                                                            + " Округ № " + okrugCity.getNumber() + "\n";
                                                    mees=true;
                                                }else if (typeCity==TypeCity.city_all){
                                                    info += "\nТвій депутат міської ради\n"
                                                            + "Округ №  " + okrugCity.getNumber() + "\n";
                                                    mees=true;
                                                }}
                                            for (Deputat dep: deputatRepo.findAllByOkrugCity(okrugCity))
                                                {
                                                            deputat = dep;
                                                            info += deputat.getSurname().toUpperCase() + " "
                                                                    + deputat.getName().toUpperCase() + " "
                                                                    + deputat.getPartion().toUpperCase() +
                                                                    "\n /id_" + deputat.getId() + "\n";


                                                }
                                            } catch (Exception e) {e.printStackTrace();

                                            }
                                        } catch (Exception e) {   e.printStackTrace();      }
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }



                                }catch (Exception er)
                                {
                                    er.printStackTrace();
                                }
                                System.out.println(info);
                                sendApiMethod(new SendMessage().setText(info).setChatId(update.getMessage().getChatId()));
                            }
                           else if(str==null)
                           {
                               String answer="";
                               String[] adres;
                               try {
                                   adres=adress.split(",");
                                  List<Dilnizia>dilniziaList= dilniziaRepo.findAllByRegionContaining(adres[0]);
                                   try {

                                   }catch (Exception e){}
                               }catch (Exception e){}


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
        String[] strings=allStr(str);
        String city=strings[0].trim();
        if(strings.length==1) {
            System.out.println("1");
            System.out.println(str.split(" ").length);
            List<Dilnizia> dilnizias = dilniziaRepo.findAllByRegionContaining(str.trim().toLowerCase());
            List<Dilnizia> del = new ArrayList<>();
            if (dilnizias.size() != 0)
            {for (Dilnizia dilnizia1 : dilnizias) {
                    try {
                        dilnizia1.getOkrugCity().getNumber();
                        del.add(dilnizia1);
                    } catch (Exception e) {
                    }
                }
                if (del.size()!=0)
                return del.get(0);
                else return dilnizias.get(0);
            }
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

        String name="";
        String number="";
        if (strings.length==2)
        {   System.out.println("2");
           name=strings[1].trim();
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
            else if (names.size()==0)
            {
                return null;
            }
            else if(names.size()>0){
                List<Dilnizia> del = new ArrayList<>();
                for (Dilnizia dilnizia1 : names) {
                    try {
                        dilnizia1.getOkrugCity().getNumber();
                        del.add(dilnizia1);
                    } catch (Exception e) {
                    }
                }
                    if (del.size()!=0)
                        return del.get(0);
                    else return names.get(0);
                }
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
        if (strings.length==3) {
            System.out.println("3");
            try {
                city=strings[0].trim();
                name=strings[1].trim();
                number = strings[2].trim();
            } catch (Exception e) {
            }
            System.out.println(city + " " + name + " " + number);

            List<Dilnizia> cities = new ArrayList<>();
            List<Dilnizia> names = new ArrayList<>();

            cities = dilniziaRepo.findAllByRegionContaining(city);
            if (cities.size() == 1) {
                System.out.println("size==1");
                dilnizia = cities.get(0);
                return dilnizia;
            }
            if (cities.size() == 0) {
                System.out.println("size==0");
                return null;
            }

            for (Dilnizia dilnizias : cities) {
                if (dilnizias.getRegion().contains(name)) {
                    names.add(dilnizias);
                    System.out.println(dilnizias.getNumber());
                }
            }
            System.out.println("size>1");
            if (names.size() == 1) {
                System.out.println("size==1");
                dilnizia = names.get(0);
                return dilnizia;
            }
            if (names.size() == 0) {
                System.out.println("size==0");
                return null;
            }
            List<Dilnizia>list=new ArrayList<>();
            for (Dilnizia dilnizias : names) {
                String regions = dilnizias.getRegion();
                System.out.println(dilnizias.getNumber() + " " + regions);
                try {
                    String[] adress = regions.split(";");
                    for (String ad : adress) {
                        if (ad.indexOf(name) > 0) {
                            System.out.println(ad+"\n");

                            String adess = ad.split(":")[1];
                            try {

                                if (adess.indexOf(number.trim())>=0) {
                                    System.out.println(number.trim()+"||||"+adess.trim());

                                    try {
                                        int num= Integer.valueOf(number.trim());
                                        int rt=Integer.valueOf(adess.trim());
                                        if (num==rt)
                                            return dilnizias;
                                    }catch (Exception e){}
                                    for (String rty : adess.split(",")) {
                                        try {
                                            int num= Integer.valueOf(number.trim());
                                            int rt=Integer.valueOf(rty.trim());
                                            if (num==rt){
                                            System.out.println(num+"====="+rt);
                                                return dilnizias;
                                            }
                                        }catch (Exception e){}
                                        try {
                                            String[] ert = rty.trim().split("–");
                                            System.out.println("split(--)");
                                            int num = Integer.valueOf(number.trim());
                                            int num1 = Integer.valueOf(ert[0].trim());
                                            if (num1==num)
                                                return dilnizias;
                                            int num2 = Integer.valueOf(ert[1].trim());
                                            if (num2==num)
                                                return dilnizias;
                                            System.out.println(num + " " + num1 + " " + num2);
                                            if (num < num2 && num > num1) {
                                                return dilnizias;
                                            }

                                            } catch (Exception er) {
                                        }
                                    }
                                }
                                else
                                {
                                    for (String rty : adess.split(",")) {
                                        try {
                                            int num= Integer.valueOf(number.trim());
                                            int rt=Integer.valueOf(rty.trim());
                                            if (num==rt){
                                                System.out.println(num+"====="+rt);
                                                return dilnizias;
                                            }
                                        }catch (Exception e){}
                                        try {
                                            String[] ert = rty.trim().split("–");
                                            System.out.println("split(--)");
                                            int num = Integer.valueOf(number.trim());
                                            int num1 = Integer.valueOf(ert[0].trim());
                                            int num2 = Integer.valueOf(ert[1].trim());
                                            System.out.println(num + " " + num1 + " " + num2);
                                            if (num < num2 && num > num1) {
                                                dilnizia = dilnizias;
                                                return dilnizia;
                                            }
                                            if (num == num1 || num == num2) {
                                                dilnizia = dilnizias;
                                                return dilnizia;
                                            }
                                        } catch (Exception er) {
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                try {
                                    String[] ert = ad.split("-");
                                    int num = Integer.valueOf(number.trim());
                                    int num1 = Integer.valueOf(ert[0].trim());
                                    if (num1==num)
                                        return dilnizias;
                                    int num2 = Integer.valueOf(ert[1].trim());
                                    if (num2==num)
                                        return dilnizias;
                                    if (num < num2 && num > num1) {
                                        return dilnizias;
                                    }


                                } catch (Exception er) {
                                    if (ad.indexOf(number.trim()) >= 0) {
                                        return dilnizias;
                                    }
                                }
                            }

                        } else {



                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    dilnizia = dilnizias;
                    return dilnizia;
                }
            }
            return list.get(0);
        }
        else {
            return null;
        }

    }
    public String[] allStr(String request)
    {
        String city="";
        String street="";
        String number="";
        String[] split1=new String[]{};
        try {
            split1=request.split(",");
            city=split1[0];
        }catch (Exception e)
        {
            return new String[]{city};
        }
        String[] split2=new String[]{};
        try {
            split2 = split1[1].split("-");
        }catch (Exception e){return new String[]{city};}
            street=split2[0];
        try {
            number=split2[1];
        }catch (Exception e){
            return new String[]{split1[0],split1[1]};
        }
        System.out.println(city+" "+street+" "+number);
        return new String[]{city,street,number};
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
