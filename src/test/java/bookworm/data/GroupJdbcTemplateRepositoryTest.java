package bookworm.data;

import bookworm.models.Group;
import bookworm.models.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Profile("testing")
class GroupJdbcTemplateRepositoryTest {

    @Autowired
    GroupJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByGroupName() {
        assertEquals(1, repository.findByName("The Scotland Saints").size());
    }

    @Test
    void shouldFindByOwner() {
        assertEquals(1, repository.findByOwner("SallyJ23").size());
    }

    @Test
    void shouldFindByGroupId() {
        Group testGroup = new Group(UUID.fromString("61526f94-7328-11ec-90d6-0242ac120003"),
                "The Ginger Elephants",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Mus mauris vitae ultricies leo integer.",
                "JohnnyTest",
                new ArrayList<>(),
                new Library(UUID.fromString("61526bb6-7328-11ec-90d6-0242ac120003")));

        assertEquals(testGroup, repository.findByGroupId(UUID.fromString("61526f94-7328-11ec-90d6-0242ac120003")));
    }

    @Test
    void shouldAddGroup() {
        assertTrue(repository.add("Test Group", "Test description", "Farmer123"));
    }

    @Test
    void shouldUpdateGroupName() {
        Group testGroup = new Group(UUID.fromString("61526a9e-7328-11ec-90d6-0242ac120003"),
                "Belgium Blues",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Mus mauris vitae ultricies leo integer.",
                "SallyJ23",
                new ArrayList<>(),
                new Library(UUID.fromString("615269a4-7328-11ec-90d6-0242ac120003")));

        assertTrue(repository.update(testGroup));
    }

    @Test
    void shouldUpdateGroupDescription() {
        Group testGroup = new Group(UUID.fromString("61526a9e-7328-11ec-90d6-0242ac120003"),
                "The Scotland Saints",
                "Test description",
                "SallyJ23",
                new ArrayList<>(),
                new Library(UUID.fromString("615269a4-7328-11ec-90d6-0242ac120003")));

        assertTrue(repository.update(testGroup));
    }

    @Test
    void shouldDeleteGroup() {
        assertTrue(repository.delete(UUID.fromString("bc06c542-7179-402f-8c61-b8aa1741a672")));
    }

    @Test
    void shouldAddGroupMember() {
        assertTrue(repository.addGroupMember("FriesNFrosties", UUID.fromString("61526f94-7328-11ec-90d6-0242ac120003")));
    }

    @Test
    void shouldDeleteGroupMember() {
        assertTrue(repository.deleteGroupMember("FriesNFrosties", UUID.fromString("61526a9e-7328-11ec-90d6-0242ac120003")));
    }
}
