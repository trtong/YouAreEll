package message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

import java.io.IOException;
import java.util.List;

public class PrettyPrintMessage {

    public static void prettyPrintMessageArray(String output) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonOrgModule());

        List<Message> messageList = mapper.readValue(output, new TypeReference<List<Message>>(){});

        if (messageList.size() > 20) {
            messageList = messageList.subList(messageList.size() - 20, messageList.size() - 1);
        }

        System.out.println(messageList.toString());
    }


    public static void prettyPrintSingleMessage(String output) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(output, Message.class);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

    }
}
