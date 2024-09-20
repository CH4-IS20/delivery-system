package com.example.hub.presentation.controller;

import com.example.hub.application.service.HubService;
import com.example.hub.domain.model.Hub;
import com.example.hub.presentation.fixture.HubCreateRequestFixture;
import com.example.hub.presentation.fixture.HubFixture;
import com.example.hub.presentation.request.HubCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;


@WebMvcTest(HubController.class)
class HubControllerTest {

    Logger log = (Logger) LoggerFactory.getLogger(HubControllerTest.class);    // Junit에서 log찍기 위해 선언(Junit에서는 @Slf4j 어노테이션 사용 불가능)

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    HubService hubService;


    @Test
    @DisplayName("허브 작성 성공")
//    @WithMockUser(username = "1")
    void createHub() {

        Hub hub = HubFixture.get();
        HubCreateRequest request = HubCreateRequestFixture.get();


    }

    @Test
    void getHub() {
    }

    @Test
    void searchHubs() {
    }

    @Test
    void updateHub() {
    }

    @Test
    void deleteHub() {
    }
}