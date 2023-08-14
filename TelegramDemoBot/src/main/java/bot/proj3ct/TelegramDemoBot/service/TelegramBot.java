package bot.proj3ct.TelegramDemoBot.service;

import bot.proj3ct.TelegramDemoBot.config.BotConfig;
import bot.proj3ct.TelegramDemoBot.entity.DoctorServices;
import bot.proj3ct.TelegramDemoBot.entity.Services;
import bot.proj3ct.TelegramDemoBot.repositories.DoctorsRepository;
import bot.proj3ct.TelegramDemoBot.repositories.DoctorServicesRepository;
import bot.proj3ct.TelegramDemoBot.repositories.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private DoctorServicesRepository doctorServicesRepository;

    public TelegramBot(BotConfig config){
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> replyKeyboardServices(chatId);
                case "Консультация", "Рестоврация", "Лечение", "Имплант", "Удаление", "Брекет-система" ->
                        replyKeyboardDoctors(chatId, messageText);
                default -> sendMessage(chatId, "К сожалению данная команда недоступна.");
            }
        }else{
            sendMessage(update.getMessage().getChatId(), "К сожалению эта команда еще недоступна");
        }
    }

    // Display buttons with available services
    private void replyKeyboardServices(long chatID){
        List<Services> services = servicesRepository.findAll();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (Services service : services) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(service.getName()));
            keyboardRows.add(row);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        SendMessage message = new SendMessage(String.valueOf(chatID), "Добрый день, Уважаемый клиент!\nЗабронируйте удобное время для визита врача.");
        message.setReplyMarkup(replyKeyboardMarkup);

        try{
            execute(message);
        }catch(Exception ignored){
        }
    }

    // Display buttons of available doctors upon receive of service choice
    private void replyKeyboardDoctors(long chatID, String selectedService){
        List<DoctorServices> doctorServices = findDoctorByService(selectedService);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (DoctorServices doctorService : doctorServices) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(doctorService.getDoctor().getName()));
            keyboardRows.add(row);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        SendMessage message = new SendMessage(String.valueOf(chatID), "Выберите доктора");
        message.setReplyMarkup(replyKeyboardMarkup);

        try{
            execute(message);
        }catch(Exception ignored){
        }
    }

    // method to find doctor by given service
    private List<DoctorServices> findDoctorByService(String selectedService){
        List<DoctorServices> list = doctorServicesRepository.findAll();
        List<DoctorServices> returnList = new ArrayList<>();
        for(DoctorServices ds : list){
            if(ds.getService().getName().equals(selectedService)){
                returnList.add(ds);
            }
        }

        return returnList;
    }

    // method to send message
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch(TelegramApiException e){
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }
}
