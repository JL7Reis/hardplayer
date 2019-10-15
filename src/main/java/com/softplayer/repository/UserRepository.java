package com.softplayer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.softplayer.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByCpf(String cpf);
	void deleteByCpf(String cpf);
}