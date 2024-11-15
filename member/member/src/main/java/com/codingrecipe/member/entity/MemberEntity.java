package com.codingrecipe.member.entity;

import com.codingrecipe.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "member_table") //Create 에노테이션
public class MemberEntity {
    @Id //pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Mysql에서 auto_increment(시퀀스)
    private long id; //1부터 주어짐
    @Column(unique = true) //unique 제약조건 추가
    private String memberEmail;
    @Column
    private String memberPassword;
    @Column
    private String memberName;
//    Hibernate: create table member_table (id bigint not null auto_increment, member_email varchar(255), member_name varchar(255), member_password varchar(255), primary key (id)) engine=InnoDB
//Hibernate: alter table member_table add constraint UK_tdbojd0ydkcxi394wslvmcvn3 unique (member_email)

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        return memberEntity;
    }

    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        return memberEntity;
    }
}
