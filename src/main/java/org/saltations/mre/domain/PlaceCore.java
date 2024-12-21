package org.saltations.mre.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Core object for a Place
 */

@Introspected
@Getter
@Setter
@ToString
@Serdeable(naming = SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "of", buildMethodName = "done", toBuilder = true)
@Schema(name = "PlaceCore", description = "Represents a place's basic info", allOf = Place.class)
public class PlaceCore implements Place
{
    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("name")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 50)})
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("street1")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 50)})
    private String street1;

    @Size(max = 50)
    @JsonProperty("street2")
    @Setter(onParam_={@Size(max = 50)})
    private String street2;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("city")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 50)})
    private String city;

    @NotNull
    @JsonProperty("state")
    @Setter(onParam_={@NotNull})
    private USState state;

}
