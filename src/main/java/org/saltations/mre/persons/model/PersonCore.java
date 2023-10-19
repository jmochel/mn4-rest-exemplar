package org.saltations.mre.persons.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.saltations.mre.core.annotations.StdEmailAddress;

/**
 * Represents the core attributes of a Person without any tracking metadata
 * <p>
 * Used as input for REST (create/patch/replace) operations
 * </p>
 */

@Introspected
@Getter
@Setter
@ToString
@Serdeable(naming = SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "of", buildMethodName = "done", toBuilder = true)
public class PersonCore implements Person
{
    @NotNull
    @Min(value = 12L)
    @Setter(onParam_={@NotNull,@Min(value = 12L)})
    private Integer age;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("first_name")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 50)})
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("last_name")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 50)})
    private String lastName;

    @NotNull
    @NotBlank
    @StdEmailAddress
    @JsonProperty("email_address")
    @Setter(onParam_={@NotNull,@NotBlank,@StdEmailAddress})
    private String emailAddress;
}
