package cs204.project.Model.Player;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cs204.project.Model.Tournament.Tournament;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService, UserDetailsService {

    private final PlayerRepository repositorys;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player user = repositorys.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")  // Pass just "USER", not "ROLE_USER"
                .build();
    }

    @Override
    public Player getPlayer(Long id) {
        Optional<Player> p = repositorys.findById(id);
        return p.isPresent() ? p.get() : null;
    }

    @Override
    public int getPlayerRank(Long id, String region) {
        Optional<Integer> rank = repositorys.findPlayerRank(id, region);
        return rank.isPresent() ? rank.get() : null;
    }
}