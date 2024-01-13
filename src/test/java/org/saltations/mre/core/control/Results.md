# Results, OK Results and Error Results

# Success, Failure and Errors

Outcomes in apis can be broken up into Successes, Failures, and Error.

* Success - Mechanism worked; We got what we wanted
* Failure -  Mechanism worked; We did not get what we wanted
* Error - Mechanism failed
  * Potentially Recoverable - Network dropped, Server didn’t answer , etc..
  * Unrecoverable - JVM Ran out of RAM, hardware failed..

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

Return a Union of an OK Result (success or failure without error) OR an error result.

OK, fulfilled.

accomplishment
achievement
realization
success
attainment
consummation
actualization
fruition

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