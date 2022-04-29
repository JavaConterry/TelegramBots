import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.util.Random;

public class RandBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            Random rand = new Random();
            int a = rand.nextInt(1,50);
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(String.valueOf(a));
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "ThrowRandomNumberBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}
