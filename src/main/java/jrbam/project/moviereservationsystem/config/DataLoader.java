package jrbam.project.moviereservationsystem.config;

import jrbam.project.moviereservationsystem.entity.User;
import jrbam.project.moviereservationsystem.enums.UserRole;
import jrbam.project.moviereservationsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = userRepository.findByUsername("admin").orElse(User.emptyUser());

        if (user.getRole() == UserRole.ROLE_ADMIN &&
            passwordEncoder.matches("m0v1e_bvff", user.getPassword()))
            return;

        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("m0v1e_bvff"))
                .role(UserRole.ROLE_ADMIN)
                .build());
    }
}
