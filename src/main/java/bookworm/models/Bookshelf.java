package bookworm.models;

import java.util.List;
import java.util.UUID;

public class Bookshelf {

    private final BookshelfType TYPE;

    private UUID bookshelfId;
    private List<Book> books;
    private String name;
    private UUID parentBookshelfId;

    public Bookshelf(List<Book> books, String name, UUID bookshelfId, BookshelfType type) {
        this.books = books;
        this.name = name;
        this.bookshelfId = bookshelfId;
        this.TYPE = type;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getBookshelfId() {
        return bookshelfId;
    }

    public void setBookshelfId(UUID bookshelfId) {
        this.bookshelfId = bookshelfId;
    }

    public UUID getParentBookshelfId() {
        return parentBookshelfId;
    }

    public void setParentBookshelfId(UUID parentBookshelfId) {
        this.parentBookshelfId = parentBookshelfId;
    }

    public BookshelfType getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookshelf bookshelf = (Bookshelf) o;
        return TYPE == bookshelf.TYPE && bookshelfId.equals(bookshelf.bookshelfId) && Objects.equals(books, bookshelf.books) && name.equals(bookshelf.name) && Objects.equals(parentBookshelfId, bookshelf.parentBookshelfId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TYPE, bookshelfId, books, name, parentBookshelfId);
    }
}
