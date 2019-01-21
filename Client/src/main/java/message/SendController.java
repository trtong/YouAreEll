package message;

import id.IdController;
import org.apache.http.HttpResponse;
import shellutilities.Console;
import shellutilities.RequestType;
import shellutilities.YouAreEll;

import java.io.IOException;

public class SendController  {
    private YouAreEll url;
    private Console c;
    private String[] userLine;
    private boolean validId;
    private IdController idCheck;


    public SendController(String[] userLine) throws IOException {
        url = new YouAreEll();
        this.userLine = userLine;
        c = new Console();
        idCheck = new IdController(userLine);
        validId = false;

    }

    public void setUrl(YouAreEll url) {
        this.url = url;
    }

    public void sendController() {
        try {
            setValidId(toIdValid());
            PrettyPrintMessage.prettyPrintSingleMessage(YouAreEll.readCalls(postMessage(userLine[1],
                    messageCreator().toString())));
    } catch (IOException e) {
            c.print("Error posting message, try again?");
        }
    }


    public void setValidId(boolean validId) {
        this.validId = validId;
    }

    private String getRawMsg() {
        int index = userLine.length;
        if (validId) {
            index = userLine.length - 1;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < index; i++) {
            sb.append(userLine[i] + " ");
        }
        return sb.toString().trim();
    }

    private boolean toIdValid() {
        try {
            return idCheck.idExists(userLine[userLine.length-1]);
        } catch (Exception e) {
            return false;
        }
    }

    public Message messageCreator() {
        if (this.validId) {
            return new Message("", "", userLine[1], userLine[userLine.length - 1], getRawMsg());
        } else {
            return new Message("", "", userLine[1], "", getRawMsg());
        }
    }

    public HttpResponse postMessage(String fromId, String payload) throws IOException {
        return url.MakeURLCall("/ids/" + fromId + "/messages", RequestType.POST, payload);
    }


}
