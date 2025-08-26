package com.alex.caesars.logic;

import com.alex.caesars.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class AuthorizationService {
// write all url as a class var
    private final String caesarHost = "caesor.com";
    private final String caesarAuthPath = "/auth";

    private final RestTemplate restTemplate = new RestTemplate();


    public AuthResponseDTO callCaesarsAuthApi(AuthDTO authDTO) {
        String url = caesarHost + caesarAuthPath;
        log.debug("calling {}", url);
        // Call API and get response
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        // Return body as String (JSON in this case)
        if (response.getStatusCode() == HttpStatus.OK) {
            String output = response.getBody();
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(output);
            return authResponseDTO;
        }
        log.error("unable to call caesars: {}", response.getBody());

        //error - retry request (depend on error (if it's 400-nothing), (5XX - retry))
        return null;
    }

    /**
     * 1 player pushed spin
     * 2 im sending the request to caesars to get balance
     * 3 decide if the balance is enough
     * 4 if the balance is ok deduct the bet from the balance
     */
    public ResponseEntity handleRequest(AuthDTO authDTO) {
        AuthResponseDTO authResponseDTO = callCaesarsAuthApi(authDTO);
        if (Objects.isNull(authResponseDTO)) {
            return new ResponseEntity<>(new ErrorDTO("error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // error = suppose to be explanation what went wrong
        CaesarWalletDTO caesarWalletDTO = callCaesarsWalletStatus();
        if (Objects.isNull(caesarWalletDTO)) {
            return new ResponseEntity<>(new ErrorDTO("error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new PlayerInfoDTO(authResponseDTO.getName(), caesarWalletDTO.getBalance()), HttpStatus.OK);

    }

    private CaesarWalletDTO callCaesarsWalletStatus() {
        //get wallet status
        // check status from Caesars - if something went wrong, return null;
        return new CaesarWalletDTO();
    }


}
