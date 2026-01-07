package jrbam.project.moviereservationsystem.service;

import jrbam.project.moviereservationsystem.dto.AuthUserDto;
import jrbam.project.moviereservationsystem.entity.User;
import jrbam.project.moviereservationsystem.enums.UserRole;
import jrbam.project.moviereservationsystem.exception.UserExistsException;
import jrbam.project.moviereservationsystem.repository.UserRepository;
import jrbam.project.moviereservationsystem.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String signupUser(AuthUserDto authUserDto) {

        if (this.userRepository.existsByUsername(authUserDto.getUsername()))
            throw new UserExistsException("User already exists", HttpStatus.BAD_REQUEST);

        User user = this.userRepository.save(User.builder()
                .username(authUserDto.getUsername())
                .password(this.passwordEncoder.encode(authUserDto.getPassword()))
                .role(UserRole.ROLE_USER)
                .build()
        );
        return this.jwtUtil.generateToken(user);
    }

    public String authenticateUser(String username) {
        return this.userRepository.findByUsername(username)
                .map(this.jwtUtil::generateToken)
                .orElseThrow(RuntimeException::new);
    }
}
