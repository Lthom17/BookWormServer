package bookworm.data;

import bookworm.data.mappers.BookshelfMapper;
import bookworm.models.Bookshelf;
import bookworm.models.BookshelfType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class BookshelfJdbcTemplateRepository implements BookshelfRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookshelfJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bookshelf> findByLibraryId(UUID libraryId) {
        final String sql = "select *"
                + " from bookshelf"
                + " where member_library_id = ?"
                + " or group_library_id = ?;";

        return jdbcTemplate.query(sql, new BookshelfMapper(), libraryId.toString(), libraryId.toString());
    }

    @Override
    public Bookshelf findByBookshelfId(UUID bookshelfId) {
        final String sql = "select *"
                + " from bookshelf"
                + " where bookshelf_id = ?;";

        return jdbcTemplate.query(sql, new BookshelfMapper(), bookshelfId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public boolean add(Bookshelf bookshelf, UUID libraryId) {
        final String sql = "insert into bookshelf (bookshelf_id, name, parent_id, member_library_id, group_library_id)"
                + " values (?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                bookshelf.getBookshelfId().toString(),
                bookshelf.getName(),
                getParentBookshelfId(bookshelf),
                bookshelf.getType().equals(BookshelfType.MEMBER_SHELF) ? libraryId.toString() : null,
                bookshelf.getType().equals(BookshelfType.GROUP_SHELF) ? libraryId.toString() : null) > 0;
    }

    @Override
    public boolean update(Bookshelf bookshelf, UUID libraryId) {
        final String sql = "update bookshelf set"
                + " bookshelf_id = ?,"
                + " parent_id = ?,"
                + " member_library_id = ?,"
                + " group_library_id = ?"
                + " where bookshelf_id = ?;";

        return jdbcTemplate.update(sql,
                bookshelf.getBookshelfId().toString(),
                getParentBookshelfId(bookshelf),
                bookshelf.getType().equals(BookshelfType.MEMBER_SHELF) ? libraryId.toString() : null,
                bookshelf.getType().equals(BookshelfType.GROUP_SHELF) ? libraryId.toString() : null,
                bookshelf.getBookshelfId().toString()) > 0;
    }

    @Override
    @Transactional
    public boolean delete(UUID bookshelfId) {
        jdbcTemplate.update("delete from bookshelf where parent_id = ?;", bookshelfId.toString());
        return jdbcTemplate.update("delete from bookshelf where bookshelf_id = ?;", bookshelfId.toString()) > 0;
    }

    @Override
    public List<Bookshelf> findByBookshelfParentId(UUID bookshelfId) {
        final String sql = "select *"
                + " from bookshelf"
                + " where parent_id = ?;";

        return jdbcTemplate.query(sql, new BookshelfMapper(), bookshelfId.toString());
    }

    private String getParentBookshelfId(Bookshelf bookshelf) {
        return (bookshelf.getParentBookshelfId() != null) ?
                bookshelf.getParentBookshelfId().toString() : "";
    }
}