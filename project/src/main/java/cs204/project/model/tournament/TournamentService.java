package cs204.project.model.tournament;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TournamentService {
    // List<Long> getPlayerListByTId(Long id);
    public Tournament addPlayerToTournament(Long tId, Long PId);
    public Tournament findTournamentByDate(LocalDate date);
    public Tournament findTournamentByName(String name);
    public void isTournamentValid(Long tId);
}


