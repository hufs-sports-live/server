package com.sports.server.query.application;

import com.sports.server.command.record.domain.Record;
import com.sports.server.query.repository.RecordQueryRepository;
import com.sports.server.query.dto.response.TimelineResponse;
import com.sports.server.command.sport.domain.Quarter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimelineQueryService {

    private final RecordQueryRepository recordQueryRepository;

    public List<TimelineResponse> getTimeline(final Long gameId) {
        List<Record> records = recordQueryRepository.findByGameIdOrderByQuarterAndScoredAtDesc(gameId);
        Map<Quarter, List<Record>> groupedByQuarter = records.stream()
                .collect(groupingBy(Record::getScoredQuarter));
        return groupedByQuarter.keySet()
                .stream()
                .sorted(Comparator.comparingLong(Quarter::getId).reversed())
                .map(quarter -> new TimelineResponse(quarter, groupedByQuarter.get(quarter)))
                .toList();
    }
}
