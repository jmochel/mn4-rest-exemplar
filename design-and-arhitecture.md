# Micronaut Exemplar

The Hard Thing : Naming 
========================

# Functional naming

A “FUNCTIONAL NAME” describes an object’s purpose and tends to create a mental image of the object in the operator’s mind

# GUIDELINES:

1. Functional names are brief, simple, and familiar to the operator.
2. Don’t use similar sounding words that can be confused with other equipment.
3. If possible, avoid acronyms.
4. Do not use tradenames, brands, or trademarks. (If a tradename, brand, or trademark is already in use and is clearly understood by the operators, consider it as an exception to this guide.)
5. A functional name for a system or component describes the purpose of the system or component.
6. A functional name for a display or instrumentation describes what is being measured or displayed.
7. A functional name for a control indicates the purpose of the control.
8. Do not use the same name for two separate systems or components within a workplace.
9. In facilities with identical systems or units, use identical functional names with unit or system identifiers (for example three identical systems could be labeled A, B, and C).

In the case of naming, clarity is more important than shortness


A name should clearly without ambiguity indicate what the object is or what a function does
Variables are nouns

* created_at rather than created

Function is an action so the name should contain at least one verb and should allow the code
to read as a sentence

* `write(filename)` means write to file
* `reloadTableData()` means to reload table data
* `isBirthDay()` means checking if today is birth day
* `getAddress()` means getting the address
* `setAddress(address)` means setting an address

Use the same verb for the same action

A function

* Answers a question or provides information
* Changes the state of the object
* Executes a task and returns the result

Make your code short, concise and read as interesting stories


Layers
===========

| | | |
|-|-|-|
|Controller|Service|Repo|
|`POST /resources (Req<Proto>) : Resp<Resource`|`create(Proto): Result<Entity`|`insert(Proto): Result<Entity`|
|`POST /resources (Req<List<Proto>>) : Resp<List<Resource>`|`create(List<Proto>): Result<List<Entity>`|`insert(List<Proto>): Result<List<Entity>`|
|`PUT /resources/1 (Req<Resource>): Resp<Resource`|`replace(Entity): Result<Entity`|`update(Entity): Result<Entity`|
|`PUT /resources (Req<List<Resource>>) : Resp<List<Resource>`|`replace(List<Entity>): Result<List<Entity>`|`update(List<Proto>): Result<List<Entity>`|
|`PATCH /resources/1 (Req<Patch>): Resp<Resource`|`modify(Entity): Result<Entity`|`update(Entity): Result<Entity`|
|`PATCH /resources (Req<List<Patch>>) : Resp<List<Resource>`|`modify(List<Entity>): Result<List<Entity>`|`update(List<Proto>): Result<List<Entity>`|
|`GET /resources/1 (): Resp<Resource`|`find(Id): Result<Entity`|`findById(Id): Result<Entity`|
|`GET /resources/ (List<Id>): Resp<List<Resource>`|`find(List<Id>): Result<List<Entity>`|`find(List<Id>): Result<List<Entity>`|
|`DELETE /resources/1 (): Resp<Void`|`delete(Id): Result<Void`|`delete(Id): Result<Void`|
|`DELETE /resources/ (List<Id>): Resp<List<Resource>`|`delte(List<Id>): Result<List<Void>`|`delete(List<Id>): Result<List<Void>`|
|`GET /resources?fld1=val,fld2<val,fld3>val&sort=+fld1,-fld2&fields=fld1,fld2`|`find(Query): Result<List<Entity>`|`find(Query): Result<List<Entity>`|


REST Nouns, Verbs, protocols 
===========

## Nouns (Resources)
Plurals for Aggregates and Standalone entities

Sub collections for VOs

/users/{userId}/phones
/users/{userId}/phones/1
/users/{userId}/emails

For example, the process of setting up a new customer in a banking domain can be modeled as a resource. CRUD is just a minimal business process applicable to almost any resource. This allows us to model business processes as true resources which can be tracked in their own right.

It is very important to distinguish between resources in REST API and domain entities in a domain driven design. Domain driven design applies to the implementation side of things (including API implementation) while resources in REST API drive the API design and contract. API resource selection should not depend on the underlying domain implementation details

Use of PUT for complex state transitions can lead to synchronous cruddy CRUD


Controller Layer
===========

The controller layer is responsible for

1. Authentication/Authorization of operations
    2. In general permissions look like `app-id.resource-id.permission` so we could have `exemplar.person.read` or `exemplar.person.write`
    3. APIs should stick to component specific permissions without resource extension to avoid the complexity of too many fine grained permissions. For the majority of use cases, restricting access for specific API endpoints using read or write is sufficient.
2. Mapping incoming values such as JSON into the appropriate domain objects
1. Providing the correct REST semantics POST,PUT,PATCH,GET, and DELETE
1. Exposing the domain model in a way that reflects the nouns and verbs of the domain.

The EntityControllerBase provides us with CRUD semantics for most of the domain and CRUD semantics alone are
usually insufficient for a real world application

The default entity controller will expose the following CRUD REST operations


* PUT Replace resource(s)
  * POST /users 
    * BODY single prototype
    * RESP 200 or 201
    * PAYLOAD create resource
  * POST /users (single prototype in body)
    * BODY multiple prototypes
    * RESP 200 or 201
    * PAYLOAD create resources
  * PUT /users/1
    * BODY single resource
    * RESP 200 or 201
    * PAYLOAD updated resource 
  * PUT /users
    * BODY multiple resource
    * RESP 207
    * PAYLOAD multiple updated resource
  * PUT /users/1?name=changed&age=21
    * BODY updated resource
    * RESP 200 or 201
    * PAYLOAD updated resource

