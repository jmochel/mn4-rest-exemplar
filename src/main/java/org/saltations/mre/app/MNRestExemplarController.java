package org.saltations.mre.app;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/mn-rest-exemplar")
public class MNRestExemplarController
{
    @SuppressWarnings("SameReturnValue")
    @Get(produces = "text/json")
    public String index()
    {
        return "{}";
    }
}
