package cs204.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cs204.project.Model.Player.Player;
import cs204.project.Model.Player.PlayerRepository;

@RestController
public class UserRegistrationController {

    // @Autowired
    // private PlayerRepository userRepository;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // @PostMapping(value = "/user/signup", consumes = "application/json")
    // public Player createUser(@RequestBody Player user) {
    //     user.setPassword(passwordEncoder.encode(user.getPassword()));
    //     return userRepository.save(user);
    // }
}