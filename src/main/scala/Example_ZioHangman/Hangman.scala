//package ZioHangmanExample
//
//import zio._
//import java.io.IOException
//
///**
// *
// */
//object Hangman extends ZIOAppDefault {
//
//	// TODO - why replace the putstrln / getstrln (page 19-20 in Jorge Vasquez) with readLine???
//	def getUserInput(message: String): IO[IOException, String] = Console.readLine(message)
//
//
//	lazy val getGuess: IO[IOException, Guess] =
//		for {
//			input <- getUserInput("What's your next guess? ")
//			guess <- ZIO.from(Guess.make(input)) <>
//						(Console.printLine("Invalid input. Please try again...") <*> getGuess)
//		}
//
//	lazy val getName: IO[IOException, Name] =
//		for {
//			input <- getUserInput("What's your name? ")
//			name <- ZIO.from(Name.make(input)) <> (Console.printLine("Invalid input. Try again") <*> getName)
//		} yield name
//
//	lazy val chooseWord: UIO[Word] =
//		for {
//			index <- Random.nextIntBounded(words.length)
//			word <- ZIO.from(words.lift(index).flatMap(Word.make)).orDieWith(_ => new Error("Boom!"))
//		}
//
//	// IO[+E, +A] = ZIO[Any, E, A]
//	// meaning: environment == Any, fail with error == E, succeed with type == A
//	val run: IO[IOException, Unit] =
//		for {
//			name <- Console.printLine("Welcome to ZIO Hangman!") <*> getName
//			word <- chooseWord
//			_ <- gameLoop(State.initial(name, word))
//		} yield ()
//}
