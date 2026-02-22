package com.example.AIU;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data // @Getter, @Setter, @EqualsAndHashCode, @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Wymagane dla equals/hashCode tylko na ID
public class Player implements Comparable<Player>, Serializable {


    @EqualsAndHashCode.Include
    @Builder.Default
    private UUID id = UUID.randomUUID();


    private String name;
    private Position position;
    private LocalDate dateOfBirth;


    private int acceleration; // Przyspieszenie
    private int shooting;     // Strzały
    private int passing;      // Podania
    private int defense;    //defensywa
    private int dribble;


    @ToString.Exclude
    private Team team;

    @Override
    public int compareTo(Player other) {
        return this.name.compareTo(other.name);
    }
}
