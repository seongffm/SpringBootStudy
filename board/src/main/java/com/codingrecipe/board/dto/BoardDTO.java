package com.codingrecipe.board.dto;

import com.codingrecipe.board.entity.BoardEntity;
import com.codingrecipe.board.entity.BoardFileEntity;
import lombok.*;
//spring에서 제공하는 실제 파일을 담는 Multipart인터페이스
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//DTO(Data Transfer Object)
//1.필드 2.생성자 3.getter setter 4.To String
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

//    multiple로 파일 여러개 첨부시 List로 받아야하는 MultipartFile객체
    private List<MultipartFile> boardFile; //save.html -> Controller 파일 담는 용도
//    이름들도 List객체로 선언
    private List<String> originalFileName; //원본 파일 이름
    private List<String> storedFileName; //서버 저장용 파일 이름
    private int fileAttached; //파일 첨부 여부(첨부1, 미첨부0) (flag값)


    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        if(boardEntity.getFileAttached()==0){
            boardDTO.setFileAttached(boardDTO.getFileAttached());//0
        }else{
            boardDTO.setFileAttached(boardEntity.getFileAttached());//1
            //파일 여러개 첨부시 리스트로
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            for(BoardFileEntity boardFileEntity:boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);

            //파일 이름을 가져가야 함.
//            original FileName, storedFileName : board_file_table(BoardFileEntity)
//            다른 테이블 조회 join ->
//            select * from board_table b, board_file_table bf where b.id=bf.board_id and where b.id=?
//            파일 하나만 첨부할때는 리스트에서 [0]번만 추출하면 되니까 아래처럼 함
//            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
//            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
        }
        return boardDTO;
    }

//    단축키 : alt + insert
// cntrl 클릭으로 원하는 매개변수의 생성자만 생성가능
    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }
}
