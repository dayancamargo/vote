package com.test.meeting.service;

import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.user.User;
import com.test.meeting.model.user.UserRequest;
import com.test.meeting.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserRequest request) {

        if(Objects.isNull(request)){
            throw  new ValidationException("UserRequest could not be null.");
        }

        User user = new User();
        user.setCpf(request.getCpf());
        log.debug("saving: {}", user);

        try {
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            throw new ValidationException("Cpf already inserted.");
        }
    }

    public Page<User> findAll(Pageable page) {
        log.debug("Find with: {}", page);
        return userRepository.findAll(page);
    }

    public User findByCpf(String cpf) {
        log.debug("Find with: {}", cpf);
        return userRepository.findByCpf(cpf)
                             .orElseThrow(() -> new NoDataFoundException("Cannot found any user with this cpf: " + cpf));
    }
}
