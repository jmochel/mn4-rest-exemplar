Structure and Design 
---------------------

# 1. Domain Model

## References

https://deviq.com/principles/tell-dont-ask
https://www.thoughtworks.com/en-us/insights/blog/agile-project-management/domain-modeling-what-you-need-to-know-before-coding
https://medium.com/@aboutcoding/rich-domain-models-22f176ad6f1b
https://java-design-patterns.com/patterns/domain-model/

# 2. Structure (Outside In)

## References

https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)
https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html
https://tbuss.de/posts/2023/9-how-to-do-the-package-structure-in-a-ports-and-adapter-architecture/
https://github.com/citerus/dddsample-core
https://medium.com/unil-ci-software-engineering/clean-domain-driven-design-2236f5430a05
https://medium.com/unil-ci-software-engineering/comparing-canonical-ddd-and-clean-ddd-34e807f4e336
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


    
### 2. Error handling

References

https://gaetanopiazzolla.github.io/java/2023/03/05/java-exception-patterns.html
https://www.freecodecamp.org/news/write-better-java-code-pattern-matching-sealed-classes/
https://softwaremill.com/functional-error-handling-with-java-17/
https://softwareengineering.stackexchange.com/questions/339088/pattern-for-a-method-call-outcome

https://github.com/armtuk/java-functional-adapter/blob/develop/src/main/java/com/plexq/functional/Success.java
