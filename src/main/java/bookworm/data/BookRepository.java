package bookworm.data;

import bookworm.models.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository {


    Book add(Book book, UUID bookshelf_id);

    boolean delete(Book book, UUID bookshelf_id);

    List<Book> findByBookshelfId(UUID bookshelf_id);

    boolean deleteByBookshelfId(UUID bookshelf_id);

}
