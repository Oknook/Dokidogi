package com.ssafy.dokidog2.board.repository;


import com.ssafy.dokidog2.board.entity.BoardEntity;
import com.ssafy.dokidog2.board.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {

    //    BoardFileEntity save(BoardFileEntity boardFileEntity);
    List<BoardFileEntity> findAllByBoardEntity(BoardEntity boardEntity);
}
