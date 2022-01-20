package bookworm.controller;

import bookworm.data.MemberRepository;
import bookworm.domain.MemberService;
import bookworm.models.BookUser;
import bookworm.models.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private MemberRepository repository;

    @Test
    void shouldAddMember() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();
        BookUser bs = makeNewUser();
        String jsonIn = jsonMapper.writeValueAsString(bs);

        Member member = makeNewMember();
        when(repository.add(member)).thenReturn(member);

        var request = post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isCreated());
    }

    private BookUser makeNewUser(){
        String username = "BartSims";
        String password = "P@ssw0rd!";
        String email = "Bart@simpson.com";

        BookUser bs = new BookUser();
        bs.setUsername(username);
        bs.setPassword(password);
        bs.setEmail(email);

        return bs;

    }

    private Member makeNewMember(){
        String username = "BartSims";
        String password = "P@ssw0rd!";
        String email = "Bart@simpson.com";
        String firstName = "";
        String lastName = "";
        BookUser user = new BookUser(username, password, email, firstName, lastName);
        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");

        return new Member(user, roles, false);
    }

}
