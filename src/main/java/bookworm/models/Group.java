package bookworm.models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Group {

    private final UUID GROUP_ID;

    private String name;
    private String description;
    private String owner;
    private List<String> groupMembers;
    private Library library;

    public Group(UUID GROUP_ID, String name, String description, String owner, List<String> groupMembers, Library library) {
        this.GROUP_ID = GROUP_ID;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.groupMembers = groupMembers;
        this.library = library;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public UUID getGROUP_ID() {
        return GROUP_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return GROUP_ID.equals(group.GROUP_ID) && name.equals(group.name) && Objects.equals(description, group.description) && owner.equals(group.owner) && groupMembers.equals(group.groupMembers) && library.equals(group.library);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GROUP_ID, name, description, owner, groupMembers, library);
    }
}