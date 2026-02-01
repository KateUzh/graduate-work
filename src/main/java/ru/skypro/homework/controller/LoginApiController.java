package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skypro.homework.api.LoginApi;
import ru.skypro.homework.api.LoginApiDelegate;

import javax.annotation.Generated;
import java.util.Optional;

@CrossOrigin(value = "http://localhost:3000")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Controller
@RequestMapping("${openapi.aPIDocumentation.base-path:}")
public class LoginApiController implements LoginApi {

    private final LoginApiDelegate delegate;

    public LoginApiController(@Autowired(required = false) LoginApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new LoginApiDelegate() {});
    }

    @Override
    public LoginApiDelegate getDelegate() {
        return delegate;
    }

}
