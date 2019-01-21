package message;

import id.IdController;
import org.apache.http.HttpResponse;
import shellutilities.Console;
import shellutilities.RequestType;
import shellutilities.YouAreEll;

import java.io.IOException;

public class MessageController {
    private YouAreEll url;
    private final String MAIN_URL = "/messages";
    private Console c;
    private String[] userLine;
    private IdController ic;

    public MessageController(String[] userLine) throws IOException {
        this.userLine = userLine;
        url = new YouAreEll();
        c = new Console();
        ic = new IdController(userLine);
    }

    public void setUrl(YouAreEll url) {
        this.url = url;
    }

    public void messageController() throws IOException {
        try {
            switch (userLine.length) {
                case (1):
                    PrettyPrintMessage.prettyPrintMessageArray(YouAreEll.readCalls(get_messages()));
                    break;
                case (2):
                    PrettyPrintMessage.prettyPrintMessageArray(YouAreEll.readCalls(getMessageToId(userLine[1])));
                    break;
                case (3):
                    try {
                        if (ic.idExists(userLine[2])) {
                            PrettyPrintMessage.prettyPrintMessageArray(YouAreEll.readCalls(get_messages_from(userLine[1], userLine[2])));
                            break;
                        }
                    } catch (IOException e) {
                            c.print("Unable to get messages. Try again.");
                    }
                    break;
                default:
                    c.print("Error, no messages found. Try again.");
                    break;
            }
        } catch(NullPointerException nullEx){
            c.print("Not a valid github name.");
        }
    }

    public HttpResponse get_messages() {
        return url.MakeURLCall(MAIN_URL, RequestType.GET, "");
    }

    public HttpResponse get_messages_from(String toId, String fromId) {
        return url.MakeURLCall("/ids/" + toId + "/from/" + fromId , RequestType.GET, "");
    }

    public HttpResponse getMessageToId(String github) {
        return url.MakeURLCall("/ids/" + github + MAIN_URL, RequestType.GET, "");
    }
}
