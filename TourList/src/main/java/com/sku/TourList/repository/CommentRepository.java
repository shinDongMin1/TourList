package com.sku.TourList.repository;

import com.sku.TourList.domain.Comment;
import com.sku.TourList.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByUser(Member user);
    ArrayList<Comment> findAllByBoard_IdOrderByModifiedDateDesc(Long id);
}
