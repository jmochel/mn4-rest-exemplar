package org.saltations.mre.feature.places;


import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Interface with the core attributes describing a place.
 */

@Introspected
@Schema(name = "Place", description = "Represents a place's basic info")
public interface Place
{
    @Schema(description = "The name of the place", example = "Boston City Hall")
    String getName();

    void setName(@NotNull @NotBlank @Size(max = 50) String name);

    @Schema(description = "The  address #1 of the place", example = "77 Mass Ave")
    String getStreet1();

    void setStreet1(@NotNull @NotBlank @Size(max = 50) String street1);

    @Schema(description = "The street address #2 of the place", example = "Ste 33")
    String getStreet2();

    void setStreet2(@NotNull @NotBlank @Size(max = 50) String street2);

    @Schema(description = "The city of the place", example = "Boston City Hall")
    String getCity();

    void setCity(@NotNull @NotBlank @Size(max = 50) String city);

    @Schema(description = "The state of the place", example = "MA")
    USState getState();

    void setState(@NotNull @NotBlank @Size(max = 2) USState state);
}
