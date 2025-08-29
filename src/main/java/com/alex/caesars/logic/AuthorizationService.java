package com.alex.caesars.logic;

import com.alex.caesars.Messages;
import com.alex.caesars.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Objects;

@Service
@Slf4j
public class AuthorizationService {
// write all url as a class variable
    private final String caesarHost = "https://petstore.swagger.io/v2";
    private final String caesarAuthPath = "/pet/findByStatus?status=available";

    /*@Value("${caesar.host}")
    private String caesarHost;
    @Value("${caesar.auth.path}")
    private String caesarAuthPath;

try to add URLs to application properties
    */

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthorizationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    public AuthResponseDTO callCaesarsAuthApi(AuthDTO authDTO)throws Exception {
        String url = caesarHost + caesarAuthPath;
        log.debug("calling {}", url);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                //validate body - map output tp obj and validate fields
                String body = response.getBody();
                if (body == null || body.isEmpty()) {
                    throw new Exception("Empty response");
                }

                return objectMapper.readValue(body, new AuthResponseDTO.class);
            }
        throw new RuntimeException("Returned status is not 2XX"+response.getStatusCodeValue());
            //maybe create my own exceptions?
        }catch(HttpStatusCodeException e){
log.error("");
throw new Exception("Returned error:" + e.getStatusCode().value());
        }catch (ResourceAccessException e){
            throw new Exception("Couldn't reach server", e);
        }catch (JsonProcessingException e){
            throw new ParseException("Failed to parse the response", e);
        }catch (Exception e){
            throw new RuntimeException("Unknown error", e);
        }
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
            return new ResponseEntity<>(new ErrorDTO(Messages.CAESARS_ACCESS_ERROR,"Wrong info provided"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // error = suppose to be explanation what went wrong
        CaesarWalletDTO caesarWalletDTO = callCaesarsWalletStatus(authResponseDTO);
        if (Objects.isNull(caesarWalletDTO)) {
            return new ResponseEntity<>(new ErrorDTO(Messages.CAESARS_ACCESS_ERROR, "error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new PlayerInfoDTO(authResponseDTO.getName(), caesarWalletDTO.getBalance()), HttpStatus.OK);

    }

    private CaesarWalletDTO callCaesarsWalletStatus(AuthResponseDTO authResponseDTO) {
        authResponseDTO.getToken();
        // go outside with token and ask for a wallet
        //get wallet status
        //check status from Caesars - if something went wrong, return null;
        return new CaesarWalletDTO();
    } // write as call Caesar auth


}
