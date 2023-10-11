package org.saltations.mre.persons.model;


import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.saltations.mre.core.annotations.StdEmailAddress;

/**
 * Common interface with the core attributes describing a person.
 */

@Introspected
@Schema(name = "person", description = "Represents a person's basic contact info")
public interface Person
{
    @Schema(description = "The age of the person")
    Integer getAge();

    void setAge(@NotNull @Min(12L) Integer age);

    @Schema(description = "The first name of the person")
    String getFirstName();

    void setFirstName(@NotNull @NotBlank @Size(max = 50) String firstName);

    @Schema(description = "The last name of the person")
    String getLastName();

    void setLastName(@NotNull @NotBlank @Size(max = 50) String lastName);

    @Schema(description = "Email address", example = "jmochel@landschneckt.org")
    String getEmailAddress();
    void setEmailAddress(@NotNull @NotBlank @StdEmailAddress String emailAddress);
}