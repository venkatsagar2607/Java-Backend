package ai.smartcity.controller;

import ai.smartcity.model.User;
import ai.smartcity.payload.AuthRequest;
import ai.smartcity.payload.AuthResponse;
import ai.smartcity.payload.RegisterRequest;
import ai.smartcity.repository.UserRepository;
import ai.smartcity.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository, JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest req){
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRoles(Collections.singletonList("USER"));
        userRepository.save(u);
        String token = jwtUtils.generateToken(u.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req){
        return userRepository.findByEmail(req.getEmail())
            .map(u -> {
                if (passwordEncoder.matches(req.getPassword(), u.getPassword())) {
                    return ResponseEntity.ok(new AuthResponse(jwtUtils.generateToken(u.getEmail())));
                } else {
                    return ResponseEntity.status(401).body("Invalid credentials");
                }
            })
            .orElse(ResponseEntity.status(404).body("User not found"));
    }
}
