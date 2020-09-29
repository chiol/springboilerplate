package kr.ibct.springboilerplate.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseTestTest extends BaseTest {
    @Value("${myapp.name}")
    String name;

    @Order(2)
    @Test
    public void testMapper() throws JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestId("testId");
        testClass.setTestName("testName");
        String result = "{\"testId\":\"testId\",\"testName\":\"testName\"}";
        assertThat(this.objectMapper.writeValueAsString(testClass)).isEqualTo(result);
    }

    @Order(1)
    @Test
    public void testMockMvc() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(4)
    @Test
    public void testProfile() {

        assertThat(name).isEqualTo("ibct-boilerplate");
    }

    @Data
    public static class TestClass {
        private String testId;
        private String testName;
    }
}
