package com.sku.TourList.controller;

import com.sku.TourList.domain.Member;
import com.sku.TourList.domain.Tour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/journal/*")
@RequiredArgsConstructor
public class JournalController {

    @RequestMapping("journal")
    public ModelAndView journal(@ModelAttribute Member member, HttpSession session, ModelAndView mav) {
        mav.setViewName("journal/journal");
        List<String> photo = new ArrayList<> ();
        photo.add("1");photo.add("2");photo.add("3");photo.add("4");photo.add("5");
        photo.add("6");photo.add("7");photo.add("8");photo.add("9");photo.add("10");
        mav.addObject("photo", photo);
        mav.addObject("login_message", session.getAttribute ("name"));
        return mav;
    }
}
