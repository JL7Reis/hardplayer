package com.softplayer.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softplayer.domain.LoginPayload;
import com.softplayer.domain.ReturnObject;
import com.softplayer.service.TokenAuthenticationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class WebController {
	
	@Autowired
	private TokenAuthenticationService tokenService;
	
	@ApiOperation(value = "Retorna um json web token para usuário autorizado.", 
			notes = "Retorna um json web token para usuário autorizado.")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Token gerado com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao gerar token gerado com sucesso.")})
	@PostMapping("/login")
	private ReturnObject login(@RequestBody LoginPayload LoginPayload) {
		return tokenService.setLogin(LoginPayload);
	}
	
	@ApiOperation(value = "Retorna um json web token para usuário autorizado.", 
			notes = "Retorna um json web token para usuário autorizado.")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Token gerado com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao gerar token gerado com sucesso.")})
	@PostMapping("/token/generate-token")
	private ReturnObject getToken(@RequestBody LoginPayload LoginPayload) {
		return tokenService.setLogin(LoginPayload);
	}
	
	@ApiOperation(value = "Efetua logout do sistema.", 
			notes = "Efetua logout do sistema.",
			authorizations = {@Authorization("baererAuth")})
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Logout efetuado com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao efetuar o logout.")})
	@GetMapping("/logout")
	public void logout(){
		try {
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@ApiOperation(value = "Baixa do código do programa.", 
			notes = "Baixa do código do programa.")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Download do arquivo efetuado com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao efetuar fazer download do arquivo.")})
	@GetMapping("/source")
	private String getSource() throws Exception {
		return "window.location.href = https://github.com/JL7Reis/softplayer/archive/master.zip";
	}
}
