package com.softplayer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mongodb.MongoWriteException;
import com.softplayer.domain.User;
import com.softplayer.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User addUser(User user) throws MongoWriteException {
		user.setRegistration(LocalDateTime.now());
		return Optional.ofNullable(userRepository.save(user)).orElse(new User());
	}

	public User getUser(String cpf) {
		return Optional.ofNullable(userRepository.findByCpf(cpf)).orElse(new User());
	}
	
	public List<User> getUsers() {
		return Optional.ofNullable(userRepository.findAll()).orElse(Lists.newArrayList());
	}
	
	public User updateUser(User user) {
		User returnUser = userRepository.findByCpf(user.getCpf());
		if (returnUser != null) {
			user.set_id((returnUser.get_id()));
			user.setRegistration(returnUser.getRegistration());
			user.setUpdate(LocalDateTime.now());
			returnUser = Optional.ofNullable(userRepository.save(user)).orElse(new User());
		}
		return returnUser;
	}
	
	public void deleteUser(String cpf) {
		userRepository.deleteByCpf(cpf);
	}
}