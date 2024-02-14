package com.ssafy.dokidog2.user.repository;


import com.ssafy.dokidog2.user.entity.Block;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("select b.blocked.userId from Block b where b.blocking.userId = :userId")
    List<Long> findBlocksByBlockingId(long userId);

    @Query("select b.blockId from Block b where b.blocking.userId = :userId and b.blocked.userId = :blockedId")
    Block findBlockByBlockingUserId(long userId, long blockedId);
}
