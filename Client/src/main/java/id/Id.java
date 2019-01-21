package id;

public class Id {
    public String userid, name, github;

    public Id() {
        super();
    }

    public Id(String userId, String name, String github) {
        this.userid = userId;
        this.name = name;
        this.github = github;
    }

    @Override
    public String toString() {
        return "\n{" +
                "\n\t\"userid\": \"" + this.userid + "\"," +
                "\n\t\"name\": \"" + this.name + "\"," +
                "\n\t\"github\": \"" + this.github + "\"" +
                "\n}";
    }

}
