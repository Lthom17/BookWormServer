package bookworm.controllers;

import bookworm.domain.MemberService;
import bookworm.models.ErrorResponse;
import bookworm.models.Result;
import bookworm.models.ResultType;
import bookworm.security.JwtConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/security")
public class AuthController {

    AuthenticationManager authManager;
    JwtConverter converter;
    private final MemberService memberService;

    public AuthController(AuthenticationManager authenticationManager, JwtConverter converter, MemberService memberService) {
        this.authManager = authenticationManager;
        this.converter = converter;
        this.memberService = memberService;

    }


    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody Map<String, String> credentials) {
        Result result = new Result();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        credentials.get("username"),
                        credentials.get("password"));
        try {
            Authentication authResult = authManager.authenticate(token);

            if (authResult.isAuthenticated()) {
                String jwt = converter.getTokenFromUser((User) authResult.getPrincipal());

                Map<String, String> tokenWrapper = new HashMap<>();
                tokenWrapper.put("jwt_token", jwt);

                return ResponseEntity.ok(tokenWrapper);
            }
        } catch (AuthenticationException e) {

            result.addMessage(e.getMessage(), ResultType.FORBIDDEN);

        }

        return ErrorResponse.build(result);
    }
}