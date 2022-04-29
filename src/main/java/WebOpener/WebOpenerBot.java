package WebOpener;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebOpenerBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                String toSearch = update.getMessage().getText();
                URL url = new URL("https://en.wikipedia.org/wiki/" + toSearch);
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(toSearch);
                execute(message);
                Runtime.getRuntime()
                        .exec(new String[]{
                                "cmd", "/c", "start opera " + url
                        });
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

