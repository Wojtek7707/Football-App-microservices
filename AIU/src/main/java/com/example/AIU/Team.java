package com.example.AIU;

import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team implements Comparable<Team>, Serializable {

    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    private String name;
    private String city;

    private int attack;
    private int midfield;
    private int defense;

    // Relacja 1:N do Player.
    // toString nie używamy całej listy
    @ToString.Exclude
    @Builder.Default
    private List<Player> players = new ArrayList<>();

    // Metoda pomocnicza do ustawienia dwukierunkowej relacji
    public void addPlayer(Player player) {
        players.add(player);
        player.setTeam(this);
    }

    // Implementacja Comparable (naturalny porządek)
    @Override
    public int compareTo(Team other) {
        return this.name.compareTo(other.name);
    }
}