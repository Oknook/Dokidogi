package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.entity.Block;
import com.ssafy.dokidog2.user.repository.BlockRepository;
import com.ssafy.dokidog2.user.repository.UserRepository;
import com.ssafy.dokidog2.util.Response;
import java.time.LocalDateTime;

public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    public BlockService(BlockRepository blockRepository, UserRepository userRepository) {
        this.blockRepository = blockRepository;
        this.userRepository = userRepository;
    }

    public Response block(long userId, long blockId) {
        return Response.builder()
            .code("200")
            .message("차단 등록")
            .data(blockRepository.save(
                Block.builder()
                    .blocking(userRepository.findByUserId(userId))
                    .blocked(userRepository.findByUserId(blockId))
                    .regDttm(LocalDateTime.now())
                .build()))
            .build();
    }

    public Response getList(long userId) {
        return Response.builder()
            .code("200")
            .message("차단 리스트")
            .data(blockRepository.findBlocksByBlockingId(userId))
            .build();
    }

    public Response unblock(long userId, long blockId) {
        blockRepository.delete(blockRepository.findBlockByBlockingUserId(userId, blockId));
        return Response.builder()
            .code("200")
            .message("차단 해제")
            .build();
    }
}
