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
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.core.errors.CannotFindEntity;
import org.saltations.mre.core.errors.CannotPatchEntity;
import org.saltations.mre.core.errors.DomainProblemBase;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import reactor.core.publisher.Mono;

import java.net.URI;


/**
 * Generic controller for processing entities; An object uniquely identified by a single identity attribute
 *
 * @param <ID> Type of the unique identifier for the entity E.
 * @param <IC> Core interface that describes the business item
 * @param <C> Class of the business item
 * @param <E> Class of the persistable business item entity. Contains all the same data as C but supports additional
 *           entity specific meta-data.
 * @param <R> Type of the entity repository used by the service
 * @param <ES> Type of the entity service
 */

@Slf4j
public class EntityController<ID,IC, C extends IC,E extends Entity<ID>, R extends EntityRepo<ID,E>, M extends EntityMapper<ID,C,E>, ES extends EntityService<ID,IC,C,E,R,M>>
{
    private final RouteBuilder.UriNamingStrategy uriNaming;

    private final Class<E> clazz;

    protected final ES service;

    private final ObjectMapper jacksonMapper;

    public EntityController(RouteBuilder.UriNamingStrategy uriNaming, Class<E> clazz, ES service)
    {
        this.uriNaming = uriNaming;
        this.clazz = clazz;
        this.service = service;

        this.jacksonMapper = new ObjectMapper();
        this.jacksonMapper.registerModule(new JavaTimeModule());
    }

    /**
     * The name of the entity class
     */

    protected String resourceName()
    {
        return clazz.getSimpleName();
    }

    @Get("/{id}")
    public Mono<MutableHttpResponse<E>> get(@NotNull ID id)
    {
        E found;

        try
        {
            found = this.service.find(id).orElseThrow(() -> new CannotFindEntity(resourceName(), id));
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
            created = service.create(toBeCreated);
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
            if (!service.exists(id))
            {
                throw new CannotFindEntity(resourceName(), id);
            }

            replaced = service.update(replacement);
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
            if (!service.exists(id))
            {
                throw new CannotFindEntity(resourceName(), id);
            }

            var mergePatch = jacksonMapper.readValue(mergePatchAsString, JsonMergePatch.class);

            patched = service.patch(id, mergePatch);
        }
        catch (DomainProblemBase e)
        {
            throw createThrowableProblem(e);
        }
        catch (Exception e)
        {
            throw new CannotPatchEntity(e, resourceName(), (Long) id);
        }

        return Mono.just(HttpResponse.ok(patched));
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@NotNull ID id)
    {
        try
        {
            service.delete(id);
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

        e.getProperties().entrySet().forEach( entry -> builder.with(entry.getKey(), entry.getValue()));

        return builder.build();
    }

    private URI createType(DomainProblemBase e)
    {
        return URI.create("https://localhost/probs/" + e.getClass().getSimpleName().replaceAll("([A-Z]+)([A-Z][a-z])", "$1-$2").replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase());
    }
}