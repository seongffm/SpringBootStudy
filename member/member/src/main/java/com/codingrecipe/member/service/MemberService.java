package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
//        repository의 save메서드 호출(조건, entity객체를 넘겨줘야함)
//        1.dto->entity로 변환
//        2.repository의 save 메소드 호출

//        단축키 alt+Enter 좌변을 자동으로 만들어줌
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);

//        Jpa가 제공해주는 메서드 기본키 없는 경우 insert 있는경우 update
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
    /*
    * 1. 회원이 입력한 이메일로 DB에서 조회를 함
    * 2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
    */
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()){
//            조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            // Optional객체를 벗겨주는 get메소드
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
//                string 객체 비교할때 ==(x) equls메소드 사용
                // 비밀번호가 일치
//                entity -> dto변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;

            }else{
                //비밀번호가 불일치
                return null;
            }
        }else {
//            조회 결과가 없다(해당 이메일을 가진 회원이 없다
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
//        for each 구문 : for(각 요소 값: 배열이나 컨테이너 값)
        for (MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
//            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
//            memberDTOList.add(memberDTO);
        }
        return memberDTOList;
    }

    public MemberDTO findByID(Long id) {
//        엔티티 하나 값 받아올때 Optional로 감싸야 함
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
//            MemberEntity memberEntity = byId.get();
//            MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
//            return dto;
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

//    수정하기 전에 회원 정보들 출력되있어야 하기에 updateForm필요
    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
//            System.out.println(MemberDTO.toMemberDTO(optionalMemberEntity.get()));
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }

    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if(byMemberEmail.isPresent()){
            //조회 결과가 있다 -> 사용할 수 없다.
            return null;
        }else{
            //조회 결과가 있다 -> 사용할 수 없다
            return "ok";
        }
    }
}
