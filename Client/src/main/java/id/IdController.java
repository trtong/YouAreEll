package id;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.apache.http.HttpResponse;
import shellutilities.Console;
import shellutilities.RequestType;
import shellutilities.YouAreEll;

import java.io.IOException;
import java.util.*;

public class IdController {

    private YouAreEll url;
    private final String MAIN_URL = "/ids";
    private String[] userLine;
    private int size;
    private HashMap<String, String>  allIds;


    public IdController(String [] cmds) throws IOException {
        url = new YouAreEll();
        userLine = cmds;
        size = cmds.length;
        allIds = allIdSet();
    }

    public void setUrl(YouAreEll url) {
        this.url = url;
    }

    public void idController() {

        if (size > 1) {
            Id tempId = idCreator();
            try {
                if (idExists(userLine[2])) {
                    Console.print("Putting updated entry");
                    PrettyPrintId.prettyPrintSingle(YouAreEll.readCalls(put_id(tempId.toString())));

                } else if (!idExists(userLine[2])) {
                    Console.print("Posting new entry");
                    PrettyPrintId.prettyPrintSingle(YouAreEll.readCalls(post_id(tempId.toString())));
                }
            } catch (ArrayIndexOutOfBoundsException a) {
                Console.print("Invalid ids menu option. Try again?");
            }
        } else {
            try {
                PrettyPrintId.prettyPrintJsonArray(YouAreEll.readCalls(get_ids()));
            } catch (Exception e) {
                Console.print("Could not gather Id's try again?");
            }
        }
    }

    private HashMap<String, String> getAllIds() {
        return allIds;
    }

    private List<Id> idSet() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonOrgModule());

        List<Id> idArray = mapper.readValue(YouAreEll.readCalls(this.get_ids()), new TypeReference<List<Id>>() {
        });

        return idArray;
    }

    private HashMap<String, String>  allIdSet() throws IOException {
        HashMap<String, String>  allids = new HashMap<>();
        for (Id id: idSet()) {
            allids.put(id.github, id.userid);
        }

        return allids;
    }

    public boolean idExists(String github) {
        return getAllIds().keySet().contains(github);
    }

    public  Id idCreator() {
        try {
            if (idExists(userLine[2])) {
                return new Id(getAllIds().get(userLine[2]), userLine[1], userLine[2]);
            }
            return new Id("", userLine[1], userLine[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public HttpResponse get_ids() {
        return url.MakeURLCall(MAIN_URL, RequestType.GET, "");
    }

    public HttpResponse post_id(String payload) {
        return url.MakeURLCall(MAIN_URL, RequestType.POST, payload);
    }

    public HttpResponse put_id(String payload) {
        return url.MakeURLCall(MAIN_URL, RequestType.PUT, payload);
    }

}
