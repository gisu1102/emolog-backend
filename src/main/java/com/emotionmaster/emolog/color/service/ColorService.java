package com.emotionmaster.emolog.color.service;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.dto.response.ColorAndDate;
import com.emotionmaster.emolog.color.dto.response.ColorResponse;
import com.emotionmaster.emolog.color.repository.ColorRepository;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import com.emotionmaster.emolog.emotion.repository.DefaultEmotionRepository;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;
    private final DefaultEmotionRepository defaultEmotionRepository;
    private final UserService userService;

    public List<ColorAndDate> findAllColorOfMonth(int month){
        User user = userService.getCurrentUser();
        return colorRepository.findColorAndDateByMonthAndUserId(month, user.getId());
    }

    public List<ColorAndDate> findAllColorOfWeek(int month, int week) {
        User user = userService.getCurrentUser();
        return colorRepository.findAllByMonthAndWeekAndUserId(month, week, user.getId());
    }

    public EmotionType save(String emotion, Diary diary) {
        //rgb값 넣는 List
        List<List<Integer>> rgbOfEmotions = new ArrayList<>();
        for (int i=0; i<3; i++){
            rgbOfEmotions.add(new ArrayList<>(0));
        }

        //Emotion 종류 넣는 Map
        Map<EmotionType, Integer> types = new HashMap<>();
        for (int i=0; i<EmotionType.values().length; i++){
            types.put(EmotionType.values()[i], 0);
        }

        //전달받은 감정들을 '/'로 나눠서 DB에 입력된 rgb값과 type을 넣는다.
        List<String> emotions = arrangeRequest(emotion, rgbOfEmotions, types);

        //전달받은 rgb을 통해 오늘의 색을 계산하고, hex 코드를 만들어 저장한다.
        int [] rgb = calculateColor(rgbOfEmotions, emotions.size());
        colorRepository.save(new Color(rgb[0], rgb[1], rgb[2], HexColor(rgb), diary));

        //최빈값을 통한 감정의 type을 반환
        return getEmotionType(types);
    }

    private static EmotionType getEmotionType(Map<EmotionType, Integer> types) {
        Optional<Map.Entry<EmotionType, Integer>> emotion_type = types.entrySet().stream()
                .max(Comparator.comparing(Map.Entry<EmotionType, Integer>::getValue)
                        .thenComparing(Map.Entry::getKey));

        return emotion_type.map(Map.Entry::getKey).orElse(null);
    }

    private List<String> arrangeRequest(String emotion, List<List<Integer>> rgbOfEmotions, Map<EmotionType, Integer> types) {
        List<String> emotions = List.of(emotion.split("/"));
        for (String emo : emotions){
            ColorResponse colorResponse = defaultEmotionRepository.getColorByEmotion(emo);
            rgbOfEmotions.get(0).add(colorResponse.getRed());
            rgbOfEmotions.get(1).add(colorResponse.getGreen());
            rgbOfEmotions.get(2).add(colorResponse.getBlue());
            EmotionType type = colorResponse.getType();
            types.put(type, types.get(type) + 1); //type의 경우 최빈값을 구하기 위해 하나씩 올리기
        }
        return emotions;
    }

    private String HexColor(int[] rgb) {
        StringBuilder hexa= new StringBuilder();
        for (int i=0; i<3; i++) {
            String hexString = Integer.toHexString(rgb[i]);
            if (hexString.equals("0")) hexString = "00";
            hexa.append(hexString);
        }
        return hexa.toString();
    }

    private int [] calculateColor(List<List<Integer>> rgbOfEmotions, int len) {
        double bigWeight = (double) len /10;
        double smallWeight = (1 - bigWeight) / (double)(len-1);
        int index = 0;
        int [] rgb = new int[3];
        for (List<Integer> colors : rgbOfEmotions){
            Optional<Integer> amount = colors.stream().max(Integer::compareTo);
            if (amount.isPresent()) {
                colors.remove(amount.get());
                int sum = colors.stream().mapToInt(Integer::intValue).sum();
                int color = (int) (Math.ceil(amount.get() * bigWeight) + sum * smallWeight);
                if (color < 256) rgb[index] = color; else rgb[index] = 255;
                index++;
            }
        }
        return rgb;
    }
}
