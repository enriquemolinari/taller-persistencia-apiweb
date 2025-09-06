package unrn.web;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//El término advise viene de AOP
//Un advice es un bloque de código que se ejecuta antes y/o despues de otro bloque código.
@ControllerAdvice
public class AgendaTelefonicaGlobalExceptionHandler {

    public static final String ERROR_MESSAGE_KEY = "errorMessage";
    public static final String ERROR_VIEW_NAME = "error";

    //las que lanza SpringMVC cuando el Body o cualquier otro parametro requerido no viene
    //las que lanza mi modelo o servicio
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeExceptions(RuntimeException ex) {
        return buildModelAndView(ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class})
    public ModelAndView handleSpringMVCParams() {
        return buildModelAndView("Parámetros inválidos");
    }

    private ModelAndView buildModelAndView(String errorMessage) {
        var mv = new ModelAndView(ERROR_VIEW_NAME);
        mv.addObject(ERROR_MESSAGE_KEY, errorMessage);
        return mv;
    }

    //cualquier otra
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException() {
        return buildModelAndView("Algo salió mal, intente nuevamente");
    }

}
