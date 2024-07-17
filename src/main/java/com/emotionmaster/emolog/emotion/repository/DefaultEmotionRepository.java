package com.emotionmaster.emolog.emotion.repository;


import com.emotionmaster.emolog.color.dto.response.ColorResponse;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DefaultEmotionRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ColorResponse getColorByEmotion(String emotion){
        String sql = "SELECT red, blue, green, type FROM default_emotion WHERE emotion = ?";

        // RowMapper를 구현하여 ResultSet의 각 행을 ColorResponse 객체로 매핑합니다.
        return jdbcTemplate.queryForObject(sql, new Object[]{emotion}, new RowMapper<ColorResponse>() {
            public ColorResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                ColorResponse colorResponse = new ColorResponse();
                colorResponse.setRed(rs.getInt("red"));
                colorResponse.setBlue(rs.getInt("blue"));
                colorResponse.setGreen(rs.getInt("green"));
                int type = Integer.parseInt(rs.getString("type"));
                colorResponse.setType(EmotionType.values()[type]);
                return colorResponse;
            }
        });
    }
}
