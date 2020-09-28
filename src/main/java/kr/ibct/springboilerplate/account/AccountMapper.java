package kr.ibct.springboilerplate.account;


import kr.ibct.springboilerplate.account.exceptions.AccountPutNullException;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account signInRequestToAccount(AccountDto.SignUpRequest request);

    AccountDto.GetResponse toAccountResponse(Account account);

    @Mapping(target = "phoneNum", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "username", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchRequestToAccount(AccountDto.PatchRequest from, @MappingTarget Account to);

    @Mapping(target = "phoneNum", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    @Mapping(target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    @Mapping(target = "username", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    void putRequestToAccount(AccountDto.PutRequest from, @MappingTarget Account to) throws AccountPutNullException;
}
