package bookworm.models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Library {
    private List<Bookshelf> bookshelves;
    private final UUID LIBRARY_ID;

    public Library() {
        this.LIBRARY_ID = UUID.randomUUID();
    }

    ;

    public Library(UUID library_id) {
        this.LIBRARY_ID = library_id;
    }

    public List<Bookshelf> getBookshelves() {
        return bookshelves;
    }

    public UUID getLibraryId() {
        return LIBRARY_ID;
    }

    public void setBookshelves(List<Bookshelf> bookshelves) {
        this.bookshelves = bookshelves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(bookshelves, library.bookshelves) && Objects.equals(LIBRARY_ID, library.LIBRARY_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookshelves, LIBRARY_ID);
    }
}