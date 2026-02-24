package com.cboard.owlswap.owlswap_backend.service;

import com.cboard.owlswap.owlswap_backend.dao.UserDao;
import com.cboard.owlswap.owlswap_backend.exception.DtoMappingException;
import com.cboard.owlswap.owlswap_backend.exception.NotFoundException;
import com.cboard.owlswap.owlswap_backend.model.Dto.UserDto;
import com.cboard.owlswap.owlswap_backend.model.DtoMapping.UserMapper;
import com.cboard.owlswap.owlswap_backend.model.User;
import com.cboard.owlswap.owlswap_backend.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService
{
    @Autowired
    UserDao dao;
    @Autowired
    RatingService ratingService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CurrentUser currentUser;




    public List<UserDto> getAllUsers()
    {
        List<User> users = dao.findAll();
        return users
                .stream()
                .map(user -> {
                            try {
                                return userMapper.userToDto(user);
                            } catch (Exception e) {
                                throw new DtoMappingException("Failed to user Item to DTO. userId=" + user.getUserId(), e);
                            }
                        }
                )
                .toList();
    }

    public UserDto getUserById(int userId)
    {
        User user = dao.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: "));

        Double avgRating = ratingService.calculateAverageRating(user.getUserId());
        user.setAverageRating(avgRating);

        try {
            return userMapper.userToDto(user);
        } catch (Exception e) {
            throw new DtoMappingException("Failed to user Item to DTO. userId=" + user.getUserId(), e);
        }
    }

    public void deleteUser(int userId)
    {
        User user = dao.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: "));

        dao.delete(user);

    }


    public UserDto getProfile() {

        User user = currentUser.user();

        Double avgRating = ratingService.calculateAverageRating(user.getUserId());
        user.setAverageRating(avgRating);

        return userMapper.userToDto(user);

    }
}
