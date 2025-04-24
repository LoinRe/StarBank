package projectwork.starbank.dto;

public class User {

    private final String id;
    private final String username;
    private final String firstName;
    private final String lastName;

    public User(String id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
