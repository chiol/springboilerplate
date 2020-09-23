package kr.ibct.springboilerplate.account;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account signInRequestToAccount(AccountDto.SignUpRequest request);

    AccountDto.GetResponse toAccountResponse(Account account);

    void updateRequestToAccount(AccountDto.UpdateRequest request,@MappingTarget Account account);
}
