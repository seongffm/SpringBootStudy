package com.codingrecipe.member.dto;

import com.codingrecipe.member.entity.MemberEntity;
import lombok.*;

@Getter
@Setter

@NoArgsConstructor //기본생성자 만들어주는 에노테이션(초기화)
@AllArgsConstructor //필드를 모두다 매개변수로 하는 생성자 만들어주는 에노테이션
@ToString
//클래스
public class MemberDTO {
    //필드
    private long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    //함수
    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        return memberDTO;
    }
}
