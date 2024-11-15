package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; //null 방지

//클래스는 객체생성 , 인터페이스는 메소드 집합
                                            //어떤 Entity클래스를 쓸건지, pk의 자료형이 무엇인지
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
//    추가적인 추상메서드도 정의 가능
//    이메일로 회원 정보를 조회(select * from member_table where member_email=?)
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
