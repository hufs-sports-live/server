package com.sports.server.query.acceptance;

import com.sports.server.query.dto.response.LeagueSportResponse;
import com.sports.server.support.AcceptanceTest;
import com.sports.server.query.dto.response.LeagueResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(scripts = "/league-fixture.sql")
public class LeagueQueryAcceptanceTest extends AcceptanceTest {

    @Test
    void 삭제되지_않은_모든_리그를_조회한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/leagues")
                .then().log().all()
                .extract();

        // then
        List<LeagueResponse> actual = toResponses(response, LeagueResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual)
                        .map(LeagueResponse::leagueId)
                        .containsExactly(3L, 2L, 1L),
                () -> assertThat(actual)
                        .map(LeagueResponse::name)
                        .containsExactly("롤 대회", "농구대잔치", "삼건물 대회")
        );
    }

    @Test
    void 리그의_모든_스포츠를_조회한다() {
        // given
        Long threeBuildingCup = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/leagues/{leagueId}/sports", threeBuildingCup)
                .then().log().all()
                .extract();

        // then
        List<LeagueSportResponse> actual = toResponses(response, LeagueSportResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual)
                        .map(LeagueSportResponse::name)
                        .containsExactly("축구"),
                () -> assertThat(actual)
                        .map(LeagueSportResponse::sportId)
                        .containsExactly(threeBuildingCup)
        );
    }
}
