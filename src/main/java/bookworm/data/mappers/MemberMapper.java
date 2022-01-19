package bookworm.data.mappers;

import bookworm.models.BookUser;
import bookworm.models.Library;
import bookworm.models.Member;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemberMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String password = resultSet.getString("password_hash");
        BookUser user = new BookUser(username, password, email, firstName, lastName);
        Library library = new Library(UUID.fromString(resultSet.getString("library_id")));
        List<String> roles = new ArrayList<>();
        roles.add(resultSet.getString("r.name"));
        boolean disabled = resultSet.getBoolean("disabled");
        return new Member(user, library, roles, disabled);
    }
}
