package com.softplayer.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softplayer.domain.ReturnObject;
import com.softplayer.domain.User;
import com.softplayer.service.UserService;
import com.softplayer.util.Validator;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReturnObject ro;
	
	@ApiOperation(value = "Adiciona um novo usuário na base de dados.", 
			notes = "Adiciona um novo usuário na base de dados.",
			authorizations = {@Authorization("baererAuth")})
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Usuário adicionado com sucesso."),
    		@ApiResponse(code = 409, message = "Usuário já cadastrado na base de dados"),
    		@ApiResponse(code = 500, message = "Erro ao adicionar usuário na base de dados.")})
	@PostMapping("/addUser")
	private ReturnObject addUser(@RequestBody User user) {
		boolean hasError = false;
		if (!Validator.isValidEmail(user.getEmail())) {
			ro.setReturnObjectError(HttpStatus.BAD_REQUEST.value(), "Email invalid!");
			hasError = true;
		}
		if (!Validator.isValidCpf(user.getCpf())) {
			ro.setReturnObjectError(HttpStatus.BAD_REQUEST.value(), "CPF invalid!");
			hasError = true;
		}
		
		try {
			if (!hasError) {
				ro.setReturnObjectOk(userService.addUser(user));
			}
		} catch (DuplicateKeyException e) {
			log.error(e.getMessage());
			ro.setReturnObjectError(HttpStatus.CONFLICT.value(), 
					"User with CPF "+ user.getCpf() +" already registered!");
		} catch (Exception e) {
			log.error(e.getMessage());
			ro.setReturnObjectError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		return ro;
	}
	
	@ApiOperation(value = "Retorna um usuário existente na base de dados através do CPF.", 
			notes = "Retorna um usuário existente na base de dados através do CPF.", 
			authorizations = {@Authorization("baererAuth")})
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Usuário retornado com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao retornar um usuário da base de dados.")})
	@GetMapping("/getUser/{cpf}")
	private ReturnObject getUser(@PathVariable(value="cpf", required=true) String cpf) {
		if (Validator.isValidCpf(cpf)) {
			ro.setReturnObjectOk(userService.getUser(cpf));
		} else {
			ro.setReturnObjectError(HttpStatus.BAD_REQUEST.value(), "CPF invalid!");
		}
		return ro;
	}
	
	@ApiOperation(value = "Retorna todos os usuários existentes na base de dados.", 
			notes = "Retorna todos os usuários existentes na base de dados", 
			authorizations = {@Authorization("baererAuth")})
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Consulta realizada com sucesso."),
    		@ApiResponse(code = 204, message = "Não existem usuários cadastrados na base de dados."),
    		@ApiResponse(code = 500, message = "Erro ao buscar usuários na base de dados.")})
	@GetMapping("/getUsers")
	private ReturnObject getUsers() {
		try {
			ro.setReturnObjectOk(userService.getUsers());
		} catch (Exception e) {
			log.error(e.getMessage());
			ro.setReturnObjectError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		return ro;
	}
	
	@ApiOperation(value = "Altera um usuário existente na base de dados.", 
			notes = "Altera um usuário existente na base de dados.", 
			authorizations = {@Authorization("baererAuth")})
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Alteração realizada com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao alterar usuário na base de dados.")})
	@PutMapping("/updateUser")
	private ReturnObject updateUser(@RequestBody User user) {
		try {
			ro.setReturnObjectOk(userService.updateUser(user));
		} catch (Exception e) {
			log.error(e.getMessage());
			ro.setReturnObjectError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		return ro;
	}
	
	@ApiOperation(value = "Exclui um usuário existente na base de dados através do CPF.", 
			notes = "Exclui um usuário existente na base de dados através do CPF.", 
			authorizations = {@Authorization("baererAuth")})
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Usuário excluido com sucesso."),
    		@ApiResponse(code = 500, message = "Erro ao excluir usuário na base de dados.")})
	@DeleteMapping("/deleteUser/{cpf}")
	private void deleteUser(@PathVariable(value="cpf", required=true) String cpf) {
		try {
			userService.deleteUser(cpf);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}