package com.softplayer.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Document("user")
public class User {
	
	@Id
	private ObjectId _id;
	
	@NotBlank(message = "Name required.")
	@NotNull(message = "Name required.")
	@NotEmpty(message = "Name required.")
	private String username;
	
	@NotBlank(message = "CPF required.")
	@NotNull(message = "CPF required.")
	@NotEmpty(message = "CPF required.")
	@Indexed(unique=true, sparse=true)
	@Size(min=11, max=11)
	private String cpf;
	private String email;
	
	@NotBlank(message = "Birthdate required.")
	@NotNull(message = "Birthdate required.")
	@NotEmpty(message = "Birthdate required.")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate bithdate;
	private String genre;
	private String birthplace;
	private String nationality;
	
	private LocalDateTime registration;
	private LocalDateTime update;
	
}