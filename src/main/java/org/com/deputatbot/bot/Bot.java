package org.com.deputatbot.bot;



import org.com.deputatbot.domain.Dilnizia;
import org.com.deputatbot.domain.Mer;
import org.com.deputatbot.domain.TypeCity;
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

                       if (update.getMessage().getText().indexOf("/id_")>=0)
                       {
                            try {
                                sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText("в розробці"));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                       }
                       try {
                            ArrayList<Dilnizia> dilnizias=new ArrayList<>();
                            Map<Integer, List<Dilnizia>> er=new HashMap<>();
                        int count=0;
                        String stre=update.getMessage().getText();
                        stre=stre.toLowerCase();
                        String arr11="";
                        String arr22="";
                       try {
                            String [] arr1=stre.split(",");
                            arr11=arr1[0];
                            arr22=arr1[1];
                            er.put(count++,dilniziaRepo.findAllByRegionContaining(arr11.trim()));
                            try {
                                er.put(count++,dilniziaRepo.findAllByRegionContaining(arr22.trim().split("-")[0].trim()));
                            }catch (Exception e)
                            {
                                er.put(count++,dilniziaRepo.findAllByRegionContaining(arr22.trim()));
                                System.out.println("11111111");
                            }


                        }
                        catch (Exception r)
                        {
                            arr11=stre.trim();

                            er.put(count,dilniziaRepo.findAllByRegionContaining(arr11));
                            System.out.println("one");
                        }
                       Dilnizia str=new Dilnizia();
                       try {
                           if (er.get(1).size()==1) {
                               str=er.get(1).get(0);
                           }
                           else {
                               List<Dilnizia> list = intersectArrays(er.get(1), er.get(0));

                               str = list.get(0);

                               if (list.size() > 1) {
                                   System.out.println("############");
                                   for (Dilnizia d : list) {
                                       System.out.println("$$$$$$$$$$$$$$$$");
                                       for (String r : d.getRegion().split(";")) {
                                           System.out.println("!!!!!!!!!!!");
                                           try {
                                               String three = arr22.split("-")[1].trim();

                                               if (r.indexOf(arr22.split("-")[0].trim()) > 0 && r.indexOf(three) > 0) {

                                                   str = d;
                                               }
                                               System.out.println("@@@@@@@@@@@@@@@@");
                                           } catch (Exception w) {
                                               System.out.println("222222222");

                                           }
                                       }
                                   }
                               }
                           }
                       }
                        catch (Exception t)
                            {

                            try {
                                System.out.println("two");
                              str=er.get(0).get(0);
                                }
                                catch (Exception y)
                                {System.out.println("four");}
                            System.out.println("three");
                            }
                           String info="";
                           info+="Твій регіон НДУ - ";
                           try {
                                info+=str.getOkrugNdu().getNumber()
                                        +"\n"+"Депутат : \n"
                                   +str.getOkrugNdu().getDeputat().getSurname().toUpperCase()+" "
                                   +str.getOkrugNdu().getDeputat().getName().toUpperCase()+ " "
                                   +str.getOkrugNdu().getDeputat().getPartion().toUpperCase()+" /id_"
                                   +str.getOkrugNdu().getDeputat().getId()
                                   +"\n-------------------------------------------------\n";
                           }
                           catch (Exception e)
                           {
                               info+="згідно .....";
                           }
                           info+="Твій регіон Обласної ради - ";
                           try {
                                   info+=str.getOkrugObl().getNumber()
                                           +"\n"+"Депутат : \n "
                                   +str.getOkrugObl().getDeputat().getSurname().toUpperCase()+" "
                                   +str.getOkrugObl().getDeputat().getName().toUpperCase()+" "
                                   +str.getOkrugObl().getDeputat().getPartion().toUpperCase()+" /id_"
                                   +str.getOkrugObl().getDeputat().getId()
                                   +"\n-------------------------------------------------\n";
                           }
                           catch (Exception e)
                           {
                               info+="згідно .....";
                           }
                          try {
                              if (str.getOkrugCity().getCity().getTypeCity()== TypeCity.city.GetTitle())
                                  info += "Твій регіон Міської ради - ";
                              else
                                  info+="Твій регіон Окремої територіальної громади ради - ";
                          }
                          catch (Exception m)
                          {info += "Твій регіон Міської ради - ";
                              System.out.println("fife");}
                           try {
                                   info+=str.getOkrugCity().getNumber()
                                           +"\n"+"Депутат : \n";
                               try {
                                   info+=str.getOkrugCity().getDeputat().getSurname().toUpperCase() + " "
                                           + str.getOkrugCity().getDeputat().getName().toUpperCase() + " "
                                           + str.getOkrugCity().getDeputat().getPartion().toUpperCase()+" /id_"
                                           +str.getOkrugCity().getDeputat().getId()
                                           + "\n-------------------------------------------------\n";
                               }catch (Exception ee)
                               {

                               }
                               info+=str.getOkrugCity().getCity().getTypeCity()
                               + " " + str.getOkrugCity().getCity().getName();


                           }
                           catch (Exception e)
                           {
                               info+="згідно .....";
                           }
                            try {
                                if (str.getOkrugCity().getCity().getTypeCity() == TypeCity.city.GetTitle()) {
                                    info += "\nМер твого міста : \n";
                                    try {
                                        Mer mer = str.getOkrugCity().getCity().getMer();
                                        info += mer.getSurname().toUpperCase()
                                                + " " + mer.getName().toUpperCase()
                                                + " " + mer.getPartion().toUpperCase() + " /id_" + mer.getId();
                                    } catch (Exception d) {
                                    }
                                } else {
                                    info += "\nГолова товєї об'єднаної територіальної громади : \n";
                                    try {
                                        Mer mer = str.getOkrugCity().getCity().getMer();
                                        info += mer.getSurname().toUpperCase()
                                                + " " + mer.getName().toUpperCase()
                                                + " " + mer.getPartion().toUpperCase() + " /id_" + mer.getId();
                                    } catch (Exception d) {
                                    }
                                }
                            }
                            catch (Exception c)
                            {
                                info += "\nМер твого міста : \n";
                                System.out.println("er");
                            }



                           System.out.println(info);
                           try {
                               sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(info));
                           } catch (TelegramApiException e) {
                               e.printStackTrace();
                           }
                       }
                       catch (Exception e)
                        {
                           try {
                            System.out.println("3333333333333");
                            String stre=update.getMessage().getText();
                            String [] arr1=stre.split(",");
                            List<Dilnizia> dilnizias1= dilniziaRepo.findAllByRegionContaining(arr1[1].split("-")[0]);
                            for (Dilnizia d:dilnizias1)
                            {
                                for(String s:d.getRegion().split(","))
                                {
                                    if(s.contains(arr1[1].split("-")[0]))
                                    {
                                        sorry+=s+"\n";
                                    }
                                }
                            }
                            if("Напевно ви мали на увазі :".equals(sorry))
                            {}
                            else {
                                try {
                                    sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(sorry));
                                } catch (TelegramApiException ee) {
                                    ee.printStackTrace();
                                }
                            }
                           }catch (Exception i)
                           {

                           }
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
