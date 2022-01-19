package bookworm.data;

import bookworm.models.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class MemberJdbcRepositoryTest {

    @Autowired
    MemberRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByUserName(){

        String username = "JohnnyTest";

        Member member = repository.findByUsername(username);

        assertNotNull(member);
        assertEquals(member.getUsername(), username);
    }

}
