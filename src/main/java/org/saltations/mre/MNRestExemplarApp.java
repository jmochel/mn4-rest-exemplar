package org.saltations.mre;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "mn4-rest-exemplar",
                description = "API for a Micronaut exemplar application",
                contact = @Contact(name = "Jim Mochel", email = "jmochel@saltations.org"),
                version = "0.0.0"
        ),
        extensions = @Extension(
                        properties = {
                                @ExtensionProperty(name="api-id", value = "org.saltations.exemplar-01"),
                                @ExtensionProperty(name="audience", value = "company-internal")
                        }
        )
)
public class MNRestExemplarApp
{
    public static void main(String[] args)
    {
        Micronaut.run(MNRestExemplarApp.class, args);
    }
}