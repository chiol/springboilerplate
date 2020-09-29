package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.account.exceptions.AccountAuthorizationException;
import kr.ibct.springboilerplate.account.exceptions.AccountExistException;
import kr.ibct.springboilerplate.account.exceptions.AccountIdNotFoundException;
import kr.ibct.springboilerplate.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackageClasses = AccountController.class)
public class AccountControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, AccountIdNotFoundException.class})
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .code(status.value())
                        .message(ex.getMessage())
                        .error(status.toString())
                        .path(request.getRequestURI())
                        .build()
                );
    }

    @ExceptionHandler({AccountExistException.class})
    @ResponseBody
    ResponseEntity<?> handleAccountExistException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .code(status.value())
                        .message(ex.getMessage())
                        .error(status.toString())
                        .path(request.getRequestURI())
                        .build()
                );
    }

    @ExceptionHandler({AccountAuthorizationException.class})
    @ResponseBody
    ResponseEntity<?> handleAccountAuthorizationException(HttpServletRequest request,
                                                          Throwable ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .code(status.value())
                        .message(ex.getMessage())
                        .error(status.toString())
                        .path(request.getRequestURI())
                        .build()
                );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    ResponseEntity<?> handleIllegalArgumentException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .code(status.value())
                        .message(ex.getMessage())
                        .error(status.toString())
                        .path(request.getRequestURI())
                        .build()
                );
    }

}

