package com.codingrecipe.board.controller;

import com.codingrecipe.board.dto.BoardDTO;
import com.codingrecipe.board.dto.CommentDTO;
import com.codingrecipe.board.service.BoardService;
import com.codingrecipe.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
//매핑 경로 겹치지 않게 기본 주소 경로 지정
@RequiredArgsConstructor //
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
//    댓글 목록 가져와야하기 때문에 CommentService주입받기
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }
    @GetMapping("/")
    public String findAll(Model model){
//        DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
        List<BoardDTO>boardDTOList= boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "list";
    }
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable){
        /*
        * 해당 게시글의 조회수를 하나 올리고
        * 게시글 데이터를 가져와서 detail.html에 출력
        * */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
//        댓글 목록 가져오기 추가하기
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList",commentDTOList);

        model.addAttribute("board",boardDTO);
        model.addAttribute("page",pageable.getPageNumber());
        return "detail";
    }
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate",boardDTO);
        return "update";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO,Model model){
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board",board);
        return "detail";
//        리다이렉트시 조회수 올라가니까 올라가는거 방지위해 리다이렉트 안쓴거
//        return "redirect:/board/"+boardDTO.getId();
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }
//    /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page=1) Pageable pageable, Model model){
//        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();//3 6 9 12 ~~(총 페이지보다 크게 보여줄 필요 없으니)
//        page 갯수 20개
//        현재 사용자가 3페이지
//        1 2 3
//        밑에 보여지는 페이지 갯수 3개
//        현재 사용자가 7페이지
//        7 8 9 가 띄워져야 함(밑에)
        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "paging";
    }
}
