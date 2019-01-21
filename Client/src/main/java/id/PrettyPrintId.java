package id;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import shellutilities.Console;

import java.io.IOException;
import java.util.List;

public class PrettyPrintId {

    public static void prettyPrintJsonArray(String output){

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JsonOrgModule());

            List<Id> idArray = mapper.readValue(output, new TypeReference<List<Id>>() {
            });

            System.out.println(idArray.toString());
        } catch (IOException e) {
            System.out.println("Error: Could not parse object or no object found.");
        }
    }

    public static void prettyPrintSingle(String output) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(output, Id.class);

            Console.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

        } catch (IOException e) {
            Console.print("Error: Could not parse object or no object found.");
        }

    }
}
