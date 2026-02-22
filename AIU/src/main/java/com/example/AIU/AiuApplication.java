package com.example.AIU;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@SpringBootApplication
public class AiuApplication {

	public static void main(String[] args) {

		//SpringApplication.run(AiuApplication.class, args);
		// =======================================================
		// Krok 2.1: Inicjalizacja Danych z Builderem
		// =======================================================

		// Tworzenie Drużyn (Kategorii)
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

		// Tworzenie Zawodników (Elementów) i ustawienie relacji dwukierunkowej
		Player player1 = Player.builder()
				.name("Lewandowski Robert")
				.dateOfBirth(LocalDate.of(1988, 8, 21))
				.position(Position.FORWARD)
				.acceleration(80).shooting(91).passing(79)
				.build();
		team1.addPlayer(player1); // Ustawia player.team i dodaje do team.players

		Player player2 = Player.builder()
				.name("Gavi")
				.dateOfBirth(LocalDate.of(2004, 8, 5))
				.position(Position.MIDFIELDER)
				.acceleration(85).shooting(65).passing(82)
				.build();
		team1.addPlayer(player2);

		Player player3 = Player.builder()
				.name("Ter Stegen Marc-Andre")
				.dateOfBirth(LocalDate.of(1992, 4, 30))
				.position(Position.GOALKEEPER)
				.acceleration(50).shooting(15).passing(55)
				.build();
		team1.addPlayer(player3);

		Player player4 = Player.builder()
				.name("Vinicius Junior")
				.dateOfBirth(LocalDate.of(2000, 7, 12))
				.position(Position.FORWARD)
				.acceleration(95).shooting(80).passing(78)
				.build();
		team2.addPlayer(player4);

		Player player5 = Player.builder()
				.name("Bellingham Jude")
				.dateOfBirth(LocalDate.of(2003, 6, 29))
				.position(Position.MIDFIELDER)
				.acceleration(88).shooting(86).passing(85)
				.build();
		team2.addPlayer(player5);

		List<Team> allTeams = new ArrayList<>(Arrays.asList(team1, team2));

		// =======================================================
		// Krok 2.2: Wyświetlanie Kolekcji
		// =======================================================
		System.out.println("--- Krok 2.2: Wyświetlanie kolekcji ---");
		allTeams.forEach(team -> {
			System.out.println("\nDRUŻYNA: " + team.getName() + " (" + team.getCity() + ")");
			team.getPlayers().forEach(player ->
					System.out.println("  - " + player.getName() + ", Poz: " + player.getPosition() +
							", STR: " + player.getShooting() + ", PAS: " + player.getPassing())
			);
		});

		// =======================================================
		// Krok 3: Zbieranie wszystkich elementów do Set (Wymagane)
		// =======================================================
		System.out.println("\n--- Krok 3: Zbieranie wszystkich zawodników do Set ---");

		Set<Player> allPlayersSet = allTeams.stream() // Stream drużyn
				.flatMap(team -> team.getPlayers().stream()) // Zrównanie do Streamu zawodników
				.collect(Collectors.toSet()); // Zebranie do Set

		System.out.println("Liczba unikalnych zawodników: " + allPlayersSet.size());
		allPlayersSet.stream().forEach(player -> System.out.println("  > " + player.getName()));


		// =======================================================
		// Krok 4: Filtracja i Sortowanie (Wymagane)
		// =======================================================
		System.out.println("\n--- Krok 4: Filtracja (FORWARD) i Sortowanie (ACCELERATION) ---");

		allPlayersSet.stream() // Użycie wcześniej utworzonej kolekcji Set
				.filter(player -> player.getPosition() == Position.FORWARD) // Filtracja: tylko napastnicy
				.sorted(Comparator.comparing(Player::getAcceleration).reversed()) // Sortowanie: największe przyspieszenie
				.forEach(player -> System.out.println("  > " + player.getName() +
						" (ACC: " + player.getAcceleration() + ")"));


		// =======================================================
		// Krok 5: Transformacja (Mapowanie do DTO) i Sortowanie (Wymagane)
		// =======================================================
		System.out.println("\n--- Krok 5: Transformacja na DTO i Sortowanie (Wg Nazwy) ---");

		List<PlayerDto> allPlayerDtos = allPlayersSet.stream() // Użycie kolekcji zawodników
				.map(player -> PlayerDto.builder() // Transformacja na PlayerDto
						.name(player.getName())
						.position(player.getPosition())
						.dateOfBirth(player.getDateOfBirth())
						.acceleration(player.getAcceleration())
						.shooting(player.getShooting())
						.passing(player.getPassing())
						.teamName(player.getTeam().getName()) // Wprowadzenie nazwy drużyny zamiast obiektu
						.build())
				.sorted() // Sortowanie: Domyślny porządek (Comparable na DTO, czyli po nazwie)
				.collect(Collectors.toList());

		allPlayerDtos.stream().forEach(dto ->
				System.out.println("  [DTO] " + dto.getName() + " (Drużyna: " + dto.getTeamName() + ", Przysp.: " + dto.getAcceleration() + ")")
		);

//
//		// =======================================================
//		// Krok 6: Serializacja (Zapis/Odczyt) (Wymagane)
//		// =======================================================
//		final String FILENAME = "teams_data.bin";
//
//		// Zapis do pliku binarnego
//		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
//			oos.writeObject(allTeams);
//			System.out.println("\n--- Krok 6: Zapisano " + allTeams.size() + " drużyny do pliku " + FILENAME + " ---");
//		}
//
//		// Odczyt z pliku binarnego
//		List<Team> teamsFromFile;
//		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
//			teamsFromFile = (List<Team>) ois.readObject();
//			System.out.println("--- Krok 6: Odczytano " + teamsFromFile.size() + " drużyny z pliku. ---");
//		}
//
//		// Wyświetlenie odczytanych danych
//		teamsFromFile.forEach(team -> {
//			System.out.println("\nODCZYTANA DRUŻYNA: " + team.getName() +
//					" (Atak: " + team.getAttack() +
//					", Liczba Zawodników: " + team.getPlayers().size() + ")");
//		});
//

//		// =======================================================
//		// Krok 7: Strumienie Równoległe i Pule Wątków (Wymagane)
//		// =======================================================
//		System.out.println("\n--- Krok 7: Strumienie Równoległe z własną pulą wątków ---");
//
//		// Wymagania Lab 1: Własna pula wątków (ForkJoinPool) o niestandardowym rozmiarze
//		ForkJoinPool customPool = new ForkJoinPool(2);
//
//		try {
//			// Wymagania Lab 1: Użycie puli do wykonania zadania na równoległym streamie
//			customPool.submit(() ->
//					allTeams.parallelStream().forEach(team -> {
//						System.out.println("  [WĄTEK: " + Thread.currentThread().getName() +
//								"] Przetwarzanie drużyny: " + team.getName());
//						team.getPlayers().forEach(player -> {
//							try {
//								// Wymagania Lab 1: Użycie Thread.sleep() do symulacji obciążenia
//								Thread.sleep(100);
//								System.out.println("    - " + player.getName() + " (Zakończono: " + team.getName() + ")");
//							} catch (InterruptedException e) {
//								Thread.currentThread().interrupt();
//							}
//						});
//					})
//			).get(); // Czekaj na zakończenie zadania
//
//		} finally {
//			// Wymagania Lab 1: Zamknięcie puli wątków
//			customPool.shutdown();
//			System.out.println("--- Pula wątków zamknięta. ---");
//		}
	}

}
