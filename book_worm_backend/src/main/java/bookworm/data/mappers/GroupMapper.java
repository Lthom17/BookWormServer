package bookworm.data.mappers;

import bookworm.models.Group;
import bookworm.models.Library;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        UUID groupId = UUID.fromString(resultSet.getString("group_id"));
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String owner = resultSet.getString("owner");
        UUID libraryId = UUID.fromString(resultSet.getString("library_id"));

        Group group = new Group(groupId, name, description, owner, new ArrayList<String>(), new Library(libraryId));

        return group;
    }
}
