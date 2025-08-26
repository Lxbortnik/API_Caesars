package com.alex.caesars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private int refPlayerId;
    private String name;
    private String gameId;

    public AuthResponseDTO(String body) {
    }
}
