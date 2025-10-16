package com.example.todo_kotlin.exception

import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.OffsetDateTime

data class ErrorBody(
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)

@ControllerAdvice
class GlobalExceptionHandler {
    private fun body(st: HttpStatus, msg: String, path: String) =
        ErrorBody(status = st.value(), error = st.reasonPhrase, message = msg, path = path)

    // 404 – ID inexistente / delete inexistente
    @ExceptionHandler(EntityNotFoundException::class, EmptyResultDataAccessException::class)
    fun notFound(ex: Exception, req: HttpServletRequest): ResponseEntity<ErrorBody> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(body(HttpStatus.NOT_FOUND, "Recurso não encontrado.", req.requestURI))

    // 400 – validação @Valid
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(ex: MethodArgumentNotValidException, req: HttpServletRequest): ResponseEntity<ErrorBody> {
        val msg = ex.bindingResult.fieldErrors.firstOrNull()?.let { "${it.field}: ${it.defaultMessage}" }
            ?: "Parâmetros inválidos."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body(HttpStatus.BAD_REQUEST, msg, req.requestURI))
    }

    // 400 – JSON malformado / enum inválido
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun unreadable(ex: HttpMessageNotReadableException, req: HttpServletRequest): ResponseEntity<ErrorBody> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido.", req.requestURI))

    // 400 – tipo errado em path/query (ex.: id não numérico)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun typeMismatch(ex: MethodArgumentTypeMismatchException, req: HttpServletRequest): ResponseEntity<ErrorBody> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(body(HttpStatus.BAD_REQUEST, "Parâmetro inválido.", req.requestURI))

    // 405 – método não suportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun methodNotAllowed(
        ex: HttpRequestMethodNotSupportedException,
        req: HttpServletRequest
    ): ResponseEntity<ErrorBody> =
        ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(body(HttpStatus.METHOD_NOT_ALLOWED, "Método não suportado.", req.requestURI))

    // 500 – fallback
    @ExceptionHandler(Exception::class)
    fun generic(ex: Exception, req: HttpServletRequest): ResponseEntity<ErrorBody> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno.", req.requestURI))
}