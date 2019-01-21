package message;

public class Message {

    public String sequence, fromid, toid, message;
    public String timestamp;

    public Message() {
        super();
    }

    public Message(String sequence, String timestamp, String fromid, String toid, String message) {
        this.sequence = sequence;
        this.timestamp = timestamp;
        this.fromid = fromid;
        this.toid = toid;
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n{" +
                "\n\t\"sequence\": \"" + this.sequence + "\",");

        if (!this.timestamp.equals("")) {
            sb.append("\n\t\"timestamp\": \"" + this.timestamp + "\",");
        }

        sb.append("\n\t\"fromid\": \"" + this.fromid + "\"," +
                "\n\t\"toid\": \"" + this.toid + "\"," +
                "\n\t\"message\": \"" + this.message + "\"" +
                "\n}");

        return sb.toString();
    }
}
