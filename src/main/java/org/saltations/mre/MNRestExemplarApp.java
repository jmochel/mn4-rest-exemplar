package org.saltations.mre;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "mn4-rest-exemplar",
                version = "0.0"
        )
)
public class MNRestExemplarApp
{
    public static void main(String[] args)
    {
        Micronaut.run(MNRestExemplarApp.class, args);
    }
}