package com.codingrecipe.board.service;

import com.codingrecipe.board.dto.BoardDTO;
import com.codingrecipe.board.entity.BoardEntity;
import com.codingrecipe.board.entity.BoardFileEntity;
import com.codingrecipe.board.repository.BoardFileRepository;
import com.codingrecipe.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
//        DTO -> Entity 필요 //Enity관리는 서비스에서 관리하는거 권고(Entity<->DTO)
//        파일 첨부 여부에 따라 로직 분리 필요!
        if(boardDTO.getBoardFile().isEmpty()){
            //첨부 파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        }else{
//            첨부 파일 있음.
            /*
            1. DTO에 담긴 파일을 꺼냄
            2. 파일의 이름 가져옴
            3. 서버 저장용 이름을 만듬
            //내사진.jpg -> 939398333218(고유값(난수))_내사진.jpg
            4. 저장 경로 설정
            5. 해당 경로에 파일 저장
            6. board_table에 해당 데이터 save처리
            7. board_file_table에 해당 데이터 save처리
            * */

//          파일 여러개 첨부시 BoardTable은 한줄만 추가되니 먼저 fileattached 1로 변경 //이 작업 먼저
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
//            save(create)에 사용하는 Entity객체는 id 가 없기에 id값 포함한 Entity객체위해 아래 작업 필요!
            BoardEntity board = boardRepository.findById(savedId).get(); //optional 벗기고 entity객체 가져오기

            for(MultipartFile boardFile: boardDTO.getBoardFile()) {//1
//            MultipartFile boardFile = boardDTO.getBoardFile(); //1
                String originalFilename = boardFile.getOriginalFilename(); //2
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; //3
                String savePath = "C:/springboot_img/" + storedFileName; //C:/springboot_img(폴더이름)/939398333218(고유값(난수))_내사진.jpg //4
                boardFile.transferTo(new File(savePath)); //5
                //Board Entity에 저장(파일 하나 첨부시)

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
//            파일 여러개 첨부시 Board테이블에는 한줄만 추가 되지만
//            BoardFile테이블에는 여러줄이 추가 된다
        }

    }
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
//        Entity -> DTO 필요
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;

    }
//    수동적인 쿼리 작성 시 스프링이 트랜잭션 관리하게끔 트랜잭션 에노테이션 필요
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional // 부모 엔티티->자식엔티티 접근하고 있기에
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }

//    public void로 선언한 save와의 차이 (void는 return 없어도 되는데 update는 업데이트 한 값 전달해야함)
    public BoardDTO update(BoardDTO boardDTO) {
//        id값까지 같이 보내면 save는 update로 처리한다
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
//        optional벗기는 과정 위에 findById메소드로 진행했기에 그 메소드 사용
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        int pageLimit =3; //한 페이지에 보여줄 글 갯수
//        한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순으로 정렬
//        page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

//        목록에서 보여줄 데이터 : id, writer, title, hits, createTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board->new BoardDTO(board.getId(),board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;



    }
}
