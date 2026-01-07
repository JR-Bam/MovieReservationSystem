package jrbam.project.moviereservationsystem.controller;

import jrbam.project.moviereservationsystem.dto.AuthTokenResponseDto;
import jrbam.project.moviereservationsystem.dto.AuthUserDto;
import jrbam.project.moviereservationsystem.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserAuthService userAuthService, AuthenticationManager authenticationManager) {
        this.userAuthService = userAuthService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthTokenResponseDto> signupUser(@RequestBody AuthUserDto authUserDto) {
        String token = userAuthService.signupUser(authUserDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthTokenResponseDto.builder().token(token).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponseDto> loginUser(@RequestBody AuthUserDto authUserDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUserDto.getUsername(), authUserDto.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String token = userAuthService.authenticateUser(authUserDto.getUsername());
        return ResponseEntity.ok(AuthTokenResponseDto.builder().token(token).build());
    }
}
