package org.polushin.vars

import org.junit.runner.RunWith
import org.polushin.vars.lexer._
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.io.Source

@RunWith(classOf[JUnitRunner])
class VarsLexerTest extends FunSuite {

  def assertLexemes(input: String, lexemes: Seq[Lexeme]): Unit = {
    val lexer = new VarsLexer(Source.fromString(input))
    lexemes.foreach(lexeme => assert(lexeme == lexer.nextLexeme()))
  }

  test("Equal sign") {
    assertLexemes("=", Seq(Equal, EOF))
  }

  test("Import keyword") {
    assertLexemes("import", Seq(Import, EOF))
  }

  test("Identifier") {
    assertLexemes("identifier", Seq(Identifier("identifier"), EOF))
  }

  test("Number") {
    assertLexemes("1", Seq(Number("1"), EOF))
    assertLexemes("123", Seq(Number("123"), EOF))
  }

  test("Import statement") {
    assertLexemes("import filename", Seq(Import, Identifier("filename"), EOF))
    assertLexemes("  \t  import  \t  filename  \t  ", Seq(Import, Identifier("filename"), EOF))
  }

  test("Equal statement") {
    assertLexemes("var = value", Seq(Identifier("var"), Equal, Identifier("value"), EOF))
    assertLexemes("  \t  var  \t   =  \t  value  \t  ", Seq(Identifier("var"), Equal, Identifier("value"), EOF))
  }

  test("Multiple statements") {
    assertLexemes("import filename \n var = value", Seq(Import, Identifier("filename"),
      NewLine, Identifier("var"), Equal, Identifier("value"), EOF))
  }

}
