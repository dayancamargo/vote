package com.test.meeting.service;

import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.user.User;
import com.test.meeting.model.user.UserRequest;
import com.test.meeting.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    private LocalDateTime MOCK_CREATED = LocalDateTime.now();
    private String MOCK_CPF = "00000000000";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        when(userRepository.saveAndFlush(fakeUser())).thenReturn(fakeUser());
        User result = userService.save(new UserRequest(MOCK_CPF));
        Assert.assertEquals(fakeUser(), result);
    }

    @Test(expected = ValidationException.class)
    public void testSaveConstraintFail() throws Exception {
        when(userRepository.saveAndFlush(fakeUser())).thenThrow(DataIntegrityViolationException.class);
        User result = userService.save(new UserRequest(MOCK_CPF));
        Assert.assertEquals(fakeUser(), result);
    }

    @Test
    public void testFindAll() throws Exception {
        when(userRepository.findAll((Pageable) null)).thenReturn(fakePage());
        Page<User> result = userService.findAll(null);
        Assert.assertEquals(fakePage(), result);
    }

    @Test
    public void testFindByCpf() throws Exception {
        when(userRepository.findByCpf(MOCK_CPF)).thenReturn(Optional.of(fakeUser()));

        User result = userService.findByCpf(MOCK_CPF);
        Assert.assertEquals(fakeUser(), result);
    }

    @Test(expected = NoDataFoundException.class)
    public void testFindByCpfNotFound() throws Exception {
        when(userRepository.findByCpf(MOCK_CPF)).thenReturn(Optional.empty());

        User result = userService.findByCpf(MOCK_CPF);
    }

    private User fakeUser(){
        User user = new User();
        user.setCpf(MOCK_CPF);
        user.setCreatedAt(MOCK_CREATED);
        return user;
    }

    private List<User> fakeList(){
        return Arrays.asList(fakeUser(), fakeUser(), fakeUser());
    }

    private Page<User> fakePage(){
        return new PageImpl<>(fakeList());
    }
}

