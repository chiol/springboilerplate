package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.errors.ErrorResponse;
import kr.ibct.springboilerplate.commons.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(basePackageClasses = AccountController.class)
public class AccountControllerAdvice {

    @ExceptionHandler({UsernameNotFoundException.class, NotFoundException.class})
    @ResponseBody
    ResponseEntity<?> handleControllerException(Throwable ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(status.value(), ex.getMessage()), status);
    }

    @ExceptionHandler({errors.AccountExistException.class})
    @ResponseBody
    ResponseEntity<?> handleAccountExistException(Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), ex.getMessage()));
    }

}

