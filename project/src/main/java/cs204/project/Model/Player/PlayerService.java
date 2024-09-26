package cs204.project.Model.Player;

import org.springframework.security.core.userdetails.UserDetails;

public interface PlayerService {

    public UserDetails loadUserByUsername(String username);
    
    public Player getPlayer(Long id);
    public int getPlayerRank(Long id, String region);
} 
