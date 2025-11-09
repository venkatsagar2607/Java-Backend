package ai.smartcity.controller;

import ai.smartcity.model.User;
import ai.smartcity.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){ this.userRepository = userRepository; }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        String email = (String) auth.getPrincipal();
        Optional<User> u = userRepository.findByEmail(email);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication auth, @RequestBody User payload) {
        String email = (String) auth.getPrincipal();
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isEmpty()) return ResponseEntity.notFound().build();

        User user = u.get();

        // ✅ Update all profile fields
        if (payload.getName() != null) user.setName(payload.getName());
        if (payload.getPhone() != null) user.setPhone(payload.getPhone());
        if (payload.getDob() != null) user.setDob(payload.getDob());
        if (payload.getAddress1() != null) user.setAddress1(payload.getAddress1());
        if (payload.getAddress2() != null) user.setAddress2(payload.getAddress2());
        if (payload.getCity() != null) user.setCity(payload.getCity());
        if (payload.getState() != null) user.setState(payload.getState());
        if (payload.getCountry() != null) user.setCountry(payload.getCountry());
        if (payload.getPincode() != null) user.setPincode(payload.getPincode());

        // Update password only if user entered it
        if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
            user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(payload.getPassword()));
        }

        userRepository.save(user);
        user.setPassword(null); // never return passwords
        return ResponseEntity.ok(user);
    }

}
