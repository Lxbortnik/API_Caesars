package com.alex.caesars;

import com.alex.caesars.dto.AuthDTO;
import com.alex.caesars.dto.AuthResponseDTO;
import com.alex.caesars.dto.CaesarWalletDTO;
import org.springframework.web.client.RestTemplate;

public class CaesarsClient {
    private final RestTemplate restTemplate;
    private final CaesarsClientProperties props;

    public AuthResponseDTO auth(AuthDTO authDTO) {
        String url = props.getHost() + props.getPaths().getAuth(); // при необходимости добавь query/body
        try {
            // если GET без тела:
            return restTemplate.getForObject(url, AuthResponseDTO.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new ExternalServiceException("Caesars returned error", e.getStatusCode().value(), e);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            throw new ExternalServiceException("Caesars unreachable", 0, e);
        } catch (Exception e) { // редкие кейсы маппинга и пр.
            throw new ParsingException("Failed to parse Caesars auth", e);
        }
    }

    public CaesarWalletDTO wallet(String token) {
        String url = props.getHost() + props.getPaths().getWallet();
        // TODO: добавить заголовок Authorization/Query param с token
        try {
            return restTemplate.getForObject(url, CaesarWalletDTO.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new ExternalServiceException("Caesars returned error", e.getStatusCode().value(), e);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            throw new ExternalServiceException("Caesars unreachable", 0, e);
        } catch (Exception e) {
            throw new ParsingException("Failed to parse Caesars wallet", e);
        }
    }
}
}
