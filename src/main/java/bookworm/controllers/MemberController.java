package bookworm.controllers;

import bookworm.domain.MemberService;
import bookworm.models.BookUser;
import bookworm.models.ErrorResponse;
import bookworm.models.Member;
import bookworm.models.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("api/member")
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> findByUsername(@PathVariable String username) {
        Result result = service.findByUsername(username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping()
    public ResponseEntity<Object> addMember(@RequestBody BookUser bookUser) {
        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");
        Member member = new Member(bookUser, roles, false);
        Result result = service.add(member);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }
}