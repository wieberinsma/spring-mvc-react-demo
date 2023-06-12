package nl.han.rwd.srd.domain.user.impl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateController
{
    @PreAuthorize("hasAuthority('ADMIN_AUTH')")
    @GetMapping("/private")
    public HttpStatus privateTest() {
        return HttpStatus.OK;
    }
}
