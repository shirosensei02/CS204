package cs204.project.model.tournament;
// import cs204.project.model.player.Player;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import cs204.project.exception.TournamentNotFoundException;

@Service
public class TournamentServiceImpl implements TournamentService{

    @Autowired
    private TournamentRepository tournaments;

    public TournamentServiceImpl(TournamentRepository tournaments){
      this.tournaments = tournaments;
    }

    public List<Tournament> getTournamentList() {
      return tournaments.findAll();
    }
    
    public Tournament addTournament(Tournament tournament) {
      return tournaments.save(tournament);
    }
    
    public void deleteTournament(Long id) throws EmptyResultDataAccessException {
      tournaments.deleteById(id);
    }
    
    public Tournament getTournament(Long id) {
      Optional<Tournament> t = tournaments.findById(id);

      if (t.isPresent()){
        return t.get();
      }
      return null;
    }

    public Tournament updateTournament(Long id, Tournament tournament) throws TournamentNotFoundException{
      Optional<Tournament> t = tournaments.findById(id);

      if (t.isPresent()) {
        return tournaments.save(tournament);
      } else {
        throw new TournamentNotFoundException(id);
      }
    }

    
    public Tournament addPlayerToTournament(Long tId, Long pId) {
      Optional<Tournament> t = tournaments.findById(tId);
      if (t.isPresent()) {
        Tournament tournament = t.get();
        List<Long> currentPlayerList = tournament.getPlayerList();
        currentPlayerList.add(pId);
        tournament.setPlayerList(currentPlayerList);
        return tournament;
      }
      return null;
      // return tournaments.addPlayer(player, tournament);
    }
    // Throw ???
    public Tournament removePlayerFromTournament(Long tId, Long pId) {
      Optional<Tournament> t = tournaments.findById(tId);
      if (t.isPresent()) {
        Tournament tournament = t.get();
        List<Long> currentPlayerList = tournament.getPlayerList();
        currentPlayerList.remove(pId);
        tournament.setPlayerList(currentPlayerList);
        return tournament;
      }
      return null;
      // return tournaments.addPlayer(player, tournament);
    }

    @Override
    public Tournament findTournamentByDate(LocalDate date) {
      // TODO Auto-generated method stub
      Optional<Tournament> t = tournaments.findTournamentByDate(date);
      return t.isPresent() ? t.get() : null;
    }

    @Override
    public Tournament findTournamentByName(String name) {
      Optional<Tournament> t = tournaments.findTournamentByName(name);
      return t.isPresent() ? t.get() : null;
    }
    // @Override
    // public List<Long> getPlayerListByTId(Long id) {
    //   Optional<Tournament> t = tournaments.findById(id);
    //   return t.isPresent() ? t.get().getPlayerList() : null;
    // }
    @Override
    public void isTournamentValid(Long tId) {
      
    }
}
