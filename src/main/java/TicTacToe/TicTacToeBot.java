package TicTacToe;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            setKeyboard();
        }
    }

    private void setKeyboard() {
        System.out.println("1:debug");
        ReplyKeyboardMarkup keyBoardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (int i = 1; i < 10; i++) {
            row.add(Integer.valueOf(i).toString());
            if (i % 3 == 0) {
                keyboard.add(row);
                row = new KeyboardRow();
            }
        }
        System.out.println("2:debug");
        row.add("0");
        keyboard.add(row);
        keyBoardMarkup.setKeyboard(keyboard);
        keyBoardMarkup.setResizeKeyboard(true);
        System.out.println("3:debug");
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
