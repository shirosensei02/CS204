package cs204.project.tournament;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;

public interface TournamentRepository {
    Long save(Tournament tournament);
    int update(Tournament tournament);
    int deleteById(Long id);
    List<Tournament> findAll();

    Optional<Tournament> findById(Long id);
}
