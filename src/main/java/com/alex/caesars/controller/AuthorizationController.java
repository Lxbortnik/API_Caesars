package com.alex.caesars.controller;

import com.alex.caesars.dto.AuthDTO;
import com.alex.caesars.logic.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthorizationController {
    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/launch")
    public ResponseEntity launch(@RequestBody AuthDTO authDTO) {
        return authorizationService.handleRequest(authDTO);

    }

}



//