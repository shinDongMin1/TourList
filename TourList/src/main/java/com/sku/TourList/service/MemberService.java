package com.sku.TourList.service;

import com.sku.TourList.domain.Member;

import javax.servlet.http.HttpSession;

public interface MemberService {
    public Member JoinCheck(Member member);
    public Member LoginCheck(Member member, HttpSession session);
    public Member Join(Member member);
    public void logout(HttpSession session);
    public Member findUser(HttpSession session);
    //public void deleteById(String id); 탈퇴
}
