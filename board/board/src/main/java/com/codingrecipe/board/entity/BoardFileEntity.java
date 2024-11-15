package com.codingrecipe.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

//    BoardEntity와 N:1 관계(한게시글(부모)에 파일(자식) 여러개) //여기가 부모 자식 설정
    @ManyToOne(fetch = FetchType.LAZY) //부모 호출할때 자식도 같이 호출되는데 LAZY속성은 필요한거만 가져오게끔 하는 속성
    @JoinColumn(name="board_id") //컬럼명
    private BoardEntity boardEntity; //부모엔티티타입

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}
