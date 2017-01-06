package com.sogou.iplus.api;

import com.google.common.base.Throwables;
import org.springframework.beans.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.dao.DataAccessException;
import com.sogou.iplus.model.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ApiResult<?> internalServerError(Exception e) {
    e.printStackTrace();
    return new ApiResult<>(Errno.INTERNAL_ERROR, Throwables.getStackTraceAsString(e));
  }

  @ExceptionHandler(DataAccessException.class)
  public ApiResult<?> dataAccessExcption(Exception e) {
    e.printStackTrace();
    return new ApiResult<>(Errno.INTERNAL_ERROR, "interal db error");
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  public ApiResult<?> servletRequestBindingException(Exception e) {
    return new ApiResult<>(Errno.BAD_REQUEST, Throwables.getStackTraceAsString(e));
  }

  @ExceptionHandler(ApiResult.AsException.class)
  public ApiResult<?> apiResultAsException(ApiResult.AsException e) {
    return e.get();
  }

  @ExceptionHandler(Errno.BadRequestException.class)
  public ApiResult<?> badRequestException(Exception e) {
    return new ApiResult<>(Errno.BAD_REQUEST, Throwables.getStackTraceAsString(e));
  }

  @ExceptionHandler(Errno.InternalErrorException.class)
  public ApiResult<?> internalErrorException(Exception e) {
    return new ApiResult<>(Errno.INTERNAL_ERROR, Throwables.getStackTraceAsString(e));
  }

  @ExceptionHandler(TypeMismatchException.class)
  public ApiResult<?> typeMismatchException(Exception e) {
    return new ApiResult<>(Errno.BAD_REQUEST, e.toString());
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ApiResult<?> resourceAccessException(Exception e) {
    e.printStackTrace();
    return new ApiResult<>(Errno.INTERNAL_ERROR, e.getMessage());
  }
}
