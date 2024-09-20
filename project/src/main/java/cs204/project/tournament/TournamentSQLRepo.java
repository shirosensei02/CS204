package cs204.project.tournament;

import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

@Repository
public class TournamentSQLRepo implements TournamentRepository {

  // TODO implement SQL connection here
  @Autowired
  private JdbcTemplate jdbcTemplate;
  // for testing
  // List<Tournament> tournaments = new ArrayList<>();

  public TournamentSQLRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public int deleteById(Long id) {
    // TODO change to delete from db
    // int size = tournaments.size();
    // tournaments.removeIf(tournament -> tournament.getId() == id);

    // return size - tournaments.size();
    return 0;
  }

  @Override
  public List<Tournament> findAll() {
    // TODO change to query all from db
    // return tournaments;
    return jdbcTemplate.query("SELECT * from tournaments",
        (rs, rownum) -> {
          String rankRangeJson = rs.getString("rankRange");

          int[] rankRange = null;
          if (rankRangeJson != null && !rankRangeJson.isEmpty()) {
            // Parse JSON array and convert to int[]
            JSONArray jsonArray = new JSONArray(rankRangeJson);
            rankRange = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
              rankRange[i] = jsonArray.getInt(i);
            }
          }

          String playerListJson = rs.getString("playerList");
          List<String> playerList = new ArrayList<>();
          if (playerListJson != null) {
            JSONArray playerArray = new JSONArray(playerListJson);
            for (int i = 0; i < playerArray.length(); i++) {
              playerList.add(playerArray.getString(i));
            }
          }

          return new Tournament(
              rs.getLong("id"),
              rs.getString("name"),
              rs.getString("date"),
              rankRange,
              rs.getString("status"),
              rs.getString("region"),
              playerList);
        });
  }

  @Override
  public Optional<Tournament> findById(Long id) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

  @Override
  public Long save(Tournament tournament) {
    // TODO change to add to db
    // TODO set id into tournament after adding into db
    // tournaments.add(tournament);
    // String saveSQL = "INSERT INTO Tournament (TournID, TournName, TournDate,
    // RankRange, TournStatus, TournRegion, PlayerList)";
    // jdbcTemplate.update(saveSQL, tournament.getId(),
    // tournament.getName(),
    // tournament.getDate(),
    // tournament.getRankRange(),
    // tournament.getStatus(),
    // tournament.getRegion(),
    // tournament.getPlayerList()
    // );

    // String saveSQLString = "insert into TOURNAMENTS (TourName, TournDate, RankRange, TournStatus,TournRegion, PlayerList) values (?, ?, ?, ?, ?, ?) ";
    String saveSQLString = "insert into TOURNAMENTS (id, name, date, rankRange, status, region, playerList) values (?, ?, ?, ?, ?, ?) ";
    // KeyHolder gets the auto-generated key from the INSERT mySQL statement
    GeneratedKeyHolder holder = new GeneratedKeyHolder();
    try {
    // Convert RankRange and PlayerList to JSON strings
    ObjectMapper objectMapper = new ObjectMapper();
    String rankRangeJson = objectMapper.writeValueAsString(tournament.getRankRange());
    String playerListJson = objectMapper.writeValueAsString(tournament.getPlayerList());

    jdbcTemplate.update((Connection conn) -> {
      PreparedStatement statement = conn.prepareStatement(
          saveSQLString,
          Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, tournament.getName());
      statement.setString(2, tournament.getDate());
      statement.setString(3, rankRangeJson);
      statement.setString(4, tournament.getStatus());
      statement.setString(5, tournament.getRegion());
      statement.setString(6, playerListJson);
      return statement;
    }, holder);

    Long primaryKey = holder.getKey().longValue();
    tournament.setId(primaryKey);
    return primaryKey;
  } catch (JsonProcessingException e) {
    e.printStackTrace();
    throw new RuntimeException("Error converting tournament data to JSON", e);
  }
  }

  @Override
  public int update(Tournament tournament) {
    // TODO Auto-generated method stub
    return 0;
  }

}
