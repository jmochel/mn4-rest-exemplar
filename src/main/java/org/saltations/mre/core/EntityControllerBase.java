package org.saltations.mre.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.web.router.RouteBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.core.errors.CannotFindEntity;
import org.saltations.mre.core.errors.CannotPatchEntity;
import org.saltations.mre.core.errors.DomainProblemBase;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Base class controller for basic operations for entities of a given type E
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <IC> Interface of the <em>core</em> business concept
 * @param <C> Class of the <em>core object</em>
 * @param <E> Class of the <em>entity</em>
 * @param <ER> Type of the <em>entity repository</em> used by the controller
 * @param <EM> Type of the <em>entity mapper</em> used by the controller
 * @param <ES> Type of the <em>entity service</em>  used by the controller
 */

@Slf4j
public class EntityControllerBase<ID,IC,
        C  extends IC,
        E  extends Entity<ID>,
        ER extends EntityRepoBase<ID,E>,
        EM extends EntityMapper<C,E>,
        ES extends EntityServiceBase<ID,IC,C,E, ER, EM>>
        implements EntityController<ID, IC, C, E, ER, EM, ES>
{
    private final RouteBuilder.UriNamingStrategy uriNaming;

    @Getter
    private final Class<E> entityClass;

    @Getter
    private final ES entityService;

    @Getter
    private final ObjectMapper jacksonMapper;

    public EntityControllerBase(RouteBuilder.UriNamingStrategy uriNaming, Class<E> entityClass, ES entityService)
    {
        this.uriNaming = uriNaming;
        this.entityClass = entityClass;
        this.entityService = entityService;

        this.jacksonMapper = new ObjectMapper();
        this.jacksonMapper.registerModule(new JavaTimeModule());
    }

    @Get("/{id}")
    public Mono<MutableHttpResponse<E>> get(@NotNull ID id)
    {
        E found;

        try
        {
            found = this.entityService.find(id).orElseThrow(() -> new CannotFindEntity(getResourceName(), id));
        }
        catch (DomainProblemBase e)
        {
            throw createThrowableProblem(e);
        }

        return Mono.just(HttpResponse.ok(found));
    }

    @Post
    public Mono<MutableHttpResponse<E>> create(@NotNull @Valid @Body final C toBeCreated)
    {
        E created;

        try
        {
            created = entityService.create(toBeCreated);
        }
        catch (DomainProblemBase e)
        {
            throw createThrowableProblem(e);
        }

        return Mono.just(HttpResponse.ok(created));
    }

    @Put("/{id}")
    public Mono<MutableHttpResponse<?>> replace(@NotNull ID id, @NotNull @Valid @Body E replacement)
    {
        E replaced;

        try
        {
            if (!entityService.exists(id))
            {
                throw new CannotFindEntity(getResourceName(), id);
            }

            replaced = entityService.update(replacement);
        }
        catch (DomainProblemBase e)
        {
            throw createThrowableProblem(e);
        }

        return Mono.just(HttpResponse.ok(replaced));

    }

    @Patch("/{id}")
    public Mono<MutableHttpResponse<?>> patch(@NotNull ID id, @NotNull @NotBlank @Body String mergePatchAsString)
            throws CannotPatchEntity
    {
        E patched;

        try
        {
            if (!entityService.exists(id))
            {
                throw new CannotFindEntity(getResourceName(), id);
            }

            var mergePatch = jacksonMapper.readValue(mergePatchAsString, JsonMergePatch.class);

            patched = entityService.patch(id, mergePatch);
        }
        catch (DomainProblemBase e)
        {
            throw createThrowableProblem(e);
        }
        catch (Exception e)
        {
            throw new CannotPatchEntity(e, getResourceName(), (Long) id);
        }

        return Mono.just(HttpResponse.ok(patched));
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@NotNull ID id)
    {
        try
        {
            entityService.delete(id);
        }
        catch (DomainProblemBase e)
        {
            throw createThrowableProblem(e);
        }

        return HttpResponse.ok();
    }

    @NonNull
    private MutableHttpResponse<E> created(@NonNull E entity) {
        return HttpResponse
                .created(entity)
                .headers(headers -> headers.location(location(entity.getId())));
    }

    private URI location(ID id)
    {
        var base = uriNaming.resolveUri(this.getClass());

        return URI.create(base + "/" + id);
    }

    private URI location(E entity)
    {
        return location(entity.getId());
    }

    public ThrowableProblem createThrowableProblem(@NotNull DomainProblemBase e)
    {
        var builder = Problem.builder()
                .withTitle(e.getTitle())
                .withStatus(e.getStatusType())
                .withDetail(e.getDetail());

        // Add the type

        builder.withType(createType(e));

        // Add the properties

        e.getExtensionPropertiesByName().entrySet().forEach(entry -> builder.with(entry.getKey(), entry.getValue()));

        return builder.build();
    }

    private URI createType(DomainProblemBase e)
    {
        return URI.create("https://localhost/probs/" + e.getClass().getSimpleName().replaceAll("([A-Z]+)([A-Z][a-z])", "$1-$2").replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase());
    }
}