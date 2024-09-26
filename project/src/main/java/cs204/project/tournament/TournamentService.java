package cs204.project.tournament;
import cs204.project.Model.User.MyAppUser;

import java.util.List;

public interface TournamentService {
    List<Tournament> getTournamentList();
    Tournament getTournament(Long id);

    // return newly added tournament
    Tournament addTournament(Tournament tournament);

    /**  return updated tournament
    @param id
    @param tournament
    @return
    */ 
    Tournament updateTournament(Long id, Tournament tournament);

    /**
     * return status of delete
     * 1 if remove
     * 0 if does not exist
     * @param id
     * @return
     */
    int deleteTournament(Long id);

    public Long joinTournament(MyAppUser player, Tournament tournament);
}
