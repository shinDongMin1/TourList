package com.sku.TourList.repository;

import com.sku.TourList.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserid(String userid);
    Member findByUseridAndPassword(String userid, String password);
}
