package com.codingrecipe.board.repository;

import com.codingrecipe.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> { //<entity클래스이름, pk타입>
//    update board_table st board_hits=board_hits+1 where id=? //네이티브쿼리
//    jpa Entity를 이용한 쿼리문 작성 //update나 delete문 쿼리문은 @Modifying 에노테이션 필수로 필요
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}
