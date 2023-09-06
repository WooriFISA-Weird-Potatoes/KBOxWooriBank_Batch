package com.woorifisa.kboxwoori.batch.prediction.processor;

import com.woorifisa.kboxwoori.batch.prediction.UserPrediction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class CalcPredictionProcessor implements ItemProcessor<Map.Entry<Object, Object>, UserPrediction> {

    private final RedisTemplate<String, String> redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final LocalDate date;

    private static final String TODAY_SCHEDULE_KEY = "crawling:todaySchedule:";
    private static final String FIND_USER_SQL = "SELECT id FROM users WHERE user_id = ?";
    private static final int CORRECT_ANSWER_POINTS = 3;
    private List<String> winningTeams;

    @PostConstruct
    public void init() {
        winningTeams = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(TODAY_SCHEDULE_KEY + date.minusDays(1) + "-?");
        if (keys != null) {
            getWinningTeams(keys);
            log.info("winningTeams = {}", winningTeams);
        }
    }

    private void getWinningTeams(Set<String> keys) {
        for (String key : keys) {
            Map<Object, Object> match = redisTemplate.opsForHash().entries(key);
            int team1Score = Integer.parseInt(match.get("team1Score").toString());
            int team2Score = Integer.parseInt(match.get("team2Score").toString());

            if (team1Score > team2Score) {
                winningTeams.add(redisTemplate.opsForHash().get(key, "team1Name").toString());
            } else if (team1Score < team2Score) {
                winningTeams.add(redisTemplate.opsForHash().get(key, "team2Name").toString());
            }
        }
    }

    @Override
    public UserPrediction process(Map.Entry<Object, Object> item) throws Exception {
        String userId = (String) item.getKey();
        String predictions = (String) item.getValue();
        List<String> predictionsList = Arrays.asList(predictions.substring(1, predictions.length() - 1).split(", "));
        log.info("userId = {}, predictionsList = {}", userId, predictionsList);

        Long id = jdbcTemplate.queryForObject(FIND_USER_SQL, Long.class, userId);

        int count = comparePredictionResults(predictionsList);

        int extraPoints = CORRECT_ANSWER_POINTS * count;

        return new UserPrediction(id, extraPoints);
    }

    private int comparePredictionResults(List<String> predictionsList) {
        return (int) predictionsList.stream().filter(p -> winningTeams.stream()
                .anyMatch(Predicate.isEqual(p))).count();
    }
}
