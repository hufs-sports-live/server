package com.sports.server.command.report.domain;

import com.sports.server.command.comment.domain.Comment;
import com.sports.server.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

class ReportTest {

    @DisplayName("신고는")
    @Nested
    class CreateTest {

        @Test
        void 댓글로_생성한다() {
            // given
            Comment comment = new Comment("신고해봐", 1L);

            // when
            Report report = new Report(comment);

            // then
            assertThat(report).isNotNull();
        }

        @Test
        void 블락된_댓글로_생성할_수_없다() {
            // given
            Comment comment = mock(Comment.class);
            given(comment.isBlocked()).willReturn(true);

            // when then
            assertThatThrownBy(() -> new Report(comment))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 블락된 댓글은 신고할 수 없습니다.");
        }
    }
}
