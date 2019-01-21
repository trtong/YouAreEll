package message;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;

public class MessageMapper {

    public static Message returnObject(String jsonPayload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonPayload, Message.class);

    }
}
