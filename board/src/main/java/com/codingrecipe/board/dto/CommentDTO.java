package com.codingrecipe.board.dto;

import com.codingrecipe.board.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity,Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
//        baseEntity에 있는 createdTime
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());

//        부모 엔티티에 접근해서 id가져오는 방법도 있고(Service메소드에 @Transactional필요!) 인자로 id까지 같이 넘겨받아서 처리하는 방법도 있다
//        commentDTO.setBoardId(commentEntity.getBoardEntity().getId());
        commentDTO.setBoardId(boardId);
        return commentDTO;

    }
}
