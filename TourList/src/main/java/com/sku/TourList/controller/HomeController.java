package com.sku.TourList.controller;

import com.sku.TourList.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {
    final MemberService memberService;

    @GetMapping("")
    public ModelAndView Home(HttpSession session, ModelAndView mav){
        if(session.getAttribute ("name") != null){
            memberService.logout (session);
        }
        mav.setViewName("tourlist"); // 뷰의 이름
        return mav;
    }

    @GetMapping("tourlist")
    public ModelAndView TourList(HttpSession session, ModelAndView mav){
        mav.addObject("login_message", session.getAttribute ("name"));
        mav.setViewName("tourlist"); // 뷰의 이름
        return mav;
    }
}