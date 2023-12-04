package com.tovelop.maphant.configure

import com.tovelop.maphant.utils.ResponseJsonWriter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(e: MethodArgumentTypeMismatchException?): ResponseEntity<String> {

        return ResponseEntity(
            ResponseJsonWriter.writeJSON(
                mapOf(
                    "message" to (e?.message ?: "메서드 파라메터 타입 불일치 (Int 대신 String이 들어왔다던지 등)"),
                    "success" to false
                )
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}