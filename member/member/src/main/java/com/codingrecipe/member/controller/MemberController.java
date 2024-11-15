package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor//    에노테이션 붙은 스프링 객체 주입(자원 사용 권한 갖게 함)
public class MemberController {
// 생성자 주입
   private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm(){
        return "save";
    }
    @PostMapping("/member/save")
    public String save(//@RequestParam("memberEmail") String memberEmail,
                       //@RequestParam("memberPassword") String memberPassword,
                       //@RequestParam("memberName") String memberName){

                       //전달된 객체를 자동으로 매핑하여 모델에 자동으로 추가하고 뷰에서 접근 가능하도록 만들어줌
                       @ModelAttribute MemberDTO memberDTO
                       ){
//        soutm 단축키
        System.out.println("MemberController.save");
//        soutp 단축키
//        System.out.println("memberEmail = " + memberEmail + ", memberPassword = " + memberPassword + ", memberName = " + memberName);
        System.out.println("memberDTO = " + memberDTO);

        /*
        * MemberService memberService = new MemberService();
        * memberService.save();
        * */

        memberService.save(memberDTO);
        return "login";
    }
    @PostMapping("/member/login")
//    로그인 -> 세션 이용해서 활용
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
            MemberDTO loginResult = memberService.login(memberDTO);
            if (loginResult!=null){
//                login 성공
                System.out.println(loginResult);
                session.setAttribute("loginEmail",loginResult.getMemberEmail());
                return "main";
            }else {
//                login 실패
                return "login";
            }
        }
    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/member/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList= memberService.findAll();
        // 어떠한 html로 가져갈 데이터가 있다면 model사용
        model.addAttribute("memberList",memberDTOList);
        return "list";
        }

        @GetMapping("/member/{id}")
//        쿼리스트링으로 값 가져올때 -> request.파라미터로 값 가져오고
//        경로상의 값 가져올때- > PathVairable로 가져옴
        public String findById(@PathVariable("id") Long id, Model model){
//            조회하는 데이터가 하나니까 ->
            MemberDTO memberDTO = memberService.findByID(id);
            model.addAttribute("member",memberDTO);
            return "detail";
        }
        @GetMapping("/member/update")
        public String updateForm(HttpSession session, Model model){
//            세션에 setAttribute로 저장하면 타입이 객체로 저장되기에 형변환 진행해줘야한다.
            String myEmail = (String) session.getAttribute("loginEmail");
            MemberDTO memberDTO = memberService.updateForm(myEmail);
            model.addAttribute("updateMember",memberDTO);
            return "update";

        }
        @PostMapping("/member/update")
        public String update(@ModelAttribute MemberDTO memberDTO){
            memberService.update(memberDTO);
            return "redirect:/member/" + memberDTO.getId();
        }

        @GetMapping("/member/delete/{id}")
        public String deleteById(@PathVariable("id") Long id){
            memberService.deleteById(id);
//            리다이렉트는 html파일 이름이 아니라 주소를 명시해야한다
            return "redirect:/member/";
        }
        @GetMapping("/member/logout")
        public String logout(HttpSession session){
            session.invalidate();
            return "index";
        }
        @PostMapping("/member/email-check")
        public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail){
            System.out.println("memberEmail = " + memberEmail);
            String checkResult = memberService.emailCheck(memberEmail);
            return checkResult;
//            if(checkResult!=null){
//                return "ok";
//            }else{
//                return "no";
//            }

        }
    }

