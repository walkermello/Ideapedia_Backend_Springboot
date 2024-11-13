package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.dto.validasi.ValLoginDTO;
import com.tugasakhir.ideapedia.service.AppUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AppUserDetailService appUserDetailService;

    @PostMapping("/login")
    public ResponseEntity<Object> doLogin(@Valid @RequestBody
                                              ValLoginDTO valLoginDTO
            , HttpServletRequest request){
        return appUserDetailService.doLogin(appUserDetailService.convertToEntiy(valLoginDTO), request);
    }
}
