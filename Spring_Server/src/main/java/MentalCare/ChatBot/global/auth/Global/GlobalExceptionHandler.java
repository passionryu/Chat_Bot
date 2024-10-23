package MentalCare.ChatBot.global.auth.Global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*글로벌 에러*/

    // 유효성 검사 실패 시 400 Bad Request 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)//MethodArgumentNotValidException 예외 = 데이터 바인딩 과정에서 유효성 검사의 실패 시
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult() //바인딩 오류의 결과를 가져온다
                .getAllErrors() //모든 오류를 포함한 리스트를 반환
                .get(0) //그 중 첫번쨰 오류를 가져온다
                .getDefaultMessage(); //해당 오류의 기본 메시지를 가져온다.
        return ResponseEntity.badRequest().body(errorMessage); //400응답과 함께 에러메시지 본문을 반환
    }

    // 그 외 모든 에러시에는 500에러 발생
    @ExceptionHandler(Exception.class) //모든 Exception 유형의 예외를 처리, 즉, 이 메서드는 위에서 처리되지 않은 모든 예외에 대해 호출됩니다.
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
