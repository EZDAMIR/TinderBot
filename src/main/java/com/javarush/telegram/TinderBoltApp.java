package com.javarush.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = Config.get("TELEGRAM_BOT_NAME"); //TODO: добавь имя бота в файле config.properties
    public static final String TELEGRAM_BOT_TOKEN = Config.get("TELEGRAM_BOT_TOKEN"); //TODO: добавь токен бота в config.properties
    public static final String OPEN_AI_TOKEN = Config.get("OPEN_AI_TOKEN"); //TODO: добавь токен ChatGPT в config.properties

    private final ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;

    private final ArrayList<String> list = new ArrayList<>();

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
            sendTextMessage("Подождите пару секунд - ChatGPT думает...");
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
                sendTextMessage(" Отличный выбор! \nТвоя задача пригласить девушку/парня на ❤️ за 5 сообщений");

                String prompt = loadPrompt(query);
                chatGPT.setPrompt(prompt);
                return;
            }

            sendTextMessage("Подождите пару секунд - ChatGPT думает...");
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

                sendTextMessage("Подождите пару секунд - ChatGPT думает...");
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
