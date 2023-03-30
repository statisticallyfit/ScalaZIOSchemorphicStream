package util



import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._


/**
 *
 */
object Util {

	// NOTE: treating as block / pkg name the string of letters separated by the 'separators' = the underscore or dot
	def extractLongestRunOfLetterOrDot(accChars: List[Char],
								traversalList: List[Char],
								indexLeftOff: Int = 0,
								separators: Seq[Char] = Seq('.', '_')):(List[Char], Int)= {

		if (traversalList.isEmpty) return (accChars, indexLeftOff)

		else if(traversalList.head.isLetterOrDigit || separators.contains(traversalList.head) ) {
			extractLongestRunOfLetterOrDot(accChars :+ traversalList.head, traversalList.tail , indexLeftOff + 1)
		} else { //if (! (traversalList.head.isLetterOrDigit || traversalList.head == '.')){
			return (accChars, indexLeftOff + 1)
		}
	}

	def extractContinue(accRuns: List[String], traversalList: List[Char]): List[String] = {
		if(traversalList.isEmpty) return accRuns

		val (newAcc, idx) = extractLongestRunOfLetterOrDot(accChars = List(), traversalList = traversalList, indexLeftOff= 0)

		extractContinue(accRuns = accRuns :+ newAcc.mkString, traversalList = traversalList.drop(idx))
	}

	def replacePkgWithClass(strWithPkgNames: String): String = {
		// Get the extractions
		val exs = extractContinue(List(), strWithPkgNames.toList)
		// Clean up to contain only pkg names
		val justPkgNames = exs.filter(_.contains('.'))

		// Now replace old dot pkg names with the last class names
		val classNames = justPkgNames.map(_.split('.').last)
		val pairs: List[(String, String)] = justPkgNames.zip(classNames)

		val replacements = pairs.foldLeft(strWithPkgNames){case (acc, (old, upd)) => acc.replace(old, upd)}

		replacements
	}

	// ----------------------------------------
	def getPackageName[T: ClassTag]: String ={
		classTag[T].runtimeClass.getPackage match {
			case null => ""
			case other => other.toString.split("\\ ").last + "."
		}
	}

	def inspect[T: ClassTag: TypeTag]: String =  {
		typeTag[T].tpe.toString.replace(getPackageName[T], "")
		//.split("\\.").last
	}

	def getPackageName_obj[T: ClassTag](obj: T): String ={
		classTag[T].runtimeClass.getPackage match {
			case null => ""
			case other => other.toString.split("\\ ").last + "."
		}
	}

	//---

	def inspectFunc_print[T](f: T)(implicit tag: TypeTag[T]) = {
		println(s"tag.tpe = ${tag.tpe}")
		println(s"typeOf[T] = ${typeOf[T]}")
	}

	def inspectObj_print[T](x: T)(implicit tag: TypeTag[T]) = {
		println(s"tag.tpe = ${tag.tpe}")
		println(s"tag.tpe.typeArgs = ${tag.tpe.typeArgs}")
		println(s"tag.tpe.typeParams = ${tag.tpe.typeParams}")
		println(s"tag.tpe.paramLists = ${tag.tpe.paramLists}")
		println(s"tag.tpe.companion = ${tag.tpe.companion}")

		println(s"tag.tpe.typeSymbol = ${tag.tpe.typeSymbol}")
		println(s"tag.tpe.typeSymbol.name = ${tag.tpe.typeSymbol.name}")
		println(s"tag.tpe.typeSymbol.name.decoded = ${tag.tpe.typeSymbol.name.decoded}")

		println(s"typeOf[T] = ${typeOf[T]}")
		println(s"typeOf[T].typeSymbol.name = ${typeOf[T].typeSymbol.name}")
		println(s"typeOf[T].typeSymbol.name.decoded = ${typeOf[T].typeSymbol.name.decoded}")
	}

	// These are the key functions for getting the functino type:

	def inspectFunc_str[T](f: T)(implicit tag: TypeTag[T]): String = tag.tpe.toString
	def inspectFunc_strNoPkg[T: ClassTag](func: T)(implicit tag: TypeTag[T]): String = {
		tag.tpe.toString.replace(getPackageName[T], "")
	}
	def inspectFunc_Type[T](f: T)(implicit tag: TypeTag[T]): Type = tag.tpe
	//error//def inspectFunc_T[T](f: T)(implicit tag: TypeTag[T]): T = tag.tpe.asInstanceOf[T]

	// Nicer name
	def getFuncType[T: TypeTag](f: T): String = {
		val funcTypeStr: String = inspectFunc_str[T](f)
		println(s"Printing long function type = ${funcTypeStr}")

		funcTypeStr
	}
	def getShortFuncType[T: TypeTag](f: T): String = {
		val shortFuncTypeStr: String = replacePkgWithClass(strWithPkgNames = getFuncType[T](f))
		println(s"Printing short function type = ${shortFuncTypeStr}")
		shortFuncTypeStr
	}

	/*val fs2 = "(scala.zio.pkg1.pk2.pkg3.Enum3[A,B, C, D], scala.zio.pkg3.pkg4.CaseClass5[A1, A2, A3, A4, A5]) => scala.zio.pkg8.pkg9.CaseClass3[R, A, S]"*/

	//assert(replacePkgWithClass(fs2) == "(Enum3[A,B, C, D], CaseClass5[A1, A2, A3, A4, A5]) => CaseClass3[R, A, S]")
}
