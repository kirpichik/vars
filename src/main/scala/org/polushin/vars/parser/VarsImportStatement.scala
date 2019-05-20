package org.polushin.vars.parser

import org.polushin.vars.Scope

import scala.util.{Failure, Success, Try}

class VarsImportStatement(factory: ParserFactory, filename: String) extends Statement {

  override def applyScope(scope: Scope): Try[Scope] = factory.parseFile(filename) match {
    case Failure(exception) => Failure(exception)
    case Success(parser) => withResources(parser)(parser => applyParserScope(parser, scope))
  }

  private def applyParserScope(parser: Parser, scope: Scope): Try[Scope] = {
    parser.nextStatement() match {
      case Failure(exception) => Failure(exception)
      case Success(optStatement) => optStatement match {
        case None => Success(scope)
        case Some(statement) => statement.applyScope(scope); applyParserScope(parser, scope)
      }
    }
  }

}
