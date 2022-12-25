package com.miranda.voting.assemblies.v1.controller;

import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "AssembliesController", tags = { "Assemblies Controller" })
@Valid
@RequestMapping("/assemblies")
@RestController
public class AssembliesController {
}
