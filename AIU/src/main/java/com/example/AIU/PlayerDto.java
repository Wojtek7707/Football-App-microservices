package com.example.AIU;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto implements Comparable<PlayerDto>, Serializable {

    private String name;
    private Position position;
    private LocalDate dateOfBirth;


    private int acceleration;
    private int shooting;
    private int passing;
    private int defense;
    private int dribble;


    private String teamName;

    // Comparable (naturalny porządek)
    @Override
    public int compareTo(PlayerDto other) {
        return this.name.compareTo(other.name);
    }
}