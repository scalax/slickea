package org.xarcher.ea.macros.jpa

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

/**
 * Created by djx314 on 15-5-16.
 */
class JpaJavaGenerate[T]extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro JpaJavaMacroImpl.impl
}

class JpaJavaMacroImpl(override val c: Context) extends JpaJavaModels {

  import c.universe._

  def impl(annottees: c.Expr[Any]*): c.Expr[Any] = annottees.map(_.tree) match {
    case (classDecl: ClassDef) :: Nil => {
      c.Expr(genCode(classDecl))
    }
    case decl => {
      c.abort(c.enclosingPosition, "Underlying class must not be top-level and without companion")
    }
  }

  protected def genCode(classDef: ClassDef) = {

    val q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents { $self => ..$stats }" = classDef
    columnInfos
    val caseM =
      q"""case class $tpname()"""

    println(caseM)
    caseM

  }

}