package org.saltations.mre;

import io.micronaut.http.annotation.*;

@Controller("/mn4-rest-exemplar")
public class Mn4RestExemplarController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}