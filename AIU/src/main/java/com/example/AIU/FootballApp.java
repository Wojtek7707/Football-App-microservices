import com.example.AIU.Player;
import com.example.AIU.PlayerDto;
import com.example.AIU.Position;
import com.example.AIU.Team;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class FootballApp {

    public static void main(String[] args) throws Exception {

        // Zad1

        // tworzenie druzyn
        Team team1 = Team.builder()
                .name("FC Barcelona")
                .city("Barcelona")
                .attack(85).midfield(88).defense(84)
                .build();

        Team team2 = Team.builder()
                .name("Real Madrid")
                .city("Madrid")
                .attack(87).midfield(86).defense(85)
                .build();

        // tworzenie zawodnikow
        Player player1 = Player.builder()
                .name("Lewandowski Robert")
                .dateOfBirth(LocalDate.of(1988, 8, 21))
                .position(Position.FORWARD)
                .acceleration(80).shooting(91).passing(79).defense(40).dribble(80)
                .build();
        team1.addPlayer(player1);

        Player player2 = Player.builder()
                .name("Gavi")
                .dateOfBirth(LocalDate.of(2004, 8, 5))
                .position(Position.MIDFIELDER)
                .acceleration(85).shooting(65).passing(82).defense(78).dribble(85)
                .build();
        team1.addPlayer(player2);

        Player player3 = Player.builder()
                .name("Ter Stegen Marc-Andre")
                .dateOfBirth(LocalDate.of(1992, 4, 30))
                .position(Position.GOALKEEPER)
                .acceleration(50).shooting(15).passing(55).defense(92).dribble(50)
                .build();
        team1.addPlayer(player3);

        Player player4 = Player.builder()
                .name("Vinicius Junior")
                .dateOfBirth(LocalDate.of(2000, 7, 12))
                .position(Position.FORWARD)
                .acceleration(95).shooting(80).passing(78).defense(45).dribble(95)
                .build();
        team2.addPlayer(player4);

        Player player5 = Player.builder()
                .name("Bellingham Jude")
                .dateOfBirth(LocalDate.of(2003, 6, 29))
                .position(Position.MIDFIELDER)
                .acceleration(88).shooting(86).passing(85).defense(80).dribble(80)
                .build();
        team2.addPlayer(player5);
        List<Team> allTeams = new ArrayList<>(Arrays.asList(team1, team2));


        // Zad2: wyswietlanie kolekcji
        System.out.println("2: Wyświetlanie kolekcji");
        allTeams.forEach(team -> {
            System.out.println("\nDRUŻYNA: " + team.getName() + " (" + team.getCity() + ")");
            team.getPlayers().forEach(player ->
                    System.out.println("  - " + player.getName() + ", Poz: " + player.getPosition() +
                            ", STR: " + player.getShooting() + ", POD: " + player.getPassing()+", DRYBL: " + player.getDribble()+", DEF: " + player.getDefense())
            );
        });


        // Zad3: zbieranie wszystkich elementow do Set
        System.out.println("\n 3: Zbieranie wszystkich zawodników do Set");

        Set<Player> allPlayersSet = allTeams.stream() // stream drużyn
                .flatMap(team -> team.getPlayers().stream()) // zrownanie do streamu zawodnikow
                .collect(Collectors.toSet()); // zebranie do Set

        System.out.println("Liczba zawodników: " + allPlayersSet.size());
        allPlayersSet.stream().forEach(player -> System.out.println("  > " + player.getName()));



        // Zad4: filtracja i sortowanie
        System.out.println("\nKrok 4: Filtracja (FORWARD) i Sortowanie (ACCELERATION)");

        allPlayersSet.stream() // Użycie wcześniej utworzonej kolekcji Set
                .filter(player -> player.getPosition() == Position.FORWARD) // tylko napastnicy
                .sorted(Comparator.comparing(Player::getAcceleration).reversed()) // sortowanie-  największe przyspieszenie
                .forEach(player -> System.out.println("  > " + player.getName() +
                        " (ACC: " + player.getAcceleration() + ")"));


        // zad5: transformacja - mapowanie do DTO
        // orsz sortowanie
        System.out.println("\n5: Transformacja na DTO i Sortowanie (Wg Nazwy)");

        List<PlayerDto> allPlayerDtos = allPlayersSet.stream() // uzycie kolekcji zawodnikow
                .map(player -> PlayerDto.builder() // transformacja na PlayerDto
                        .name(player.getName())
                        .position(player.getPosition())
                        .dateOfBirth(player.getDateOfBirth())
                        .acceleration(player.getAcceleration())
                        .shooting(player.getShooting())
                        .passing(player.getPassing())
                        .defense(player.getDefense())
                        .dribble(player.getDribble())
                        .teamName(player.getTeam().getName()) // wprowadzenie nazwy drużyny zamiast obiektu
                        .build())
                .sorted() // Sortowanie Comparable na DTO po nazwie
                .collect(Collectors.toList());

        allPlayerDtos.stream().forEach(dto ->
                System.out.println("  [DTO] " + dto.getName() + " (Drużyna: " + dto.getTeamName() + ", Przysp.: " + dto.getAcceleration() + " ...)")
        );


        // Zad6: Serializacja
        System.out.println("\n6: Serializacja");
        final String FILENAME = "teams.ser";

        //zapis do pliku binarnego
        try (FileOutputStream fileOut = new FileOutputStream(FILENAME);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            // zapis całej listy allTeams (kategorii)
            out.writeObject(allTeams);
            System.out.println("kolekcja kategorii (allTeams) została zapisana do pliku: " + FILENAME);

        } catch (IOException i) {
            i.printStackTrace();
        }

        //odczyt z pliku binarnego
        List<Team> deserializedTeams = null;
        try (FileInputStream fileIn = new FileInputStream(FILENAME);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            // Odczyt i rzutowanie obiektu z powrotem na List<Team>
            // Konieczne jest rzutowanie z Object na List
            deserializedTeams = (List<Team>) in.readObject();
            System.out.println("kolekcja kategorii została wczytana z pliku.");

        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("klasa nie została znaleziona.");
            c.printStackTrace();
            return;
        }

        // printowanie
        System.out.println("\nWyświetlanie Kolekcji po Deserializacji");
        if (deserializedTeams != null) {
            // uzywamy zagniezdzonej lambdy forEach  do wyswietlenia
            // wszystkich kategorii i elementow, aby udowodnic, że relacje zostaly zachowane
            deserializedTeams.forEach(team -> {
                System.out.println("\n[Deserializowane] DRUŻYNA: " + team.getName() + " (" + team.getCity() + ")");
                team.getPlayers().forEach(player ->
                        System.out.println("  - " + player.getName() +
                                ", Poz: " + player.getPosition() +
                                ", Wiek: " + java.time.temporal.ChronoUnit.YEARS.between(player.getDateOfBirth(), LocalDate.now()))
                );
            });
        }
        // Zad7: Rownolegle Potoki z ForkJoinPool
        System.out.println("\nKrok 7: Równoległe Potoki z ForkJoinPool");

        //definicja rozmiarów puli do testowania
        List<Integer> poolSizes = Arrays.asList(2, 4);

        // iteracja po roznych rozmiarach puli
        for (int poolSize : poolSizes) {
            System.out.println("\nTEST: Rozmiar puli wątków = " + poolSize + " ");

            // tworzenie niestandardowej puli watkow ForkJoinPool
            ForkJoinPool customThreadPool = null;
            try {
                customThreadPool = new ForkJoinPool(poolSize);

                // wykonanie zadania rownoleglego na liscie allTeams (kategorii)
                // submit() jest uzywane do uruchomienia zadania w niestandardowej puli
                customThreadPool.submit(() ->
                        allTeams.parallelStream() // uzycie równoległego potoku na kategoriach
                                .forEach(team -> {
                                    System.out.println(Thread.currentThread().getName() +
                                            " - Przetwarzam drużynę: " + team.getName());

                                    // symulacja obciazenia: drukowanie elementow z przerwami
                                    team.getPlayers().forEach(player -> {
                                        try {
                                            // symulacja pracy (przetwarzania każdego gracza)
                                            Thread.sleep(1000);
                                            System.out.println(Thread.currentThread().getName() +
                                                    " -> Gracz: " + player.getName());
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                        }
                                    });
                                })
                ).get(); // .get() czeka na zakonczenie wszystkich zadan

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // zamknieciu puli wątkow
                if (customThreadPool != null) {
                    customThreadPool.shutdown();
                    // czekanie na zakonczenie watkow
                    try {
                        if (!customThreadPool.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                            System.err.println("Wątki nie zakończyły się w wymaganym czasie.");
                            customThreadPool.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        customThreadPool.shutdownNow();
                    }
                }
            }
        }
        System.out.println("\nKoniec testów równoległych");

    }
}