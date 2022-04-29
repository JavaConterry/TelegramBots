package WebOpener;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class WebOpenerBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String toSearch = update.getMessage().getText();
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(toSearch);

            try {
                execute(message);
                Runtime.getRuntime()
                        .exec(new String[]{
                                "cmd", "/c","start opera https://en.wikipedia.org/wiki/"+toSearch
                        });
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "WebOpener";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}

