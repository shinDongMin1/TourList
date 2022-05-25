package com.sku.TourList.controller;

import com.sku.TourList.domain.Member;
import com.sku.TourList.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {

    final MemberService memberService;
    private String Join_message = "";
    private String login_message = "";

    @RequestMapping("Join")
    public ModelAndView Join(ModelAndView mav) {
        mav.setViewName("member/Join");
        mav.addObject("Join_message", Join_message);
        return mav;
    }

    @RequestMapping("Join_check")
    public ModelAndView Join_check(@ModelAttribute Member member, ModelAndView mav) {
        //String id = memberService.JoinCheck(dto);
        Member id = memberService.JoinCheck(member);

        if (id != null) { // 가입 실패 시
            mav.setViewName("redirect:Join"); // 뷰의 이름
            Join_message = "fail";
        } else { // 가입 성공 시
            memberService.Join (member);//인서트
            mav.setViewName("redirect:login");
            Join_message = "success";
        }
        login_message = "";

        return mav;
    }

    @GetMapping("login")
    public ModelAndView login(ModelAndView mav) {
        mav.setViewName("member/login");
        mav.addObject("Join_message", Join_message);
        mav.addObject("login_message", login_message);
        return mav;
    }

    @RequestMapping("login_check")
    public ModelAndView login_check(@ModelAttribute Member member, HttpSession session, ModelAndView mav) {
        //String name = memberService.LoginCheck(dto, session);
        Member name = memberService.LoginCheck(member, session);

        if (name != null) { // 로그인 성공 시
            mav.setViewName("redirect:/tourlist"); // 뷰의 이름
            Join_message = "";
            login_message = "";
        } else { // 로그인 실패 시
            mav.setViewName("redirect:login");
            Join_message = "";
            login_message = "error";
        }
        return mav;
    }

    @RequestMapping("logout")
    public ModelAndView logout(HttpSession session, ModelAndView mav) {
        Join_message = "";
        login_message = "";
        memberService.logout (session);
        mav.setViewName("tourlist");
        //mav.addObject("logout_message", null);
        return mav;
    }
}
