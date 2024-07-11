# Paper Rock Scissors Exercise

The following is a small exercise to get an idea of your coding and design skills. We would like you to develop a simple interactive game of [Paper Rock Scissors](https://en.wikipedia.org/wiki/Rock_paper_scissors)

It's intentionally not an algorithmically complex problem, so we're looking more at modelling and structure. We are trying to get a feel for how you code in a professional setting.

## Functional Requirements
* The user is presented with a CLI to play the game. 
* A user can enter one of 3 inputs: paper, rock or scissors.
* The computer will choose one input at random.
* The game rules will be applied and a winner is chosen: 
  - Paper beats Rock
  - Rock beats Scissors
  - Scissors beats Paper. 
  - The same input is a tie. 
* The game will repeat until the user explictly chooses to exit.
* On exit a summary is displayed of games won, lost, and tied.

## Non-Functional Requirements
* Create a branch of this repo and submit your solution as a PR. 
* You can use any language you like, but we'd like to see your object oriented design skills, so best to use a language supporting OO. 
* Write your code to the same standard you would professionally (object structure, design patterns, readability, testing/testability, extensibility)
* Write some unit tests for the key pieces of logic. 
* Don't go overboard, this should only take a few hours.

## Design

At a high level, the design tries to match the original game. As such, the system consists of a game and the participants.

There are only two types of participants at the moment:
* `CliPlayer`: a participant that interacts via the CLI. This is the class that represent the human player.
* `RandomComputer`: a computer participant that makes random choices.

The game consists of three components (and some model classes):
* `GameProperties`: contains all properties and rules, such as the number of rounds and the scoring.
* `GameMessenger`: is responsible for sending all game events to the participants.
* `Game`: manages the actual game loop.

The main reason for the division into these components is to improve testability. This is especially true for separating
the `Game` and `GameMessenger`, which makes it a lot easier to test the game interacting with the participants without
having to test the full game loop.

All interactions are synchronous to keep things simple. This seems acceptable since we have only one human player and the computer opponents are fast enough. Since the computer opponents all make random choices, there can also be no cheating.

Finally, all classes that contain business logic rely on dependency injection (inversion of control) where applicable.
This is again to help testability since dependency injection allows for the easy injection of mocks during testing.

## Project/Resources

The project is a basic Gradle/Java project. The following dependencies were added:
* *JUnit5*: framework for unit tests.
* *Mockito*: framework for mocking dependencies.
* *Log4j2*: logging framework. Allows us to easily log without polluting the standard out.
* *Lombok*: added to reduce boilerplate code.

Should the code base get larger, then the following dependencies are worth considering:
* *Guice*: dependency injection framework. Allows us to streamline/automate the dependency injection.
* *Apache Commons*: provides libraries to simplify for example string and collection manipulation.

Test coverage is not 100%. There are two reasons for this:
* We have focused on testing the critical path.
* Some classes are difficult to test due to their dependencies.
  * An example of this is the `CommandLine` class, which relies on the final `Scanner` class.
