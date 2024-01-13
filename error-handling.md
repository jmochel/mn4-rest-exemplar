# Error Handling and Flow Control

## Language

**Operation Result**

Is a disjoint union of Fulfillment or Error

Fulfillment may or may not have a value. 

It contains a (VT value) || (ET errorCode) || (ET errorCode && Throwable thrown)

**Fulfillment** 

An _Operation Result_ that represents a success.  Contains an 

