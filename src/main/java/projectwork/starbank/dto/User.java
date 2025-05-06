package projectwork.starbank.dto;

/**
 * DTO (Data Transfer Object) для представления информации о пользователе.
 */
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    private final String id;
    /**
     * Имя пользователя (логин).
     */
    private final String username;
    /**
     * Имя пользователя.
     */
    private final String firstName;
    /**
     * Фамилия пользователя.
     */
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
