package com.miranda.cpf.validate.v1.controller;

import com.miranda.cpf.validate.v1.service.ValidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/validate")
@RestController
public class ValidateController {

    private final ValidateService validateService;

    public ValidateController(ValidateService validateService) {
        this.validateService = validateService;
    }

    @GetMapping(value = "/cpfCnpj/{cpfCnpj}")
    public ResponseEntity<Boolean> isValid(@PathVariable String cpfCnpj){
        return ResponseEntity.ok().body(validateService.isValid(cpfCnpj));
    }
}
