# The Elevator Exercise

Resolution for Elevator Exercise

## Getting Started

### Prerequisites

1. Java 8
2. Maven 3

### Introduction

Implementation of elevator exercise proposed for an interview.
This solution was designed considering OOP concepts and some common patterns like strategy and observer. 

## Main Entities
- Elevator
- ElevatorState
- ElevatorRequest
- Floor
- Cabin
- Building
- KeyCardRequest

## Main Components

- *Managers*: Thread for handling new requests. Having this components will allow us to use different managers, thus we can use different strategies for attending new incoming requests. 
Currently we have an implementation that is using a direction based strategy.  But a based time strategy can be implemented later using same base components

- *Handlers*: Component for processing elevator requests. Having this components will allow us to implement different handlers. In this version we have 2 different implementation for handlers:
   * ElevatorSimpleRequestHandler: Handler without floors access restrictions 
   * ElevatorKeyCardRequestHandler: Handler for interacting with keycard access system for floors with access restriction

## Scope

In order to simplify the exercise and deliver an exercise that adds value in first version, I have done some simplifications that can be completed in next iterations

1. IoC container is not included in this version, so injections are being done manually by constructors

2. Factories for creating different managers, handlers and authentication method need to be implemented.

3. KeyCard Reader was implemented using a simple and dummy mechanism, ideally this should be integrated with a real card reader system provided by the keycard reader provider
   - Requires manual user input  

4. KeyCard Access system was implemented using a simple and dummy mechanism
   - Allowed keys are 902fbdd2b1df0c4f70b4a5d23525e932, c4fd1ef4041f00039def6df0331841de, d8c072a439c55274f145eae9f6583751 

5. Logs are being done using primitive java console messages

6. Error Handling need to be improved


## Running the tests
```
mvn clean test
```

### Demo
For Running a non-interactive demo you can do following steps
```
mvn clean compile assembly:single
```

And execute jar

```
java -jar target/elevator-1.0.0-jar-with-dependencies.jar
```
