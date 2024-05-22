package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "Tinder_AI_KZ_Bot"; //TODO: добавь имя бота в кавычках
    public static final String TELEGRAM_BOT_TOKEN = "6813312046:AAGXV7YorAtZmVwQIvOCWBESX0pTdBo83Vw"; //TODO: добавь токен бота в кавычках
    public static final String OPEN_AI_TOKEN = "sk-proj-C7DBdRdA7RvjnXSQ8RhST3BlbkFJZKwfS3Qfzb5p15Wnxds8"; //TODO: добавь токен ChatGPT в кавычках

    private final ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;

    private ArrayList<String> list = new ArrayList<>();

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }
    @Override
    public void onUpdateEventReceived(Update update) {
        //TODO: основной функционал бота будем писать здесь
        String message = getMessageText();

        if(message.equals("/start")){
            currentMode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String text = loadMessage("main");
            sendTextMessage(text);

            showMainMenu("главное меню бота", "/start",
                    "генерация Tinder-профля \uD83D\uDE0E", "/profile",
                    "сообщение для знакомства \uD83E\uDD70", "/opener",
                    "переписка от вашего имени \uD83D\uDE08", "/message",
                    "переписка со звездами \uD83D\uDD25", "/date",
                    "задать вопрос чату GPT \uD83E\uDDE0","/gpt");
            return;
        }
        if(message.equals("/gpt")){
            currentMode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }

        if(currentMode == DialogMode.GPT){
            String prompt = loadPrompt("gpt");
            Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
            String answer = chatGPT.sendMessage(prompt, message);
            sendTextMessage(answer);
            return;
        }

        if(message.equals("/date")){
            currentMode = DialogMode.DATE;
            sendPhotoMessage("date");

            String text = loadMessage("date");

            sendTextButtonsMessage(text,
                    "Ариана Гранде", "date_grande",
                    "Марго Робби", "date_robbie",
                    "Зендея", "date_zendaya",
                    "Райан Гоослинг", "date_gosling",
                    "Том Харди", "date_hardy"
                    );
            return;
        }

        if(currentMode == DialogMode.DATE){
            String query = getCallbackQueryButtonKey();
            if(query.startsWith("date_")){
                sendPhotoMessage(query);
                sendTextMessage(" Отличный выбор! \nТвоя задача пригласить девушку/парня на ❤\uFE0F за 5 сообщений");

                String prompt = loadPrompt(query);
                chatGPT.setPrompt(prompt);
                return;
            }

            Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
            String answer = chatGPT.addMessage(message);
            sendTextMessage(answer);
            return;
        }

        //command MESSAGE
        if(message.equals("/message")){
            currentMode = DialogMode.MESSAGE;
            sendPhotoMessage("message");
            sendTextButtonsMessage("Пришлите в чат вашу переписку",
                    "Следующее сообщение","message_next",
                    "Пригласить на свидание","message_date"
                    );
            return;
        }

        if(currentMode == DialogMode.MESSAGE){
            String query = getCallbackQueryButtonKey();
            if(query.startsWith("message_")){
                String prompt = loadPrompt(query);
                String userChatHistory = String.join("\n\n", list);

                Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
                String answer = chatGPT.sendMessage(prompt, userChatHistory);
                sendTextMessage(answer);
                return;
            }

            list.add(message);
            return;
        }

        sendTextMessage("*Привет!*");
        sendTextMessage("*_Привет!*_");

        sendTextMessage("Вы написали " + message);

        sendTextButtonsMessage("Выберите режим работы:",
                "Старт", "start",
                "Стоп", "stop"
                );
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
