package kr.ibct.springboilerplate;

import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

public class DayTest extends BaseTest {

    @Value("${jwt.accessJwt.secret}")
    String day;

    @Test
    public void testEnum() {
        System.out.println(day);

    }

}
