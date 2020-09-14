package kr.ibct.springboilerplate.account;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account signInRequestToAccount(AccountDto.signUpRequest request);

    AccountDto.accountResponse toAccountResponse(Account account);

    void updateRequestToAccount(AccountDto.updateRequest request,@MappingTarget Account account);
}
