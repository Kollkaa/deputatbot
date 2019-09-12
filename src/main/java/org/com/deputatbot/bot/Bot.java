package org.com.deputatbot.bot;



import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.domain.Mer;
import org.com.deputatbot.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
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


@Service
public class Bot extends TelegramLongPollingBot {

private String info="Якщо ти хочеш побачити всіх депутатів в твоєму регіоні, \n " +
        "то тобі потрібно просто ввести свою домашню адресу\n" +
        " за цим прикладом \n" +
        "(Місто, Село або СМТ) Верхньодніпровськ, (Вулиця) Дніпровська-1]\n" +
        "після цього я відправлю тобі: народного депутата України, \n" +
        "депутата обласної ради, міської ради або отг, а також мера або голову отг в твоєму окрузі ти ";
private String start="Привіт, я бот для показу всіх депутатів в твоєму регіоні";



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
        {if(update.getMessage().getText()!=null)
        {
            String text=update.getMessage().getText();
            switch (text){

                case"/start":
                    List<KeyboardRow> arr=new ArrayList<>();
                    KeyboardRow l=new KeyboardRow();
                    l.add(new KeyboardButton("Інформація"));
                    arr.add(l);
                    ReplyKeyboard j=new ReplyKeyboardMarkup();
                    ((ReplyKeyboardMarkup) j).setKeyboard(arr);
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(start).setReplyMarkup(j));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Інформація":
                    try {
                        sendApiMethod(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(info));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:
                        int count=0;
                        String stre=update.getMessage().getText();
                        stre=stre.toLowerCase();
                        String [] arr1=stre.split(",");
                        ArrayList<Dilnizia> dilnizias=new ArrayList<>();
                        Map<Integer, List<Dilnizia>> er=new HashMap<>();


                            er.put(count++,dilniziaRepo.findAllByRegionContaining(arr1[0].trim()));
                            er.put(count++,dilniziaRepo.findAllByRegionContaining(arr1[1].split("-")[0].trim()));
                           String three=arr1[1].split("-")[1].trim();
                            System.out.println(arr1[1].split("-")[1].trim());
                        System.out.println(er.get(0).size());
                        System.out.println(er.get(1).size());
                        List<Dilnizia> list= intersectArrays(er.get(1),er.get(0));
                        Dilnizia str=list.get(0);
                        if (list.size()>1)
                        {
                            for (Dilnizia d: list)
                            {
                                for (String r:d.getRegion().split(";"))
                                {
                                    if (r.indexOf(arr1[1].split("-")[0].trim())>0 && r.indexOf(three)>0)
                                    {

                                        str=d;
                                    }
                                }
                            }
                        }

                           String info="";
                           try {
                               Mer mer = merRepo.findByCity_Name(str.getOkrugCity().getCity().getName());
                               info+="Мер : " + mer.getSurname() +" "+ mer.getName() +" "+ mer.getPartion()+"\n";
                           }
                           catch (Exception e)
                           {}
                           try {
                           info+="Твій регіон НДУ - "+str.getOkrugNdu().getNumber()+"\n"
                                   +"Депутат : "+str.getOkrugNdu().getDeputat().getSurname()+" "
                                   +str.getOkrugNdu().getDeputat().getName()
                                   +str.getOkrugNdu().getDeputat().getPartion()+"\n";
                           }
                           catch (Exception e)
                           {}
                           try {
                           info+="Твій регіон Обласної ради - "+str.getOkrugObl().getNumber()+"\n"
                                   +"Депутат : "+str.getOkrugObl().getDeputat().getSurname()+" "
                                   +str.getOkrugObl().getDeputat().getName()
                                   +str.getOkrugObl().getDeputat().getPartion()+"\n";
                           }
                           catch (Exception e)
                           {}
                           try {
                           info+="Твій регіон Районної ради - "+str.getOkrugCity().getNumber()+"\n"
                                   +"Депутат : "+str.getOkrugCity().getDeputat().getSurname()+" "
                                   +str.getOkrugCity().getDeputat().getName()
                                   +str.getOkrugCity().getDeputat().getPartion()+"\n"
                                   +str.getOkrugCity().getCity().getTypeCity()
                                   +" "+str.getOkrugCity().getCity().getName();
                           }
                           catch (Exception e)
                           {}

                           try {
                               Mer mer=merRepo.findByCity(str.getOkrugCity().getCity());

                           info+="Мер твого міста - "+mer.getSurname()+" "+mer.getName()+" "+mer.getPartion();
                           }
                           catch (Exception e)
                           {}

                           System.out.println(info);
                           try {
                               sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(info));
                           } catch (TelegramApiException e) {
                               e.printStackTrace();
                           }
                           try {
                               sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(str.getNumber().toString()));
                           } catch (TelegramApiException e) {
                               e.printStackTrace();
                           }
                        break;

            }

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
    @Override
    public String getBotUsername() {
        return "Polled_bot";
    }

    @Override
    public String getBotToken() {
        return "851210991:AAEJhjujEK7z5e_SfmPevHeWLP0KiK0AHmA";
    }
}
