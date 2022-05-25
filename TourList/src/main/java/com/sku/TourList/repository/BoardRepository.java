package com.sku.TourList.repository;

import com.sku.TourList.domain.Board;
import com.sku.TourList.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(Member user);
}
