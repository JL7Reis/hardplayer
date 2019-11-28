package com.softplayer.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.softplayer.domain.enums.Genre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document("user")
public class User {
	
	@Id
	@ApiModelProperty(hidden = true)
	private ObjectId _id;
	
	@ApiModelProperty(example = "João Luis")
	@NotBlank(message = "Name required.")
	@NotNull(message = "Name required.")
	@NotEmpty(message = "Name required.")
	private String username;
	
	@ApiModelProperty(example = "05758628743")
	@NotBlank(message = "CPF required.")
	@NotNull(message = "CPF required.")
	@NotEmpty(message = "CPF required.")
	@Indexed(unique=true, sparse=true)
	@Size(min=11, max=11)
	private String cpf;
	
	@ApiModelProperty(example = "jl.rmp7@gmail.com")
	private String email;
	
	@ApiModelProperty(dataType = "yyyy-MM-dd", example = "1985-09-29")
	@NotBlank(message = "Birthdate required.")
	@NotNull(message = "Birthdate required.")
	@NotEmpty(message = "Birthdate required.")
	private LocalDate birthdate;
	
	@ApiModelProperty(allowableValues = "Feminino, Masculino, Outro", example = "Male")
	private Genre genre;
	
	@ApiModelProperty(example = "Rio de Janeiro")
	private String birthplace;
	
	@ApiModelProperty(example = "Brasil")
	private String country;
	
	@ApiModelProperty(hidden = true)
	@CreatedDate
	private LocalDateTime registration;
	
	@ApiModelProperty(hidden = true)
	@LastModifiedDate
	private LocalDateTime update;
	
}