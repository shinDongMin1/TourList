package com.sku.TourList.controller;

import com.sku.TourList.domain.Board;
import com.sku.TourList.domain.Member;
import com.sku.TourList.service.BoardService;
import com.sku.TourList.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/tourlist/*")
@RequiredArgsConstructor
public class TourListController {

    //private final ApiService apiService;
    private final BoardService boardService;
    private final MemberService memberService;
    private String board_message = "";
    private String DateSearch = "00000000";


 /*   @GetMapping("statistics")
    public ModelAndView statistics(HttpSession session, ModelAndView mav) {

        if(session.getAttribute ("date") != null){
            DateSearch = session.getAttribute ("date").toString ();
        }
        mav.setViewName("corona/statistics"); // 뷰의 이름
        mav.addObject("result",  apiService.CallCoronaApi (DateSearch, DateSearch));
        mav.addObject("login_message", session.getAttribute ("name"));
        session.setAttribute ("date", null);
        DateSearch = "00000000";
        return mav;
    }*/

    @RequestMapping(value = "date", method = RequestMethod.GET)
    public ModelAndView putDate(@RequestParam String date, HttpSession session, ModelAndView mav)  {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat ("yyyyMMdd");

        Date day = null;
        try {
            day = simpleDateFormat1.parse (date);
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        session.setAttribute ("date", simpleDateFormat2.format (day));
        mav.setViewName("redirect:statistics");
        return mav;
    }

    @GetMapping("find")
    public ModelAndView find(HttpSession session, ModelAndView mav){
        mav.setViewName("tourlist/find"); // 뷰의 이름
        mav.addObject("login_message", session.getAttribute ("name"));
        return mav;
    }

    @GetMapping("board")
    public ModelAndView board(@PageableDefault Pageable pageable, HttpSession session, ModelAndView mav){
        mav.setViewName("tourlist/board"); // 뷰의 이름
        mav.addObject ("boardList", boardService.findBoardList(pageable));
        mav.addObject("login_message", session.getAttribute ("name"));
        mav.addObject("board_message", board_message);
        board_message = "";
        return mav;
    }

    @GetMapping({"form", "form/"})
    public ModelAndView form(@RequestParam(value = "id", defaultValue = "0") Long id, HttpSession session, ModelAndView mav){
        mav.setViewName("tourlist/form"); // 뷰의 이름
        mav.addObject ("board", boardService.findBoardById (id));
        mav.addObject("login_message", session.getAttribute ("name"));
        return mav;
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    public ModelAndView postBoard(@ModelAttribute Board board, HttpSession session, ModelAndView mav) {
        Member user = memberService.findUser (session);
        if(user != null) {
            board.setUser (user);
            boardService.save (board);
            board_message = "save";
        }
        else {
            board_message = "fail";
        }
        mav.setViewName("redirect:board"); // 뷰의 이름
        return mav;
        //return new ResponseEntity<> ("{}", HttpStatus.CREATED);
    }

    @RequestMapping(value = "put", method = RequestMethod.POST)
    public ModelAndView putBoard(@ModelAttribute Board board, HttpSession session, ModelAndView mav) {
        Member user = memberService.findUser (session);
        Board findBoard = boardService.findBoardById (board.getId ());

        if(user != null) {
            if(findBoard.getUser () == user) {
                Board persistBoard = boardService.findBoardById (board.getId ());
                persistBoard.update (board);
                boardService.save (persistBoard); // save = insert + update
                board_message = "update";
            }
            else {
                board_message = "user";
            }
        }
        else {
            board_message = "fail";
        }
        mav.setViewName("redirect:board");
        return mav;
        //return new ResponseEntity<> ("{}", HttpStatus.OK);
    }

    //ajax
    //@ResponseBody
    //@PostMapping
    //@PutMapping
    //@DeleteMapping
    //ResponseEntity<?>
    //@PathVariable = board/{id}/name/
    //@RequestParam = board+?id=&name=
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ModelAndView deleteBoard(@ModelAttribute Board board, HttpSession session, ModelAndView mav) {
        Member user = memberService.findUser (session);
        Board findBoard = boardService.findBoardById (board.getId ());

        if(user != null) {
            if(findBoard.getUser () == user) {
                boardService.deleteById (board.getId ());
                board_message = "delete";
            }
            else{
                board_message = "user";
            }
        }
        else {
            board_message = "fail";
        }
        mav.setViewName("redirect:board");
        return mav;
        //return new ResponseEntity<> ("{}", HttpStatus.OK);
    }
}
