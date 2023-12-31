package com.sports.server.command.comment.domain;


import org.springframework.data.repository.Repository;

public interface CommentRepository extends Repository<Comment, Long> {
    void save(Comment comment);
}
