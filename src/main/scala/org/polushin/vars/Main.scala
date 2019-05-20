package org.polushin.vars

import org.polushin.vars.parser.{VarsImportStatement, VarsParserFactory}

import scala.util.{Failure, Success}

object Main {

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Required argument: <filename>")
      System.exit(1)
    }

    val initialFile = new VarsImportStatement(new VarsParserFactory, args(0), "", 0)
    initialFile.applyScope(new VarsScope, List.empty) match {
      case Failure(exception) => println(exception.getMessage); System.exit(2)
      case Success(scope) =>
        println("Variables:")
        scope.getDefinitions.foreach(println)
    }
  }

}
