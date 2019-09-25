package org.com.deputatbot.bot;



import org.com.deputatbot.domain.City;
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
                           ArrayList<Dilnizia> dilnizias = new ArrayList<>();
                           Map<Integer, List<Dilnizia>> er = new HashMap<>();
                           int count = 0;
                           String stre = update.getMessage().getText();
                           stre = stre.toLowerCase();
                           String arr11 = "";
                           String arr22 = "";
                           try {
                               String[] arr1 = stre.split(",");
                               arr11 = arr1[0];
                               arr22 = arr1[1];
                               er.put(count++, dilniziaRepo.findAllByRegionContaining(arr11.trim()));
                               try {
                                   er.put(count++, dilniziaRepo.findAllByRegionContaining(arr22.trim().split("-")[0].trim()));
                               } catch (Exception e) {
                                   er.put(count++, dilniziaRepo.findAllByRegionContaining(arr22.trim()));
                                   System.out.println("11111111");
                               }


                           } catch (Exception r) {
                               arr11 = stre.trim();

                               er.put(count, dilniziaRepo.findAllByRegionContaining(arr11));
                               System.out.println("one");
                           }
                           Dilnizia str = new Dilnizia();
                           try {
                               if (er.get(1).size() == 1) {
                                   str = er.get(1).get(0);
                               } else {
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
                                                       System.out.println("@@@@@@@@@@@@@@@@");
                                                   }

                                               } catch (Exception w) {
                                                   System.out.println("222222222");

                                               }
                                           }
                                       }
                                   }
                               }
                           } catch (Exception t) {

                               try {
                                   System.out.println("two");
                                   str = er.get(0).get(0);
                               } catch (Exception y) {
                                   System.out.println("four");
                               }
                               System.out.println("three");
                           }
                           String info = "";
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
                               } catch (Exception e) { }
                               info+="Твій мер " +
                                     "м."+str.getOkrugCity().getCity().getName()+"\n";
                               try {
                                            City city=str.getOkrugCity().getCity();
                                          info+=city.getMer().getSurname().toUpperCase()+" "
                                          +city.getMer().getName().toUpperCase()+" "
                                          +city.getMer().getPartion().toUpperCase()+
                                          "\n /id_" + city.getMer().getId() + "\n";;
                                        }catch (Exception e){}
                               info += "Твій депутат обласної ради\n" +
                                       "Округ № - ";
                               try {
                                           info+=str.getOkrugObl().getNumber()+"\n" ;
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
                                       }catch (Exception e){}
                               info += "Твій депутат міської ради\n" +
                                       "Округ № - ";
                               try {
                                   info+=str.getOkrugCity().getNumber()+"\n" ;
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
                               }catch (Exception e){}
                           }
                           else {
                               System.out.println("Country");
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
                               } catch (Exception e) { }
                               info+="Твій голова селищної ради\n"
                                       +str.getOkrugCity().getCity().getName()+" "+str.getOkrugCity().getCity().getTypeCity()+"\n";
                               try {
                                   City city=str.getOkrugCity().getCity();
                                   info+=city.getMer().getSurname().toUpperCase()+" "
                                           +city.getMer().getName().toUpperCase()+" "
                                           +city.getMer().getPartion().toUpperCase()+
                                           "\n /id_" + city.getMer().getId() + "\n";
                               }catch (Exception e){}
                               info += "Твій депутат обласної ради\n" +
                                       "Округ № - ";
                               try {
                                   info+=str.getOkrugObl().getNumber()+"\n" ;
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
                               }catch (Exception e){}
                               info += "Твій депутат селищної ради\n" +
                                       "Округ № -в розробці-\n" +
                                       "ФІО -в розробці-";

                           }
                            sendApiMethod(new SendMessage().setText(info).setChatId(update.getMessage().getChatId()));


                       }catch (Exception e)
                                {}
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
    public Dilnizia searchDilnizia()
    {
        return new Dilnizia();
    }
    /*
    @Override
    public String getBotUsername() {
        return "Polled_bot";
    }

    @Override
    public String getBotToken() {
        return "851210991:AAEJhjujEK7z5e_SfmPevHeWLP0KiK0AHmA";
    }
    */
    @Override
    public String getBotUsername() {
        return "@PolllandBot";///""@Documents_in_Poland_bot;@warsaww_bot
    }

    @Override
    public String getBotToken() {
        return "943271793:AAE7x-ZWsV54FouKFSqjEN06N1f7vj6OL_k";// 808617170:AAF58eibRG7whQZkJAI3ounVnN__2TRbFEo|||827804459:AAEhCYbx6DhbZDsoUroynFmqf2f57yDqzaw
    }
}
