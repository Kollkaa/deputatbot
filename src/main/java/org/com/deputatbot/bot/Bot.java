package org.com.deputatbot.bot;


import com.boot.lions.domain.Role;
import com.boot.lions.domain.User;
import com.boot.lions.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Bot extends TelegramLongPollingBot {
    @Autowired
     private UserRepo userRepo;

    private boolean support_admin =false;
    @PostConstruct
    public void construct()
    { TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }}

    String default1="Нажмите на /start что б начать заново, вы ввели что-то не так";
    List<String> products=new ArrayList<>();
    {
        products.add("Услуга №1 @e_drozdov");
        products.add("Услуга №2 @e_drozdov");
        products.add("Услуга №3 @e_drozdov");
        products.add("Услуга №4 @e_drozdov");
        products.add("Услуга №5 @e_drozdov");


    }
    User use=new User();
    User admin=null;
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage())
        {if(update.getMessage().getText()!=null)
           System.out.println(update.getMessage().getChatId());
            switch (update.getMessage().getText()) {
                case "/start":

                    if (userRepo.findByUsername(update.getMessage().getFrom().getUserName()) == null) {
                        try {
                            use.setUsername(update.getMessage().getFrom().getUserName());
                        } catch (Exception e) {
                            use.setUsername(update.getMessage().getFrom().getFirstName());
                        }
                        use.setPassword("1111");
                        use.setActive(true);
                        use.setRoles(Collections.singleton(Role.USER));
                        use.setChat_id(update.getMessage().getChatId());
                        userRepo.save(use);
                        System.out.println("registration");
                    } else {
                        use = userRepo.findByUsername(update.getMessage().getFrom().getUserName());
                        System.out.println("login");
                    }
                    try {
                        sendApiMethod(sendMessageRemake("Вас приветсвует DemoBot telegram", update.getMessage().getChatId(), 1));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    break;
                case "Отключить админа":
                    if (use.isSupport_admin())
                        use.setSupport_admin(false);
                    try {
                        sendApiMethod(sendMessageRemake("... Админ отключен", update.getMessage().getChatId(), 1));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    break;
                case "Написать админу":

                    List<User> users = userRepo.findAll();

                    try {
                        for (User us : users) {
                            if (us.isAdmin()) {
                                admin = us;
                                use.setSupport_admin(true);
                                System.out.println(use.isSupport_admin());
                            }
                        }

                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                        try {
                            sendApiMethod(sendMessageRemake("Извените на даный момент нет свободних администраторов", update.getMessage().getChatId(), 1));
                        } catch (TelegramApiException e1) {
                            e1.printStackTrace();
                        }
                    }


                    if (admin != null) {
                        try {
                            sendApiMethod(sendMessageRemake("К вам подключен " + admin.getUsername() + ". Задайте вопрос которий вас волнует", update.getMessage().getChatId(), 2));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case "Просмотреть услуги":
                    String str = "";
                    for (String s : products) {
                        str += s + "\n";
                    }

                    try {
                        sendApiMethod(sendMessageRemake(str, update.getMessage().getChatId(), 1));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Назад":
                    try {
                        sendApiMethod(sendMessageRemake("...", update.getMessage().getChatId(), 1));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                 if(update.getMessage().isReply())
                 {
                     try {
                         sendApiMethod(new SendMessage().setChatId( update.getMessage().getReplyToMessage().getText().split("from")[1].trim()).setText(update.getMessage().getText()));
                     } catch (TelegramApiException e) {
                         e.printStackTrace();
                     }
                 }else if(use.isSupport_admin()) {
                        try {
                            sendApiMethod(new SendMessage().setChatId(Long.valueOf("-333820295")).setText(update.getMessage()
                                    .getText()+"\n from "+update.getMessage().getChatId()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            sendApiMethod(new SendMessage().setChatId(update.getMessage().getChatId()).setText(default1));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }}

                   break;


           }
        }


    }

    private SendMessage sendMessageRemake(String text, Long chat_id, int type)
    {

        ReplyKeyboardMarkup keyBoardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
       if (type==1) {
           KeyboardRow row = new KeyboardRow();
           KeyboardButton keyboardButton1 = new KeyboardButton();
           keyboardButton1.setText("Написать админу");
           KeyboardButton keyboardButton2 = new KeyboardButton();
           keyboardButton2.setText("Просмотреть услуги");
           row.add(keyboardButton1);
           row.add(keyboardButton2);
           keyboard.add(row);

       }
       if (type==2)
       {
           KeyboardRow row = new KeyboardRow();
           KeyboardButton keyboardButton1 = new KeyboardButton();
           keyboardButton1.setText("Отключить админа");
           KeyboardButton keyboardButton2 = new KeyboardButton();
           keyboardButton2.setText("Просмотреть услуги");
           row.add(keyboardButton1);
           row.add(keyboardButton2);
           keyboard.add(row);
       }
        keyBoardMarkup.setKeyboard(keyboard);
        keyBoardMarkup.setResizeKeyboard(true);
       return new SendMessage().setText(text).setChatId(chat_id).setReplyMarkup(keyBoardMarkup);
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
