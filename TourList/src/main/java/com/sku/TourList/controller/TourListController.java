package com.sku.TourList.controller;

import com.sku.TourList.domain.*;
import com.sku.TourList.service.ApiService;
import com.sku.TourList.service.BoardService;
import com.sku.TourList.service.CommentService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tourlist/*")
@RequiredArgsConstructor
public class TourListController {

    private final ApiService apiService;
    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;
    private String board_message = "";
    private String comment_message = "";


    @GetMapping("region")
    public ModelAndView region(HttpSession session, ModelAndView mav) {
        mav.setViewName("tourlist/region"); // 뷰의 이름
        List<Region> region = new ArrayList<>();
        mav.addObject("region", region);

        mav.addObject("login_message", session.getAttribute ("name"));
        return mav;
    }

    @GetMapping({"attractions", "attractions/"})
    public ModelAndView attractions(@RequestParam(value = "areaCode", defaultValue = "0") String areaCode, @RequestParam(value = "sigunguCode", defaultValue = "0") String sigunguCode, HttpSession session, ModelAndView mav) {
        mav.setViewName("tourlist/attractions"); // 뷰의 이름
        List<Tour> result = apiService.CallApi (areaCode, sigunguCode);

        mav.addObject("result",  result);
        mav.addObject("login_message", session.getAttribute ("name"));
        return mav;
    }

    @GetMapping({"view", "view/"})
    public ModelAndView viewItem(@RequestParam(value = "contentId", defaultValue = "0") String contentId, HttpSession session, ModelAndView mav) {
        mav.setViewName("tourlist/view"); // 뷰의 이름
        Tour result = apiService.CallDetail (contentId);

        mav.addObject ("result", result);
        mav.addObject("login_message", session.getAttribute ("name"));
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

    @RequestMapping(value = "saveBoard", method = RequestMethod.POST)
    public ModelAndView saveBoard(@ModelAttribute Board board, HttpSession session, ModelAndView mav) {
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

    @RequestMapping(value = "updateBoard", method = RequestMethod.POST)
    public ModelAndView updateBoard(@ModelAttribute Board board, HttpSession session, ModelAndView mav) {
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
    @RequestMapping(value = "deleteBoard", method = RequestMethod.POST)
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

    @GetMapping({"form", "form/"})
    public ModelAndView form(@RequestParam(value = "id", defaultValue = "0") Long id, HttpSession session, ModelAndView mav){
        mav.setViewName("tourlist/form"); // 뷰의 이름
        mav.addObject ("board", boardService.findBoardById (id));
        mav.addObject("login_message", session.getAttribute ("name"));
        return mav;
    }

    @GetMapping({"read", "read/"})
    public ModelAndView read(@RequestParam(value = "id", defaultValue = "0") Long id, HttpSession session, ModelAndView mav){
        mav.setViewName("tourlist/read"); // 뷰의 이름
        ArrayList<Comment> commentList = new ArrayList<> ();
        commentList = commentService.findCommentList (id);
        mav.addObject ("board", boardService.findBoardById (id));
        mav.addObject ("commentList", commentList);
        mav.addObject("login_message", session.getAttribute ("name"));
        mav.addObject("userid", session.getAttribute ("userid"));
        mav.addObject("comment_message", comment_message);
        comment_message = "";
        return mav;
    }

    @RequestMapping(value = "saveComment", method = RequestMethod.POST)
    public ModelAndView saveComment(@ModelAttribute Comment comment, @RequestParam(value = "id2", defaultValue = "0") Long id, HttpSession session, ModelAndView mav) {
        Member user = memberService.findUser (session);
        Board board = boardService.findBoardById (id);
        if(user != null) {
            comment.setUser (user);
            comment.setBoard (board);
            commentService.save (comment);
            comment_message = "save";
        }
        else {
            comment_message = "fail";
        }
        mav.setViewName("redirect:read?id=" + id); // 뷰의 이름
        return mav;
    }

    @GetMapping({"modify", "modify/"})
    public ModelAndView modify(@RequestParam Long boardID, @RequestParam Long commentID, HttpSession session, ModelAndView mav) {
        Comment findComment = commentService.findCommentById (commentID);

        mav.addObject("boardID", boardID);
        mav.addObject("comment", findComment);
        mav.addObject("login_message", session.getAttribute ("name"));
        mav.addObject("userid", session.getAttribute ("userid"));

        mav.setViewName("tourlist/modify"); // 뷰의 이름
        return mav;
    }

    @RequestMapping("updateComment")
    public ModelAndView updateComment(@RequestParam Long boardID, @RequestParam Long commentID, @ModelAttribute Comment comment, HttpSession session, ModelAndView mav) {
        Member user = memberService.findUser (session);
        Comment findComment = commentService.findCommentById (commentID);

        if(user != null) {
            if(findComment.getUser () == user) {
                Comment persistComment = commentService.findCommentById (commentID);
                persistComment.update (comment);
                commentService.save (persistComment); // save = insert + update
                comment_message = "update";
            }
            else {
                comment_message = "fail";
            }
        }
        mav.setViewName("redirect:read?id=" + boardID); // 뷰의 이름
        return mav;
        //return new ResponseEntity<> ("{}", HttpStatus.OK);
    }

    @GetMapping("deleteComment")
    public ModelAndView deleteComment(@RequestParam Long boardID, @RequestParam Long commentID, HttpSession session, ModelAndView mav) {
        Member user = memberService.findUser (session);
        Comment findComment = commentService.findCommentById (commentID);

        if(user != null) {
            if(findComment.getUser () == user) {
                commentService.deleteById (commentID);
                comment_message = "delete";
            }
            else {
                comment_message = "fail";
            }
        }
        mav.setViewName("redirect:read?id=" + boardID); // 뷰의 이름
        return mav;
    }
}
