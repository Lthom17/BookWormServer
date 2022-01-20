package bookworm.data;

import bookworm.models.Bookshelf;

import java.util.List;
import java.util.UUID;

public interface BookshelfRepository {

    List<Bookshelf> findByLibraryId(UUID libraryId);

    Bookshelf findByBookshelfId(UUID bookshelfId);

    List<Bookshelf> findByBookshelfParentId(UUID bookshelfId);

    boolean add(Bookshelf bookshelf, UUID libraryId);

    boolean update(Bookshelf bookshelf, UUID libraryId);

    boolean delete(UUID bookshelfId);
}
