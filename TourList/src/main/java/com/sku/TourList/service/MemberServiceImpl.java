package com.sku.TourList.service;

import com.sku.TourList.domain.Member;
import com.sku.TourList.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    // 중복 아이디가 있나 = 아이디
    @Override
    public Member JoinCheck(Member member) {
        return memberRepository.findByUserid (member.getUserid ());
    }

    // 가입이 되어 있나 = 아이디랑 패스워드
    @Override
    public Member LoginCheck(Member member, HttpSession session) {
        Member user = memberRepository.findByUseridAndPassword(member.getUserid (), member.getPassword ());

        if (user != null) { // 세션 변수 저장
            session.setAttribute("userid", user.getUserid());
            session.setAttribute("name", user.getName ());
        }
        return user;
    }

    // 가입
    @Override
    public Member Join(Member member) {
/*        System.out.println (user.getUserid ());
        System.out.println (user.getPassword ());
        System.out.println (user.getName ());
        System.out.println (user.getEmail ());*/
        Member savedUser = memberRepository.save (member);
        return savedUser;
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
    }

    @Override
    public Member findUser(HttpSession session) {
        return memberRepository.findByUserid ((String) session.getAttribute ("userid"));
    }
}
