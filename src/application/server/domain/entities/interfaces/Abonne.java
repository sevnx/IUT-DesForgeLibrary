package application.server.domain.entities.interfaces;

public interface Abonne {
    int getId();

    int getAge();

    boolean isBanned();

    void banUser() throws Exception;

    void unbanUser() throws Exception;
}
