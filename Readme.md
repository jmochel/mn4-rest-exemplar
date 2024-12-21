Structure and Design 
---------------------

# 0. Problem Domain

## Events

* find person
* add person
* add person to course as student
* add person to course as auditing
* add person to court as instructor
* add point of contact to person
* remove point of contact from person
* get current class list
* remove person from course
* change person from auditor to student
* change person from student to order auditor
* add course
* remove course

## Date Model: Entities and VOs

Each course is an entity, and it is composed of one of more sessions. Each curse, of course, has associated with it. People who are participating in the course, and each session has associated with the people who participated in the course. Each person has is composed of a set of points of contact.


## Naming 

* IXxxx - Interface (I explicitly name interfaces with  an 'I' prefix) simply because, as ugly as it is, it does allow for quick identification.
* 



# 1. Modeling

## References

https://deviq.com/principles/tell-dont-ask
https://www.thoughtworks.com/en-us/insights/blog/agile-project-management/domain-modeling-what-you-need-to-know-before-coding
https://medium.com/@aboutcoding/rich-domain-models-22f176ad6f1b
https://java-design-patterns.com/patterns/domain-model/




# 2. Structure (Outside In)

## References

Synthesis of Clean Architecture and Domain Driven Design

[Comments by Uncle Bob](https://groups.google.com/g/clean-code-discussion/c/oEFEWq8qdFQ/m/i0gsi3eU5VoJ)

There are several architectures that have similar characteristics. 

* DDD (Domain Driven Design) - Eric Evans
* Hexagonal Architecture - Steve Freeman and Nat Pryce.
  * Ports and Adapters Architecture
  * Synthesised on Onion Architecture - Jeffrey Palermo
* DCI (Data Context Interaction) - Trygve Reenskaug
* Clean Architecture - Uncle Bob 
  * Synthesised on Hexagonal Architecture, Onion Architecture, and several other models. Including BCE (Boundary Control Entity) - Ivar Jacobson

|                               | Clean                    | Hexagonal            | DDD                             | DCI                                                         ||
|-------------------------------|--------------------------|----------------------|---------------------------------|-------------------------------------------------------------|---|
| **Seperations**               | UI, DB, Use Case, Domain | UI, Mid-tier, Domain | Domain Model, Commands,Queries. | Models (Domain objects), Interactions (Use cases), Contexts |
| **Core Technical Mechanisms** | DI (poly and interfaces) | DI (emphasize mocks) | Unspecified (for the most part) | inheritance and traits                                                   |


Clean DDD is an approach to modeling that combines DDD with Clean Architecture. This is not the one true way. It is a decent way.

[Looking at Clean with DDD](https://medium.com/unil-ci-software-engineering/clean-domain-driven-design-2236f5430a05)
[Canon DDD vs  Clean DDD](https://medium.com/unil-ci-software-engineering/comparing-canonical-ddd-and-clean-ddd-34e807f4e336)

https://tbuss.de/posts/2023/9-how-to-do-the-package-structure-in-a-ports-and-adapter-architecture/
https://github.com/citerus/dddsample-core
https://medium.com/unil-ci-software-engineering/clean-domain-driven-design-2236f5430a05

https://wkrzywiec.medium.com/ports-adapters-architecture-on-example-19cab9e93be7
https://www.oreilly.com/library/view/functional-and-reactive/9781617292736/OEBPS/Text/Chapter_1.xhtml

https://medium.com/unil-ci-software-engineering/enchilada-pattern-by-uncle-bob-0e5fb6b3c5a8
https://wkrzywiec.medium.com/ports-adapters-architecture-on-example-19cab9e93be7

## REST controllers, Message Consumers

### REST Controllers

Should always ensure that the domain model and the domain business logic are not leaked _as-is_ to the interfaces.

* So what should you expose in the REST controller?
* 

How should you group what you expose in the REST controller?

* Group/Namespacing of APIs - Teams/groups/business units generally group APIs together and make the grouping evident in the API URI
* Bounded Context  `<major_version>/<bounded_context_name>)` - For example, `v1/ payments`
* API Product Boundary using
  * Aggregate
  * Service

How should you name what you expose in the REST controller?




### Message Consumers


## Layering/Packaging

* Does your clean architecture based app needs a separate application layer?
    * _No_ if it is basically managing CRUD operations, then it is not necessary. A controller could call the repository directly without losing maintainability.
    * _Maybe Not_ if there is business logic but it is handled inn the domain model;, then it may not be necessary. The domain model could be called directly from the controller.
    * _Defintely_ if it goes outside the normal CRUD operations AND the business logic is not in the domain model. This is where the application layer comes in. It is the orchestrator of the domain model and the data access layer. 
* Is it OK to have a Mapper being used in the Controller ?
    * It is a stretch. If it is a simple mapping to and from the incoming request or response, then it is OK. But if it is a complex mapping, then it should be in the application layer.
* Consider having the crud operations in a CRUD Controller (just uses repo)  and the business logic in a Business Controller (uses services or use cases).

# Micronaut Exemplar




For review
------------
[Project Structure](https://wkrzywiec.medium.com/ports-adapters-architecture-on-example-19cab9e93be7)
[Project Structure](https://medium.com/unil-ci-software-engineering/clean-ddd-lessons-project-structure-and-naming-conventions-00d0b9c57610)
[Onion Architecture](https://www.infoq.com/articles/architecture-onion-architecture/)


[Gradle Test Suites](https://blog.gradle.org/introducing-test-suites)

[TOML Cheatsheet](https://quickref.me/toml.html)

[Night Config](https://github.com/TheElectronWill/night-config/wiki/Configurations)

[Try Paradigm](https://blog.softwaremill.com/exceptions-no-just-try-them-off-497984eb470d)


Design Building Blocks (DDD)
----------------------------
In the descriptions below domains and subdomains and bounded contexts are groupings of the set of non-mutually exclusive, arbitrary concepts in the universe of conversations about
the business or the solutions that enable the operations of the business

**Domain**
: Logical grouping _(defined in the problem space)_ of the set of concepts invoked by the business domain.
Typically expressed as _the_ problem the business is trying to solve.

> Fixing patients' teeth is the domain of a dentist.

**Subdomain**
: Logical area within a domain that represents _a specific_ problem the business is trying to solve.

> For a dentist, a subdomain would be making appointments for patients

**Bounded Context**
: Logical boundary _(typically `languaged` in the solution space)_ around/or within the subdomain defined by the language of the responsibilities within that context.
: A bounded context is a specific area within a domain/subdomain where a specific set of concepts, terms, and rules apply, and where a specific language is used to communicate.
In other words, a bounded context is a boundary within which a domain model is defined and applied.
It represents a specific area of the domain where a specific team, department, or business unit has a clear and distinct responsibility. Each bounded context has its own models, entities, and operations,
and may have different interpretations of the same concepts compared to other bounded contexts within the same domain/subdomain.

> For a dentist, some bounded contexts within the subdomain of billing
> could be patients accounts receivable and another
> could be insurance billing

**Entity**
: A trackable object in the domain that is defined by its identity.

**Value Object**
: An object That has no identity and is defined by its attributes

**Domain Event**
: An event that represents a change in the domain

**Aggregate/Aggregate Root**
: A cluster of domain objects (entities and value objects) that can be treated as a single unit based on rules for
consistency. It typically encapsulates the group of objects and manages their behavior. Access to internal objects is
typically only possible through the aggregate route in order to ensure the integrity and consistency of the data.

**Service**
: An object that perform a specific task for the application. What distinguishes the different types of services are the roles and the language.

Questions to ask are:
* If we remove this, will it affect the execution of my domain model? If yes, it is Probably a domain service
* If we remove this, will it affect the execution of my application? If yes, it is Probably an infrastructure service
* If we remove this, will the customer be able to talk to us? If no, it is an application service.


**Infrastructure Service**
: An object that embodies (typically as verbs) activities that fulfill the infrastructure concerns, such as sending emails and logging meaningful data

A good example is a notification service implementation. An infrastructure service does not make business decisions. In the domain layer,
you define an interface with actions we want to have such as `sendEmailAboutLoan` and in the service we implement it.

Crosscutting concerns that often involve infrastructure services

* [ ] Logging
* [ ] Configuration
* [ ] Security
* [ ] Monitoring
* [ ] Tracing
* [ ] Resilience

**Domain Service**
:  A stateless object that embodies (typically as verbs) and/or operates upon domain concepts. Common examples of domain services are `Use Cases`

Domain services tend to be very granular. They contain domain logic (business decisions) that can't naturally be placed in
an entity or value object. Domain service methods can have other domain elements as parameters and return values. Domain service
implementations can also have infrastructure services as parameters or part of their construction.  Domain services should be used
cautiously as they can lead to an anemic domain model.

> When a significant process or verb in the domain is not the natural responsibility of an Aggregate, Entity or Value Object
> add an operation to the model as a standalone interface declared as a Service. Define the interface in terms of the language
> of the model and make sure the operation name is part of the ubiquitous language. Make the Service stateless.

**Application Service**
: A service that provides a hosting environment for the execution of domain logic and serves as a gateway to expose the
functionality of the domain to clients as an API.

* Operates on scalar types or DTOs, transforming them into domain types.
* Application service objects are "command" objects (GET, PUT, etc..)
* Typically responsible for fetching input data from outside of the domain
* Returns information about a result of the action, listens for an answer and decides if an event or  message should be sent.
* Application services declared dependencies on infrastructure services required to execute domain logic.

**Factory**
: A domain service that handles the beginning of a domain object's life. Answers the question: How do I make this?

> Whenever there is **exposed complexity** in creating or reconstituting an object from another medium, the factory is a likely option

**Repository**
: A domain service that handles the persistence of domain objects. Answers the question: How do I store and retrieve this?

This really is a domain service. The interface is defined within the domain.

**Controller**
: An application service that handles HTTP requests and responses.

**Presenters**
: An object that formats data for the view

You can notice a pattern in most code bases that adhere to such a guideline. Their execution flow goes as follows:

Prepare all information needed for a business operation: load participating entities from the database and retrieve any required data from other external sources.

Execute the operation. The operation consists of one or more business decisions made by the domain model. Those decisions result in either changing the model’s state, generating some artifacts (amountWithCommission value in the sample above), or both.

Apply the results of the operation to the outside world.


The question to ask is - is sending an email an important domain concept? Is Email an entity in your domain? If so, you may have an interface for an email sender defined in your domain layer, with a separated implementation in the infrastructure layer,



Project structure
-----------------
A mixture of package by feature and package by layer

Pros

* Higher modularity;
* Easier code navigation;
* Higher level of abstraction;
* Separates both features and layers;
* More readable and maintainable structure;
* More cohesion;
* Much easier to scale;
* Less chance to accidentally modify unrelated classes or files;
* Much easier to add or remove application features;
* And much more reusable modules.



```text
/src/../java/main/../
    /domain
        /model
            EntityBase and/or IEntity
            FactoryBase and/or IFactory
            RepositoryBase and/or IRepository
            MapperBase and/or IMapper
        /service
            ServiceBase and/or IService
            UseCaseBase and/or IUseCase
        /event
            EventBase and/or IEvent
    /layer
        /controller
        /presenter            
    /feature
        /feature1
            /model
                Feature1Entity
                Feature1Factory
                Feature1Repository
            Feature1Service
            Feature1Factory
            Feature1Event1
            Feature1Event2
            Feature1Controller
            Feature1UseCase1
            Feature1UseCase2       
```

Functional Requirements
=======================

Acceptance Criteria
-------------------
Acceptance Criteria are a way of describing your functional requirements in a Given/When/Then format.

1. Directly translate into Given/When/Then format:
  * When: A candidate Requests an ability to be assessed
  * Then: The candidates ability is assessed
2. Scrutinize the pre-conditions - After this first round of interrogation, this will immediately flesh out the happy path to look like this.
  * Given: There is an expert to assess Ability ‘A’
  * And: There is a pre-registered candidate ‘123’
  * When: Candidate ‘123’ requests to be assessed in Ability ‘A’
  * Then: He is assessed by the ‘expert’ randomly generating a number between 0–10
    These then fan out to additional ACs
3. Scrutinize the outcomes
4. Bring it to the team
5. Estimation
6. Move to your implementation board.


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


CA Layers
From the inside to he outside

Entities encapsulate Enterprise wide business rules and data

Use Case this layer contains application specific business rules

Interface Adapters The software in this layer is a set of adapters that convert data from the format most convenient for the use cases and entities,
to the format most convenient for some external agency such as the Database or the Web

Frameworks and Drivers.composed of frameworks and tools such as the Database, the Web Framework, etc.
Generally you don’t write much code in this layer other than glue code that communicates to the next circle inwards

Source code dependencies always point inwards. As you move inwards the level of abstraction increases. The outermost circle is low level concrete detail.
As you move inwards the software grows more abstract, and encapsulates higher level policies. The innermost circle is the most general

What gets composed into this
----------------------------

Advantages of Proper Architecture
* Testable
* Maintainable
* Changeable
* Easy to Develop
* Easy to Deploy
* Independent



Key elements of the Clean Architecture are: Separation of concerns, Dependency Rule, and Boundaries
* Each part of the application should be independent of each other
* The Dependency Rule: Source code dependencies must point inwards only
* Boundaries: The software is separated into layers with each layer having a specific responsibility

* Entities - least subject to changes when the application evolves
* Use Cases - contains application specific business rules
* Interface Adapters - contains the adapters that convert data from the format most convenient for the use cases and entities, to the format most convenient for some external agency such as the Database or the Web

Controllers, Presenters, Views, and Gateways are all examples of Interface Adapters.
They are the input and output mechanisms of the use cases and entities.

Controller is an adapter that converts HTTP requests and responses to method calls, and vice versa.



```text
/com
  /exemplar
    /core
    /infrastructure
    
        /app
            App.java
        /feature
            /feature1
                Feature1Controller.java
                Feature1Service.java
                Feature1Repo.java
            /feature2
                Feature2Controller.java
                Feature2Service.java
                Feature2Repo.java
    /app
        App.java
    /feature
        /feature1
            Feature1Controller.java
            Feature1Service.java
            Feature1Repo.java
        /feature2
            Feature2Controller.java
            Feature2Service.java
            Feature2Repo.java
```

Clean Architecture Structure

```text
/src
    /main
        /java
          /com
            /exemplar
                /somapp
                    /core   -- hexagon inside. Should not depend on infrastructure
                        /model
                            /person -- aggregate root
                            /place  -- aggregate root
                        /port
                        /usecase    
                    /infrastructure -- hexagon outside
```

CRUD Layers
===========

|                                                                               |                                              |                                            |
|-------------------------------------------------------------------------------|----------------------------------------------|--------------------------------------------|
| **Controller** (Isolated Test and Integration Tests)                          | **Service** (Isolated Tests)                     | **Repo** (Live Test)                           |
| `POST /resources (Req<Proto>) : Resp<Resource`                                | `create(Proto): Result<Entity`               | `insert(Proto): Result<Entity`             |
| `POST /resources (Req<List<Proto>>) : Resp<List<Resource>`                    | `create(List<Proto>): Result<List<Entity>`   | `insert(List<Proto>): Result<List<Entity>` |
| `PUT /resources/1 (Req<Resource>): Resp<Resource`                             | `replace(Entity): Result<Entity`             | `update(Entity): Result<Entity`            |
| `PUT /resources (Req<List<Resource>>) : Resp<List<Resource>`                  | `replace(List<Entity>): Result<List<Entity>` | `update(List<Proto>): Result<List<Entity>` |
| `PATCH /resources/1 (Req<Patch>): Resp<Resource`                              | `modify(Entity): Result<Entity`              | `update(Entity): Result<Entity`            |
| `PATCH /resources (Req<List<Patch>>) : Resp<List<Resource>`                   | `modify(List<Entity>): Result<List<Entity>`  | `update(List<Proto>): Result<List<Entity>` |
| `GET /resources/1 (): Resp<Resource`                                          | `find(Id): Result<Entity`                    | `findById(Id): Result<Entity`              |
| `GET /resources/ (List<Id>): Resp<List<Resource>`                             | `find(List<Id>): Result<List<Entity>`        | `find(List<Id>): Result<List<Entity>`      |
| `DELETE /resources/1 (): Resp<Void`                                           | `delete(Id): Result<Void`                    | `delete(Id): Result<Void`                  |
| `DELETE /resources/ (List<Id>): Resp<List<Resource>`                          | `delte(List<Id>): Result<List<Void>`         | `delete(List<Id>): Result<List<Void>`      |
| `GET /resources?fld1=val,fld2<val,fld3>val&sort=+fld1,-fld2&fields=fld1,fld2` | `find(Query): Result<List<Entity>`           | `find(Query): Result<List<Entity>`         |


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
  * Examples of POST and POLL
  * POST and Callback
  *
* Add example Async PUT call
* Add example Async PATCH call
* Add example PATCH using [JSON Patch](https://tools.ietf.org/html/rfc6902)
* Add example sophisticated query , filter and search
* Add swagger or other does for Standard client and server errors, e.g. 401 (unauthenticated), 403 (unauthorized), 404 (not found), 500 (internal server error), or 503 (service unavailable)
* Add example 7807 problem display controller , including languages
*
    
### 2. Error handling

References

https://gaetanopiazzolla.github.io/java/2023/03/05/java-exception-patterns.html
https://www.freecodecamp.org/news/write-better-java-code-pattern-matching-sealed-classes/
https://softwaremill.com/functional-error-handling-with-java-17/
https://softwareengineering.stackexchange.com/questions/339088/pattern-for-a-method-call-outcome

https://github.com/armtuk/java-functional-adapter/blob/develop/src/main/java/com/plexq/functional/Success.java
