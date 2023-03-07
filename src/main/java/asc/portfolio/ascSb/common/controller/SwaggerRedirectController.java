package asc.portfolio.ascSb.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping("/")
    public String swaggerRedirector() {
        return "redirect:/swagger-ui/index.html";
    }
}
