package cs204.project;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import io.github.cdimascio.dotenv.Dotenv;

import cs204.project.tournament.Tournament;
import cs204.project.tournament.TournamentRepository;

import java.util.List;
import java.util.Arrays;
import java.time.LocalDate;

@SpringBootApplication
public class ProjectApplication {

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure().load();
    System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

    ApplicationContext ctx = SpringApplication.run(ProjectApplication.class, args);

    JdbcTemplate template = ctx.getBean(JdbcTemplate.class);
    TournamentRepository repo = ctx.getBean(TournamentRepository.class);

    template.execute(
        "CREATE TABLE IF NOT EXISTS tournaments (" +
            "id BIGSERIAL PRIMARY KEY," +
            "name VARCHAR(255) NOT NULL," +
            "date DATE NOT NULL," +
            "rankRange INT[] NOT NULL," +
            "status VARCHAR(50) NOT NULL," +
            "region VARCHAR(100) NOT NULL," +
            "playerList JSON" +
            ")");

    template.execute(
        "CREATE TABLE IF NOT EXISTS player(" +
            "ID INT NOT NULL," +
            "PlayerName VARCHAR(50) NOT NULL," +
            "PW VARCHAR(50) NOT NULL," +
            "UserRole VARCHAR(50) NOT NULL," +
            "CONSTRAINT player_pk PRIMARY KEY (ID)" +
            ")");

    template.execute(
        "CREATE TABLE IF NOT EXISTS administrator(" +
            "ID INT NOT NULL," +
            "AdminName VARCHAR(50) NOT NULL," +
            "PW VARCHAR(50) NOT NULL," +
            "UserRole VARCHAR(50) NOT NULL," +
            "CONSTRAINT admin_pk PRIMARY KEY (ID)" +
            ")");

    template.execute(
        "CREATE TABLE PLAYERRANK(" +
            "PID			INT			NOT NULL," +
            "Region		VARCHAR(50)	NOT NULL," +
            "PlayerRank	VARCHAR(50)	NOT NULL," +
            "CONSTRAINT playerrank_pk	PRIMARY KEY (PID, Region)," +
            "CONSTRAINT playerrank_fk	FOREIGN KEY (PID) REFERENCES PLAYER(ID)" +
            ")");
            
    template.execute(
        "CREATE TABLE TOURNAMENTLIST( " +
            "PID			INT			NOT NULL," +
            "Region		VARCHAR(50)	NOT NULL," +
            "TID			INT			NOT NULL," +
            "TournStatus VARCHAR(50)	NOT NULL," +
            "CONSTRAINT tlist_pk		PRIMARY KEY(PID, Region, TID)," +
            "CONSTRAINT tlist_fk1	FOREIGN KEY (PID) REFERENCES PLAYER(ID)," +
            "CONSTRAINT tlist_fk2	FOREIGN KEY (TID) REFERENCES TOURNAMENT(TournID)" +
            ")");

    // List<Tournament> listTournaments = Arrays.asList(
    // new Tournament("t1", LocalDate.of(2024, 9, 23), new int[]{1,2}, "open",
    // "asia"),
    // new Tournament("t2", LocalDate.of(2024, 9, 23), new int[]{3,6}, "open",
    // "west")
    // );

    List<Tournament> listTournaments = Arrays.asList(
        new Tournament("t1", LocalDate.of(2024, 9, 23), new int[] { 1, 2 }, "open", "asia"),
        new Tournament("t2", LocalDate.of(2024, 9, 23), new int[] { 3, 6 }, "open", "west"));

    listTournaments.forEach(tournament -> {
      repo.save(tournament);
    });
  }

}
