package com.ssafy.dokidog2.board.repository;

import com.ssafy.dokidog2.board.entity.BoardCategory;
import com.ssafy.dokidog2.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // update board_table set board_hits=board_hits+1 where id=?
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.boardId=:boardId")
    void updateHits(@Param("boardId") Long boardId);

    List<BoardEntity> findByBoardCategory(BoardCategory category);


}
