package com.ityouzi.core.exception;

import com.ityouzi.core.core.domain.ResultMsg;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zmr
 * @author lucas
 */
@RestController
public class GlobalErrorController extends AbstractErrorController
{
    private static final String ERROR_PATH = "/error";

    public GlobalErrorController(ErrorAttributes errorAttributes)
    {
        super(errorAttributes);
    }

    @RequestMapping(value = ERROR_PATH)
    public ResultMsg error(HttpServletRequest request)
    {
        HttpStatus status = getStatus(request);
        return ResultMsg.error(status.value(), status.getReasonPhrase());
    }

    @Override
    public String getErrorPath()
    {
        return ERROR_PATH;
    }
}