package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.errors.ErrorResponse;
import kr.ibct.springboilerplate.commons.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackageClasses = AccountController.class)
public class AccountControllerAdvice {

    @ExceptionHandler({UsernameNotFoundException.class, NotFoundException.class})
    @ResponseBody
    ResponseEntity<?> handleControllerException(Throwable ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(status.value(), ex.getMessage()), status);
    }

}
