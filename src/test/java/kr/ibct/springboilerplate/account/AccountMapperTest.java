package kr.ibct.springboilerplate.account;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    String testEmail = "test@test.com";
    String testPassword = "testPassword";

    @Test
    public void mappingTest() {

        //given
        AccountDto.SignUpRequest request = new AccountDto.SignUpRequest();
        request.setEmail(testEmail);
        request.setPassword(testPassword);

        //when
        Account account = AccountMapper.INSTANCE.signInRequestToAccount(request);

        assertThat(account).isNotNull();
        assertThat(account.getEmail()).isEqualTo(request.getEmail());
        assertThat(account.getPassword()).isEqualTo(request.getPassword());
    }
}