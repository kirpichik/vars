package org.polushin.vars

import org.junit.runner.RunWith
import org.polushin.vars.lexer._
import org.polushin.vars.parser.{Statement, VarsAssignStatement, VarsImportStatement, VarsParser}
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class VarsParserTest extends FunSuite {

  def assertStatementTypes(lexemes: Seq[Lexeme], types: Seq[Class[_ <: Statement]]): Unit = {
    val parser = new VarsParser(new TestLexer(lexemes), "tests")
    types.foreach(tp => parser.nextStatement() match {
      case Success(value) => value match {
        case Some(statement) => assert(tp == statement.getClass)
        case None => fail("Parser return unexpected EOF")
      }
      case Failure(exception) => fail("Parser throws unexpected exception", exception)
    })
  }

  def assertStatementError(lexemes: Seq[Lexeme]): Unit = {
    val parser = new VarsParser(new TestLexer(lexemes), "tests")
    parser.nextStatement() match {
      case Success(_) => fail("Parser did not throw exception")
      case _ =>
    }
  }

  test("Import statement") {
    assertStatementTypes(
      Seq(Import, Identifier("f"), EOF),
      Seq(classOf[VarsImportStatement])
    )
  }

  test("Assign statement") {
    assertStatementTypes(
      Seq(Identifier("v"), Equal, Number("1"), EOF),
      Seq(classOf[VarsAssignStatement])
    )
    assertStatementTypes(
      Seq(Identifier("v"), Equal, Identifier("f"), EOF),
      Seq(classOf[VarsAssignStatement])
    )
  }

  test("Multiple statements") {
    assertStatementTypes(
      Seq(Import, Identifier("f"), NewLine, Identifier("v"), Equal, Number("1"), EOF),
      Seq(classOf[VarsImportStatement], classOf[VarsAssignStatement])
    )
    assertStatementTypes(
      Seq(NewLine, Import, Identifier("f"), NewLine, NewLine, Identifier("v"), Equal, Number("1"), NewLine, EOF),
      Seq(classOf[VarsImportStatement], classOf[VarsAssignStatement])
    )
  }

  test("No new line between statements") {
    assertStatementError(Seq(Import, Identifier("f"), Import, Identifier("a"), EOF))
    assertStatementError(Seq(Identifier("f"), Equal, Number("1"), Import, Identifier("a"), EOF))
    assertStatementError(Seq(Import, Identifier("f"), Identifier("v"), Equal, Number("1"), EOF))
    assertStatementError(Seq(Identifier("f"), Equal, Number("1"), Identifier("v"), Equal, Number("1"), EOF))
  }

  test("Wrong import statement") {
    assertStatementError(Seq(Import, Equal, EOF))
  }

  test("Wrong assignment statement") {
    assertStatementError(Seq(Identifier("f"), Equal, Equal, EOF))
    assertStatementError(Seq(Identifier("f"), Equal, Import, EOF))
  }

}

private class TestLexer(lexemes: Seq[Lexeme]) extends Lexer {
  private val queue = mutable.Queue(lexemes: _*)

  override def nextLexeme(): Lexeme = queue.dequeue

  override def close(): Unit = {}
}