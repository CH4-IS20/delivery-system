package com.example.hub.infrastructure.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class GeminiResponse {

    private List<Candidate> candidates;

    @Getter
    public static class Candidate{
        private Content content;
        private String finishReason;
        private int index;
        List<SafetyRating> safetyRatings;
    }

    @Getter
    public static class Content{
        private List<TextPart> parts;
        private String role;
    }

    @Getter
    public static class TextPart{
        private String text;
    }

    @Getter
    public static class SafetyRating{
        private String category;
        private String probability;
    }
}
