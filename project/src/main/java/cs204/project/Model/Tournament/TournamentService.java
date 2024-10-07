package cs204.project.model.tournament;

import java.util.List;
import java.util.Optional;

import cs204.project.model.player.Player;

public interface TournamentService {
    List<Long> getPlayerListByTId(Long id);
}


