package TicTacToe;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TicTacToeBot extends TelegramLongPollingBot {

    Map<User, String> signers = new LinkedHashMap<User, String>();
    int[][] buttonsLog = {{0, 0, 0},{0, 0, 0},{0, 0, 0}};

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/buttons")) {
                setKeyboard(update);
            }
        }else if (update.hasCallbackQuery()) {
            if(isButtonQuery(update)) {

                String call_data = update.getCallbackQuery().getData();
                long message_id = update.getCallbackQuery().getMessage().getMessageId();
                long chat_id = update.getCallbackQuery().getMessage().getChatId();
                User user = update.getCallbackQuery().getFrom();
                EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
                replyMarkup.setMessageId((int)message_id);
                replyMarkup.setChatId(String.valueOf(chat_id));


                int buttonIndex = Integer.valueOf(call_data.split(" ")[1]);
                InlineKeyboardMarkup markup = update.getCallbackQuery()
                        .getMessage()
                        .getReplyMarkup();
                List<List<InlineKeyboardButton>> keyboard =  markup.getKeyboard();
                InlineKeyboardButton callbackButton =keyboard.get(buttonIndex / 3).get(buttonIndex % 3);
                InlineKeyboardButton newButton = changeButtonWithUser(user, callbackButton);
                List<InlineKeyboardButton> rowsInline = keyboard.get(buttonIndex/3);
                rowsInline.set(buttonIndex % 3, newButton);
                keyboard.set(buttonIndex / 3, rowsInline);
                markup.setKeyboard(keyboard);
                replyMarkup.setReplyMarkup(markup);

                try {
                    execute(replyMarkup);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private InlineKeyboardButton changeButtonWithUser(User user, InlineKeyboardButton button) {
        if(signers.size()==2){
            if(button.getText().equals(" ")) {
                button.setText(signers.get(user));
            }
        } else if(signers.get(user)==null){
            if(signers.containsValue("❌")){
                signers.put(user, "❌");
            }
            else{
                signers.put(user, "⭕");
            }
            button.setText(signers.get(user));
        }
        return button;
    }

    public boolean isButtonQuery(Update update){
        return update.getCallbackQuery().getData().split(" ")[0].equals("button");
    }

    private void setKeyboard(Update update) {

            long chat_id = update.getMessage().getChatId();
            int fieldWidth = 3;

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
