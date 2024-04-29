package com.example.typesaferesource;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.Resources;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hello")
public class HelloRestController {

    private final MessageSource messageSource;

    @GetMapping
    public String hello() {
        return messageSource.getMessage(Resources.HELLO.getKey(), null, LocaleContextHolder.getLocale());
    }
}
