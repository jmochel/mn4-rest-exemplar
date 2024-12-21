package org.saltations.mre.domain;

import java.time.LocalDate;

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
 * TODO Summary(ends with '.',third person[gets the X, not Get X],do not use @link) CourseCore represents xxx OR CourseCore does xxxx.
 *
 * <p>TODO Description(1 lines sentences,) References generic parameters with {@code <T>} and uses 'b','em', dl, ul, ol tags
 */

@Introspected
@Getter
@Setter
@ToString
@Serdeable(naming = SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "of", buildMethodName = "done", toBuilder = true)
@Schema(name = "CourseCore", description = "Represents a courses basic info", allOf = Course.class)
public class CourseCore implements Course
{
    @NotNull
    @NotBlank
    @Size(max = 50)
    @JsonProperty("name")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 50)})
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 200)
    @JsonProperty("description")
    @Setter(onParam_={@NotNull,@NotBlank,@Size(max = 200)})
    private String description;

    @NotNull
    @JsonProperty("start_date")
    @Setter(onParam_={@NotNull})
    private LocalDate startDate;

    @NotNull
    @JsonProperty("end_date")
    @Setter(onParam_={@NotNull})
    private LocalDate endDate;
}
