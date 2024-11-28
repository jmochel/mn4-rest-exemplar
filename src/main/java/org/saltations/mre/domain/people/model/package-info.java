/**
 * Entities (standalone or as aggregate roots), Value Objects, DTOs, and Services (Svc) that are specific to the persons feature.
 * Examples include:
 * <dl>
 *     <dt>Person</dt><dd>Common interface for a {@code Person DTO}  and {@code PersonEntity} </dd>
 *     <dt>PersonCore</dt><dd>Basic DTO of a {@code Person}  which includes validation annotations</dd>
 *     <dt>PersonEntity</dt><dd>Entity Which includes all of the data of {@code PersonCore} as well as a unique identifier and other metadata describing state and the lifecycle of the entity</dd>
 *     <dt>CreatePersonSvc</dt><dd>Implementation of {@code CreatePersonSvc} which talks to the {@code PersonRepo}</dd>
 * </dl>
 */

package org.saltations.mre.domain.people.model;
