package com.codingrecipe.board.service;

import com.codingrecipe.board.dto.CommentDTO;
import com.codingrecipe.board.entity.BoardEntity;
import com.codingrecipe.board.entity.CommentEntity;
import com.codingrecipe.board.repository.BoardRepository;
import com.codingrecipe.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
//    부모 엔티티를 먼저 진행(넘겨줘야함) 해야하기에 주입받고
    private final BoardRepository boardRepository;

//    Long타입으로 (id리턴하게) 메서드 작성
    public Long save(CommentDTO commentDTO) {
//        부모 엔티티(BoardEntity) 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO,boardEntity);
            return commentRepository.save(commentEntity).getId();
        }else{
            return null;
        }

    }

    public List<CommentDTO> findAll(Long boardId) {
//        select * from comment_table where board_id = ? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /*
        * EntityList -> DToList
        */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity: commentEntityList){
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity,boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
