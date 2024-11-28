/**
 * This package contains the domains (aka Sub-domains or bounded contexts) of the application.
 * Each domain is a package that contains the following:
 * <h4>The Model</h4>
 * <p>
 * Representations of the domain's relatively rich business model. Includes aggregates, entities, and value objects of the domain.
 * This portion of the model encapsulates enterprise wide business rules. This portion of the model could be used by many different applications in the enterprise.
 * You would not expect any of this code to be impacted by changes to page navigation or security. No operational changed to any particular application should affect the model
 * A particular subdomain's model may depend on shared core {@link org.saltations.mre.common.core} classes or shared model {@link org.saltations.mre.common.domain} classes, but nothing else.
 *
 * <h4>Use Cases</h4>
 * <p>
 * Application Specific Business logic that cannot be contained in the model classes.
 * This is the code that is particular to how the applications manages the flow of data between the user and the model.
 * This code is likely to change when the application changes its business rules such as one of the requirements for enrolling a participant in a course.
 * A particular subdomain's use cases may depend on
 * <dl>
 *     <dt>Shared core</dt><dd>{@link org.saltations.mre.common.core} classes </dd>
 *     <dt>Shared model</dt><dd>{@link org.saltations.mre.common.domain.model} classes</dd>
 *     <dt>Shared usecase</dt><dd>{@link org.saltations.mre.common.domain.logic} classes</dd>
 *     <dt>Shared domain</dt><dd>{@link org.saltations.mre.common.domain} classes</dd>
 *     <dt>Other sub-domains model</dt><dd>i.e. Enrollment domain use cases could use both Student and Course domain models</dd>
 * </dl>
 *
 * <h4>Domain Specific Adapters</h4>
 * <p>
 * These are the classes that allow the domain to interact with the outside world. This includes classes such as Mappers and Controllers For that domain
 * A particular subdomain's adapters may depend on...
 *
 */

package org.saltations.mre.domain;
