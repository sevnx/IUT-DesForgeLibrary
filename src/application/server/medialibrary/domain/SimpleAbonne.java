package application.server.medialibrary.domain;

import application.server.medialibrary.domain.core.Abonne;

import java.time.LocalDate;
import java.time.Period;

public class SimpleAbonne implements Abonne {
    private final int id;
    private final String name;
    private final LocalDate birthday;

    public SimpleAbonne(int id, String name, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    // TODO: Gerinimo BretteSoft Certification (isBanned)
}
