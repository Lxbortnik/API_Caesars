package com.alex.caesars;

import com.alex.caesars.dto.AuthDTO;
import com.alex.caesars.dto.AuthResponseDTO;
import com.alex.caesars.logic.AuthorizationService;
import com.alex.caesars.CaesarClientProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AuthorizationServiceTest {


    @Test
    void auth_ok() throws Exception {
        // 1. Create RestTemplate + Mock server
        RestTemplate rt = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(rt);

        // 2. Setup fake Caesars API response
        mockServer.expect(requestTo("http://fake-caesars.com/auth"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"token\":\"abc123\",\"sessionId\":\"s1\"}",
                        MediaType.APPLICATION_JSON
                ));

        // 3. Prepare props with fake host + path
        CaesarClientProperties props = new CaesarClientProperties();
        props.setHost("http://fake-caesars.com");
        CaesarClientProperties.Paths paths = new CaesarClientProperties.Paths();
        paths.setAuth("/auth");
        props.setPaths(paths);

        // 4. Create service with mock dependencies
        ObjectMapper om = new ObjectMapper();
        AuthorizationService svc = new AuthorizationService(rt, om, props);

        // 5. Call service
        AuthResponseDTO out = svc.callCaesarsAuthApi(new AuthDTO());

        // 6. Verify result
        assertThat(out).isNotNull();
        assertThat(out.getToken()).isEqualTo("abc123");
        assertThat(out.getSessionId()).isEqualTo("s1");

        // 7. Verify mock interactions
        mockServer.verify();
    }
}
