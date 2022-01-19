package bookworm.data.mappers;

import bookworm.models.Book;
import bookworm.models.Bookshelf;
import bookworm.models.BookshelfType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class BookshelfMapper implements RowMapper<Bookshelf> {

    @Override
    public Bookshelf mapRow(ResultSet resultSet, int i) throws SQLException {
        UUID bookshelfId = UUID.fromString(resultSet.getString("bookshelf_id"));
        String shelfName = resultSet.getString("name");
        String parentId = resultSet.getString("parent_id");
        String memberLibId = resultSet.getString("member_library_id");
        BookshelfType type = (memberLibId != null ? BookshelfType.MEMBER_SHELF : BookshelfType.GROUP_SHELF);
        Bookshelf bookshelf = new Bookshelf(new ArrayList<Book>(), shelfName, bookshelfId, type);

        if (!parentId.isBlank()) {
            bookshelf.setParentBookshelfId(UUID.fromString(parentId));
        }
        return bookshelf;
    }
}