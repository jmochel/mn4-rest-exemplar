# Operation Result Pattern (aka Outcomes)

Intention is to identify success or failure of an operation in a compact, and hopefully typesafe representation that allows us to

* Handle both success and failure whenever we want
* Be able to map from one success type to another across data boundaries, ie from domain to presentation
* Transform a success in the called operation to a failure in our current operations
* Transform a failure in the called operation to a success in our current operations
* Be able to map errors from one type to another, perhaps converting a Throwable to something more useful to the presentation layer
* Be able to map from one success type to another across data boundaries, ie from domain to presentation
* Transform a success in the called operation to a failure in our current operations
* Play well with code that uses java Exceptions as part of their flow control.
* Ideally, we’d be able to chain or combine a bunch of these operations together, and only handle the error state at the end of the computation

# Success, Failure and Errors 

Outcomes in apis can be broken up into Successes, Failures, and Error.

* Success - Mechanism worked; We got what we wanted
* Failure -  Mechanism worked; We did not get what we wanted
* Error - Mechanism failed
  * Potentially Recoverable - Network dropped, Server did not answer, etc.
  * Unrecoverable - JVM Ran out of RAM, hardware failed..

If you are not sure if something should be an exception or a failure you could ask yourself “Would the user understand what to do with this failure message?”
Typically, a user does not know what to do with exception messages, they are intended for a technical person to do a more in depth troubleshooting of
the problem. Business failure messages however, could be interpreted by a person and they should be able to act on them.


# We want a model that is

* Usable.
  * Must be easy for developers to do the “right” thing in the face of error
  * Should not impose excessive ceremony in order to write idiomatic code
  * Cognitively familiar to our target audience.
* Performant.
  * The common case needs to be extremely fast. That means as close to zero overhead as possible for success paths.
  * Any added costs for failure paths must be entirely “pay-for-play.”
* Diagnosable.
  * Debugging failures, either interactively or after-the-fact, needs to be productive and easy.
* Composable.
  * At the core, the error model is a programming language feature, sitting at the center of a developer’s expression of code. As such, it had to provide familiar orthogonality and composability with other features of the system. Integrating separately authored components had to be natural, reliable, and predictable.

# Operation Result Pattern

Should provide 

* filter
* recover ?
* map
  * mapSuccess - transforms the value of a successful result.
  * mapError  -  transforms the value of an error result.
* flatMap
  * flatMapSuccess -  transforms a successful result into a different one
  * flatMapError - transforms a failed result into a different one.


Creating Result Objects

* Results.success
* Results.ofNullable creates a new result based on the given possibly-null value.
* Results.ofOptional creates a new result based on the given possibly-empty optional.
* Results.ofCallable creates a new result based on the given possibly-throwing task.
* Results.ofRunnable creates a new result based on the given possibly-throwing task.
* Results.error

Conditional Actions

* ifSuccess performs the given action with a successful result’s value.
* ifFailure performs the given action with a failed result’s value.
* ifSuccessOrElse performs either of the given actions with a result’s value.

Advantages

* It is more explicit than throwing an Exception.
  * Why? Because the operation result type is explicitly specified as the returned value of the method, which makes it pretty obvious compared to knowing what Exception could be thrown by the operation.
* It is faster.
  * Why? because returning an object is faster than throwing an Exception.

Disadvantages

* It is more complex to use than exceptions.
  * Why? Because it must be “manually propagated up the call stack” (AKA returned by the callee and handled by the caller).

Rule of Thumb : If you return Operating Result, never throw an exception. If someting is thrown. Return an Error esult with an exceptional.

Places where it really works

* Functions that perform long-running calculations or operations
* Functions that run business rules or validations
* Functions that call out to unpredictable 3rd parties, like data access or IO

Some cases where you might NOT want to use operation results:

* Functions that are operating on the UI
* Code that does logging, or error reporting
