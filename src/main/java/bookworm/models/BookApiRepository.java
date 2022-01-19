package bookworm.models;

import java.util.List;

public interface BookApiRepository {
    Book findByIsbn(String isbn);

    List<Book> findByAuthor(String author);

    List<Book> findByTitle(String title);
}
