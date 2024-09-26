package cs204.project.Model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cs204.project.tournament.Tournament;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {

    Optional<MyAppUser> findByUsername(String username);

    Long save(MyAppUser player);
    int update(MyAppUser player);
    int deleteById(Long id);
    List<MyAppUser> findAll();

    Optional<MyAppUser> findById(Long id);

}
