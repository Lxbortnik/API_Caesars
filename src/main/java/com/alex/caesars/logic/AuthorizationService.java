package com.alex.caesars.logic;

import com.alex.caesars.CaesarsClientProperties;
import com.alex.caesars.Messages;
import com.alex.caesars.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {



    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CaesarsClientProperties props;


    public AuthResponseDTO callCaesarsAuthApi(AuthDTO authDTO)throws Exception {
        String url = props.getHost() + props.getPaths().getAuth();
        log.debug("calling {}", url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                //validate body - map output to obj and validate fields
                String body = response.getBody();
                if (body == null || body.isEmpty()) {
                    throw new RuntimeException("Empty response");
                }

                return objectMapper.readValue(body, AuthResponseDTO.class);
            } else {
                throw new RuntimeException("Received no.OK response: " + response.getStatusCode());
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to get response",e);
        }

        //Todo add catch with other exceptions

  /*      throw new RuntimeException("Returned status is not 2XX"+response.getStatusCodeValue());
            //maybe create my own exceptions?
        }catch(HttpStatusCodeException e){
            throw new Exception("Returned error:" + e.getStatusCode().value());
        }catch (ResourceAccessException e){
            throw new Exception("Couldn't reach server", e);
        }catch (JsonProcessingException e){
            throw new ParseException("Failed to parse the response", e);
        }catch (Exception e){
            throw new RuntimeException("Unknown error", e);
  }
 */

        }

    /**
     * Example flow:
     * 1. Player sends request
     * 2. We call Caesars auth
     * 3. If auth ok, we fetch wallet
     * 4. Return player info + balance
     */

    public ResponseEntity<?> handleRequest(AuthDTO authDTO) throws Exception {

        AuthResponseDTO authResponseDTO = callCaesarsAuthApi(authDTO);
        if (Objects.isNull(authResponseDTO)) {
            return new ResponseEntity<>(new ErrorDTO(Messages.CAESARS_ACCESS_ERROR,"Wrong info provided"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CaesarWalletDTO caesarWalletDTO = callCaesarsWalletStatus(authResponseDTO);
        if (Objects.isNull(caesarWalletDTO)) {
            return new ResponseEntity<>(new ErrorDTO(Messages.CAESARS_ACCESS_ERROR, "Wallet not correct"), HttpStatus.INTERNAL_SERVER_ERROR);
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




    /**
     * 1 player pushed spin
     * 2 im sending the request to caesars to get balance
     * 3 decide if the balance is enough
     * 4 if the balance is ok deduct the bet from the balance
     */

}
