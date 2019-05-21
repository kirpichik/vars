package org.polushin.vars

import java.io.FileNotFoundException

import org.junit.runner.RunWith
import org.polushin.vars.parser._
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

@RunWith(classOf[JUnitRunner])
class VarsScopeTest extends FunSuite {

  def assignStmt(name: String, value: String, isIdentifier: Boolean): VarsAssignStatement = {
    val defineInfo = DefineInfo(name, "", 0)
    new VarsAssignStatement(defineInfo, value, isIdentifier)
  }

  def assignNumberStmt(name: String, value: String): VarsAssignStatement = assignStmt(name, value, isIdentifier = false)

  def assignIdentStmt(name: String, value: String): VarsAssignStatement = assignStmt(name, value, isIdentifier = true)

  def importStmt(factory: ParserFactory, importName: String, source: String): VarsImportStatement = {
    new VarsImportStatement(factory, importName, source, 0)
  }

  def assertScope(statements: Seq[Statement], identifiers: Seq[String]): Unit = {
    val scope = new VarsScope
    statements.foreach(_.applyScope(scope, List.empty))

    assert(scope.getDefinitions.toSet == identifiers.toSet)
  }

  test("Numbers definitions") {
    assertScope(
      Seq(
        assignNumberStmt("a", "1"),
        assignNumberStmt("foo", "123")
      ),
      Seq(
        "a",
        "foo"
      ))
  }

  test("Identifier definitions") {
    assertScope(
      Seq(
        assignIdentStmt("a", "1"),
        assignIdentStmt("b", "123"),
        assignIdentStmt("foo", "a"),
        assignIdentStmt("bar", "b")
      ),
      Seq(
        "a",
        "b",
        "foo",
        "bar"
      )
    )
  }

  test("Imports") {
    val factory = new TestParserFactory()
    factory.files =
      Map(
        "file1" -> Seq(
          assignNumberStmt("a", "1"),
          assignIdentStmt("b", "a")
        ),
        "file2" -> Seq(
          importStmt(factory, "file1", "file2"),
          assignIdentStmt("c", "a")
        ),
        "file3" -> Seq(
          assignNumberStmt("d", "1"),
          importStmt(factory, "file2", "file3")
        )
      )

    assertScope(
      Seq(
        importStmt(factory, "file3", "")
      ),
      Seq(
        "a",
        "b",
        "c",
        "d"
      )
    )
  }

  test("Unknown definitions") {
    val scope = new VarsScope
    assignIdentStmt("a", "b").applyScope(scope, List.empty) match {
      case Success(_) => fail("Assignment 'a = b' with empty scope")
      case Failure(_: UnknownIdentifierException) =>
      case Failure(e) => fail("Unknown definitions unknown exception", e)
    }
  }

  test("Definitions conflicts") {
    val scope = new VarsScope
    assignNumberStmt("a", "1").applyScope(scope, List.empty)
    assignNumberStmt("a", "123").applyScope(scope, List.empty) match {
      case Success(_) => fail("Conflict assignments success 'a = 1' and 'a = 123'")
      case Failure(_: ConflictingIdentifiersException) =>
      case Failure(e) => fail("Conflict assignments unknown exception", e)
    }
  }

  test("Import definitions conflicts") {
    val factory = new TestParserFactory()
    factory.files =
      Map(
        "file1" -> Seq(
          assignNumberStmt("a", "1")
        ),
        "file2" -> Seq(
          assignNumberStmt("a", "123")
        ),
        "file3" -> Seq(
          importStmt(factory, "file1", "file3"),
          importStmt(factory, "file2", "file3")
        )
      )

    val scope = new VarsScope
    importStmt(factory, "file3", "").applyScope(scope, List.empty) match {
      case Success(_) => fail("Definitions conflict in different imports success")
      case Failure(_: ConflictingIdentifiersException) =>
      case Failure(e) => fail("Definitions conflict unknown exception", e)
    }
  }

  test("Cyclic imports") {
    val factory = new TestParserFactory()
    factory.files =
      Map(
        "file1" -> Seq(
          importStmt(factory, "file2", "file1")
        ),
        "file2" -> Seq(
          importStmt(factory, "file3", "file2")
        ),
        "file3" -> Seq(
          importStmt(factory, "file1", "file3")
        )
      )

    val scope = new VarsScope
    importStmt(factory, "file1", "").applyScope(scope, List.empty) match {
      case Success(_) => fail("Cyclic import success")
      case Failure(_: CyclicImportException) =>
      case Failure(e) => fail("Cyclic import unknown exception", e)
    }
  }

  private class TestParser(statements: Seq[Statement]) extends Parser {

    private val queue = mutable.Queue(statements: _*)

    override def nextStatement(): Try[Option[Statement]] = Try {
      if (queue.isEmpty)
        None
      else
        Some(queue.dequeue())
    }

    override def close(): Unit = {}
  }

  private class TestParserFactory extends ParserFactory {

    var files: Map[String, Seq[Statement]] = Map()

    override def parseFile(filename: String): Try[Parser] = files.get(filename) match {
      case Some(file) => Success(new TestParser(file))
      case None => Failure(new FileNotFoundException(filename))
    }

  }

}
