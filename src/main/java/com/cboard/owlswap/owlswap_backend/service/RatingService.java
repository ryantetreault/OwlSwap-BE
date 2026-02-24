package com.cboard.owlswap.owlswap_backend.service;

import com.cboard.owlswap.owlswap_backend.dao.RatingDao;
import com.cboard.owlswap.owlswap_backend.dao.UserDao;
import com.cboard.owlswap.owlswap_backend.exception.BadRequestException;
import com.cboard.owlswap.owlswap_backend.exception.NotFoundException;
import com.cboard.owlswap.owlswap_backend.model.Rating;
import com.cboard.owlswap.owlswap_backend.model.User;
import com.cboard.owlswap.owlswap_backend.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingDao ratingDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    CurrentUser currentUser;

    @Transactional
    public void addRating(int userId, double score) {


        User user = userDao.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        int raterId = currentUser.userId();
        if(raterId == userId)
            throw new BadRequestException("Can not rate yourself");


        Rating rating = new Rating(user, score);
        ratingDao.save(rating);

        Double newAvg = calculateAverageRating(user.getUserId());
        user.setAverageRating(newAvg);

        userDao.save(user);
    }

    public List<Rating> getRatingsForUser(int userId) {
        return ratingDao.findByUserUserId(userId);
    }

    public Double calculateAverageRating(int userId) {
        List<Rating> ratings = ratingDao.findByUserUserId(userId);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double sum = ratings.stream().mapToDouble(Rating::getScore).sum();
        return sum / ratings.size();
    }
}