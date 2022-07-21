package com.sku.TourList.service;

import com.sku.TourList.domain.Board;
import com.sku.TourList.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface CommentService {
    public ArrayList<Comment> findCommentList(Long id);
    public Comment findCommentById(Long id);
    public Comment save(Comment comment);
    public void deleteById(Long id);
}
