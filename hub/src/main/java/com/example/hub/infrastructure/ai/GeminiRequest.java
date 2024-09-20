package com.example.hub.infrastructure.ai;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
public class GeminiRequest {

    private List<Content> contents;     // 객체의 요청의 내용 담을 공간

    public GeminiRequest(String text) {
        Part part = new TextPart(text);
        Content content = new Content(Collections.singletonList(part));
        this.contents = Arrays.asList(content);
    }


    @Getter
    @AllArgsConstructor
    private static class Content{
        private List<Part> parts;
    }

    interface Part{}

    @Getter
    @AllArgsConstructor
    private static class TextPart implements Part{
        public String text;
    }
}

