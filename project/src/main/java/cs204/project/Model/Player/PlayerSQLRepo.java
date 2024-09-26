package cs204.project.Model.Player;

// package cs204.project.tournament;
// import cs204.project.Model.User.MyAppUser;


import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Array;
import java.sql.Date;
// import java.time.LocalDate;

import java.sql.ResultSet;
import java.sql.SQLException;

// import org.json.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

@Repository
public class PlayerSQLRepo implements PlayerRepository {

  // TODO implement SQL connection here
  @Autowired
  private JdbcTemplate jdbcTemplate;
  // for testing
  // List<Tournament> tournaments = new ArrayList<>();

  public PlayerSQLRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public int deleteById(Long id) {
    // TODO change to delete from db
    // int size = tournaments.size();
    // tournaments.removeIf(tournament -> tournament.getId() == id);

    // Method 1:
    String deleteSQL = "DELETE from player WHERE ID = ?";
    int affectedRows = jdbcTemplate.update(deleteSQL, id);
    if (affectedRows > 0) {
      return affectedRows;
    } else {
      throw new PlayerNotFoundException(id);
    }
    // Method 2:
    // String deleteSQL = "DELETE from tournaments WHERE ID = ?";
    // return jdbcTemplate.update(deleteSQL, id);
    
    // If got delete, num rows deleted returned
    // If no delete, either by no ID or what, return 0?
    // So no need handle IDnotFound?
    // 

    // return size - tournaments.size();
    // return 0;
  }

  @Override
  public List<Player> findAll() {
    // TODO change to query all from db
    // return tournaments;

    return jdbcTemplate.query("SELECT * from tournaments",
        (rs, rownum) -> mapRow(rs, rownum));
  }

  @Override
  public Optional<Player> findById(Long id) {
    try {
      return Optional.ofNullable(
        jdbcTemplate.queryForObject(
          "SELECT * FROM player WHERE id = ?",
          (rs, rowNum) -> mapRow(rs, rowNum),
          id));

    } catch (EmptyResultDataAccessException e) {
      // book not found - return an empty object
      return Optional.empty();
    }
  }


  @Override
  public Long save(Player player) {
    String sql = "INSERT INTO player (PlayerName, PlayerPW, UserRole) " +
        "VALUES (?, ?, ?) RETURNING id"; // RETURNING id

    GeneratedKeyHolder holder = new GeneratedKeyHolder();
    jdbcTemplate.update((Connection conn) -> {
      PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        setDB(conn, statement, player);
      
      return statement;
    }, holder);

    Long primaryKey = holder.getKey().longValue();
    System.out.println(primaryKey);
    return primaryKey;
  }

  @Override
  public int update(Player player) {
    String sql = "UPDATE tournaments SET PlayerName = ?, PlayerPW = ?, UserRole = ? WHERE id = ?";

    return jdbcTemplate.update((Connection conn) -> {
      PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
        setDB(conn, statement, player);
      
      statement.setLong(7, player.getId());
      return statement;
    });
  }

  public PreparedStatement setDB(Connection conn, PreparedStatement statement, MyAppUser player) throws SQLException {
    statement.setString(1, player.getUsername());
    statement.setString(2, player.getPassword());
    statement.setString(3, player.getRole());
    return statement;
  }

  public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
    ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    // Retrieve the playerList JSON
    String playerListJson = rs.getString("playerList");
    List<Long> playerList = new ArrayList<>();
    if (playerListJson != null && !playerListJson.isEmpty()) {
      // Convert JSON string to ArrayList<Long>
      try {
        Long[] playerArray = objectMapper.readValue(playerListJson, Long[].class);
        playerList = new ArrayList<>(List.of(playerArray)); // Convert array to ArrayList
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // Map the ResultSet data to a Tournament object
    return new Tournament(
      rs.getLong("id"),
      rs.getString("name"),
      rs.getDate("date").toLocalDate(),
      Arrays.stream((Integer[]) rs.getArray("rankRange").getArray()).mapToInt(e -> (int) e).toArray(),
      rs.getString("status"),
      rs.getString("region"),
      playerList
    );
  }
}
