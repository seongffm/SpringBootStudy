package com.codingrecipe.board.repository;

import com.codingrecipe.board.entity.BoardEntity;
import com.codingrecipe.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    //        select * from comment_table where board_id = ? order by id desc;
//    Jpa에서 쿼리 실행한다 -> 대소 문자 구분해서 적어야 함
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
