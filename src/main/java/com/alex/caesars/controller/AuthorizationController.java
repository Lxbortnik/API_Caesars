package com.alex.caesars.controller;

import com.alex.caesars.Messages;
import com.alex.caesars.dto.AuthDTO;
import com.alex.caesars.dto.AuthResponseDTO;
import com.alex.caesars.dto.ErrorDTO;
import com.alex.caesars.logic.AuthorizationService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/")
public class AuthorizationController {
    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/launch")
    public ResponseEntity launch(@RequestBody AuthDTO authDTO)throws  Exception {
        try {
            AuthResponseDTO result = authorizationService.callCaesarsAuthApi(authDTO);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            ErrorDTO error = new ErrorDTO(Messages.INTERNAL_ERROR, "Unexpected server error");
            return ResponseEntity.status(500).body(error);

        }
   //     return authorizationService.handleRequest(authDTO);

    }

}



//