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
	
	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getBithdate() {
		return bithdate;
	}
	public void setBithdate(LocalDate bithdate) {
		this.bithdate = bithdate;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public LocalDateTime getRegistration() {
		return registration;
	}
	public void setRegistration(LocalDateTime registration) {
		this.registration = registration;
	}
	public LocalDateTime getUpdate() {
		return update;
	}
	public void setUpdate(LocalDateTime update) {
		this.update = update;
	}
}