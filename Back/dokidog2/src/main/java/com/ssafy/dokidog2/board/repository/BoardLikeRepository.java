package com.ssafy.dokidog2.board.repository;




import com.ssafy.dokidog2.board.entity.BoardLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Long> {

    // 주어진 boardId와 userId에 대해 좋아요가 이미 존재하는지 확인하는 메소드
//    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
