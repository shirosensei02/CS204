package cs204.project.controller;

// import cs204.project.Exception.TournamentIsFullException;
// import cs204.project.Exception.TournamentNotFoundException;
import cs204.project.exception.*;
// import cs204.project.model.player.*;
// import User-repo.src.main.java.cs204.project.model.player;
import cs204.project.model.tournament.Tournament;
import cs204.project.model.tournament.Tournament.Status;
import cs204.project.model.tournament.TournamentServiceImpl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController {
  private TournamentServiceImpl tournamentService;

  public TournamentController(TournamentServiceImpl tournamentService) {
    this.tournamentService = tournamentService;
  }

  @GetMapping("/tournaments")
  public List<Tournament> getTournaments() {
    return tournamentService.getTournamentList();
  }

  @GetMapping("tournaments/{id}")
  public Tournament getTournament(@PathVariable Long id) {
    return tournamentService.getTournament(id);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/tournaments")
  public Tournament addTournament(@RequestBody Tournament tournament) {
    System.out.println(tournament);
    return tournamentService.addTournament(tournament);
  }

  @PutMapping("/tournaments/{id}")
  public Tournament updateTournament(@PathVariable Long id, @RequestBody Tournament newTournamentInfo) {
    Tournament tournament = tournamentService.updateTournament(id, newTournamentInfo);
    if (tournament == null)
      throw new TournamentNotFoundException(id);
    return tournament;
  }

  @DeleteMapping("/tournaments/{id}")
  public void deleteTournament(@PathVariable long id) {
    try {
      tournamentService.deleteTournament(id);
    } catch (EmptyResultDataAccessException e) {
      throw new TournamentNotFoundException(id);
    }

  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @PutMapping("/tournaments/{id}/player/{pID}")
  public Long joinTournament(@PathVariable long id, @PathVariable long pId /* , @RequestBody Tournament tournament */) {

    Tournament tournament = tournamentService.getTournament(id);
    // Is this even needed?
    if (tournament.isPlayerListFull()) {
      throw new TournamentIsFullException(id);
    }
    // check date valid / tournament still open
    int lowerBoundRank = tournament.getRankRange()[0];
    int upperBoundRank = tournament.getRankRange()[1];
    int playerRank = playerService.getPlayerRank(id, tournament.getRegion());

    if (playerRank < lowerBoundRank && playerRank > upperBoundRank) {
      throw new PlayerRankOOBException(id);
    } else {
      tournamentService.updateTournament(id, tournamentService.addPlayerToTournament(id, pId));
      return pId; // ID of tournament
    }

  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @PutMapping("/tournaments/{id}/player/{pID}")
  public Long leaveTournament(@PathVariable long id, @PathVariable long pId /* , @RequestBody Tournament tournament */) {

    Tournament tournament = tournamentService.getTournament(id);
    // check date valid / status
    if (tournament.getStatus() == Status.CLOSED) {
      
    }
    
      tournamentService.updateTournament(id, tournamentService.removePlayerFromTournament(id, pId));
      return pId; // ID of tournament
    }

}
