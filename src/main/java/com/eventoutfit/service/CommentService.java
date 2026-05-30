package com.eventoutfit.service;

import com.eventoutfit.model.Comment;
import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.User;
import com.eventoutfit.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final OutfitService outfitService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository,
                          OutfitService outfitService,
                          UserService userService) {
        this.commentRepository = commentRepository;
        this.outfitService = outfitService;
        this.userService = userService;
    }

    public List<Comment> getCommentsByOutfit(Long outfitId) {
        logger.debug("Получение комментариев к образу: outfitId={}", outfitId);
        return commentRepository.findByOutfitIdOrderByCreatedAtAsc(outfitId);
    }

    @Transactional
    public boolean addComment(Long outfitId, Long userId, String text) {
        logger.info("Добавление комментария: outfitId={}, userId={}", outfitId, userId);

        if (text == null || text.isBlank()) {
            logger.warn("Комментарий пустой");
            return false;
        }

        Outfit outfit = outfitService.findById(outfitId).orElse(null);
        User user = userService.findById(userId).orElse(null);

        if (outfit == null || user == null) {
            logger.warn("Образ или пользователь не найден");
            return false;
        }

        Comment comment = new Comment(text, user, outfit);
        commentRepository.save(comment);

        logger.info("Комментарий добавлен: id={}", comment.getId());
        return true;
    }
}