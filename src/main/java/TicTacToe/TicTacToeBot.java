package TicTacToe;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/buttons")) {
                setKeyboard(update);
            }
        }else if (update.hasCallbackQuery() && isButtonQuery(update)) {
            System.out.println(update.getCallbackQuery().getData());
        }
    }

    public boolean isButtonQuery(Update update){
        return update.getCallbackQuery().getData().split(" ")[0].equals("button");
    }

    private void setKeyboard(Update update) {

            long chat_id = update.getMessage().getChatId();
            int fieldWidth = 3;
            int[][] buttonsLog = {{0, 0, 0},{0, 0, 0},{0, 0, 0}};

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chat_id));
            message.setText("FIELD");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> listOfRows = new ArrayList<>();


            for(int i=0; i<3; i++){
                listOfRows.add(getNewRow(fieldWidth*i+1, fieldWidth));
            }

            markupInline.setKeyboard(listOfRows);
            message.setReplyMarkup(markupInline);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    private List<InlineKeyboardButton> getNewRow(int startIndex, int length) {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for(int j=0; j<length; j++){
            rowInline.add(buttonWithNumber(startIndex+j));
        }
        return rowInline;
    }

    private InlineKeyboardButton buttonWithNumber(int n){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(" ");
        button.setCallbackData("button "+n);
        return button;
    }

    @Override
    public String getBotUsername() {
        return "TicTacToeOnKeyboard";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}
