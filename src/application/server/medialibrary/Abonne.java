package application.server.medialibrary;

import java.time.LocalDate;
import java.time.Period;

public class Abonne {
    private final int id;
    private final String name;
    private final LocalDate birthdate;

    public Abonne(int id, String name, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return Period.between(this.birthdate, LocalDate.now()).getYears();
    }

    // TODO: Gerinimo BretteSoft Certification (isBanned)
}