* PATCH Modify resource(s) 
    * BODY RFC 7396 Merge Patch
    * RESP 200 or 201
    * PAYLOAD updated resource

* GET  Fetch resource(s)
    * GET /users/1   - Fetch User 1
    * GET /users      - Fetch many

* DELETE Delete a resource
  * DELETE /users/1
    * RESP 200 or 201

* HEAD fetch meta-info as a header
  * HEAD /users

* OPTIONS Fetch all verbs that are allowed for the endpoint
  * OPTIONS /users


### Service Layer

The service layer is responsible for 

  1. Business operations in the language of the business domain.
  2. Transactional boundaries
  3. Providing the business domain semantics 

### Repo Layer

  1. X Count of all
  2. X Insert one
  2. X Insert many
  3. X Update one
  4. X Update many
  5. X Exists by id
  5. X Find one by id
  6. X Find many by ids
  7. Find many by criteria
  7. X Delete by id
  8. Delete by entity
  8. Delete by entities
  9. X Delete by ids
  10. X Delete all

#### Resource Naming 

Use domain language

Use plural form and kebab case i.e. `../shipped-orders/{id}`

Identify sub resources via path segments i.e.  `/resources/{resource-id}/sub-resources/{sub-resource-id}`



Keep URLs verb free. Instead of thinking of actions (verbs), it’s often helpful to think about putting a message in a letter box: e.g., instead of having the verb cancel in the url, think of sending a message to cancel an order to the cancellations letter box on the server side

#### Error Codes 
The following are the common error code returns

* 400 Bad Request Typically due to client error, such as a malformed, request, syntax, invalid, syntax, or invalid request
* 401 Unauthorized – actually Unauthenticated.
* 403 Forbidden – actually unauthorized. User is not authorized to perform this operation on that resource
* 404 Not found – this week we returned on any path for endpoints when it is logically expected, that resource would be returned
* 405 Method not allowed – typically you tried to do something like a delete against the URL. That does not support it.
* 409 Conflict 

#### `POST .../[resource]/` 
* With JSON Body containing all attributes needed to create the resource (No metadata like id)
  * snake_case names for json attributes
* Returns 
  * 200 and instance of the resource
  * ??? 201 , no response payload, link in header
    
#### `PUT ../[resource]/{id}`
* With JSON Body containing all attributes used to replace the resource (including id)
* Returns 200 and updated instance of the resource

#### `PATCH ../[resource]/{id}`
* With JSON Body containing [JSON Merge Patch](https://datatracker.ietf.org/doc/html/rfc7386)
* Content Type of "application/merge-patch+json"
* Returns 200 and updated instance of the resource

#### `GET ../[resource]/{id}`
* With no request payload, only query parameters
* Content Type of "application/json"
* Returns 
  * 200 and updated instance of the resource
  * 404 If resource does not exist

#### `GET ../[resource]?q=xxx&fields=id,name,description&sort=+id,-name
* With no request payload, only query parameters
* Content Type of "application/json"
* Returns
  * 200 and a collection of the resources
  * 200 if collection empty
  * 404 if collection does not exist (for named collections)





## To Do 

* Example of GUIDs as resource ids.
* Add optimistic locking support (PUT)
* Add example of POST returning link in HEADER for created resource [zalando](https://opensource.zalando.com/restful-api-guidelines/#http-requests) 
* Add example of POST of multiple elements returning a 207  [zalando](https://opensource.zalando.com/restful-api-guidelines/#http-requests)
* Add example of Async GET call
* Add example Async POST creation call
* Add example Async PUT call
* Add example Async PATCH call
* Add example PATCH using [JSON Patch](https://tools.ietf.org/html/rfc6902)
* Add example sophisticated query , filter and search
* Add swagger or other does for Standard client and server errors, e.g. 401 (unauthenticated), 403 (unauthorized), 404 (not found), 500 (internal server error), or 503 (service unavailable)
* 

# Micronaut 4.3.0 Documentation

- [User Guide](https://docs.micronaut.io/4.3.0/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.3.0/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.3.0/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Rewrite Gradle Plugin](https://plugins.gradle.org/plugin/org.openrewrite.rewrite)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
## Feature validation documentation

- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)


## Feature micronaut-test-rest-assured documentation

- [Micronaut Micronaut-Test REST-assured documentation](https://micronaut-projects.github.io/micronaut-test/latest/guide/#restAssured)

- [https://rest-assured.io/#docs](https://rest-assured.io/#docs)


## Feature retry documentation

- [Micronaut Retry documentation](https://docs.micronaut.io/latest/guide/#retry)


## Feature problem-json documentation

- [Micronaut Problem JSON documentation](https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/index.html)


## Feature data-jdbc documentation

- [Micronaut Data JDBC documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature lombok documentation

- [Micronaut Project Lombok documentation](https://docs.micronaut.io/latest/guide/index.html#lombok)

- [https://projectlombok.org/features/all](https://projectlombok.org/features/all)


## Feature swagger-ui documentation

- [Micronaut Swagger UI documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://swagger.io/tools/swagger-ui/](https://swagger.io/tools/swagger-ui/)


## Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient)


## Feature testcontainers documentation

- [https://www.testcontainers.org/](https://www.testcontainers.org/)


## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)


## Feature management documentation

- [Micronaut Management documentation](https://docs.micronaut.io/latest/guide/index.html#management)


## Feature reactor documentation

- [Micronaut Reactor documentation](https://micronaut-projects.github.io/micronaut-reactor/snapshot/guide/index.html)


## Feature openrewrite documentation

- [https://docs.openrewrite.org/](https://docs.openrewrite.org/)


## Feature liquibase documentation

- [Micronaut Liquibase Database Migration documentation](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html)

- [https://www.liquibase.org/](https://www.liquibase.org/)


