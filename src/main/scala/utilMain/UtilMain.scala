package utilMain

//import org.scalatest.Informer


/**
 *
 */



object UtilMain {
	
	
	// NOTE: treating as block / pkg name the string of letters separated by the 'separators' = the underscore or dot
	def extractLongestRunOfLetterOrDot(
								    accChars: List[Char],
								    traversalList: List[Char],
								    indexLeftOff: Int = 0,
								    separators: Seq[Char] = Seq('.', '_')
							    ): (List[Char], Int) = {
		
		if (traversalList.isEmpty) return (accChars, indexLeftOff)
		
		else if (traversalList.head.isLetterOrDigit || separators.contains(traversalList.head)) {
			extractLongestRunOfLetterOrDot(accChars :+ traversalList.head, traversalList.tail, indexLeftOff + 1)
		} else { //if (! (traversalList.head.isLetterOrDigit || traversalList.head == '.')){
			return (accChars, indexLeftOff + 1)
		}
	}
	
	//TODO put tests
	//TODO write purpose
	def extractContinue(accRuns: List[String], traversalList: List[Char]): List[String] = {
		if (traversalList.isEmpty) return accRuns
		
		val (newAcc, idx) = extractLongestRunOfLetterOrDot(accChars = List(), traversalList = traversalList, indexLeftOff = 0)
		
		extractContinue(accRuns = accRuns :+ newAcc.mkString, traversalList = traversalList.drop(idx))
	}
	
	
	
	private object ReplacePackageNameHelpers {
		def replaceNameIgnorePckgNames(pkgName: String, mapOfClassSubs: Map[String, String]): String = {
			
			// NOTE: to satisfy both replacement cases:
			//  1) json.Schema --> SchemaJson_Glow (must not split the pckName before replacing)
			//  2) higherkindness.skeuomorph.JsonSchemaF --> JsonSchemaF --> SchemaJson_Skeuo (must split the pckgname before replacing)
			
			val nameToReplace: String = if (pkgName.contains('.')) pkgName else pkgName.split('.').last
			
			mapOfClassSubs.get(nameToReplace /*.split('.').last*/) match {
				case Some(subsClass) ⇒ subsClass // replace
				case None ⇒ nameToReplace // keep the class name, no replacement
			}
		}
		
		def doSubstitute(
						 subs: Option[Map[String, String]],
						 keeps: Option[List[String]],
						 on: List[String]
					 ): List[String] = {
			
			// Now filter and replace some class names with the desired class names
			val subbedClassNames: List[String] = subs match {
				case None ⇒ on // no replacements
				case Some(mapOfClassSubs) ⇒ { // then replace the old class names with the new class names in the map
					
					
					on.map((itemToSub: String) ⇒ keeps match {
						case Some(pckgsToKeep: List[String]) ⇒ pckgsToKeep.contains(itemToSub) match {
							case true ⇒ itemToSub
							case false ⇒ replaceNameIgnorePckgNames(itemToSub, mapOfClassSubs)
						}
						case None ⇒ replaceNameIgnorePckgNames(itemToSub, mapOfClassSubs)
					})
				}
			}
			subbedClassNames
		}
		
		def pickKeepers(keeps: Option[List[String]], on: List[String]): List[String] = {
			
			// Keeping the specified package names
			// Now replace old dot pkg names with the last class names
			val classNames: List[String] = keeps match {
				case Some(pckgsToKeep: List[String]) ⇒ on.map { pckg ⇒
					pckgsToKeep.contains(pckg) match {
						case true ⇒ pckg // keep the entire package name
						case false ⇒ pckg.split('.').last // else return just the class name
					}
				}
				case None ⇒ on.map(_.split('.').last) // no filtering, just replace all package naems with the last name (class)
			}
			
			classNames
		}
	}
	import ReplacePackageNameHelpers._
	
	
	/**
	 * Replace package names with just the class names
	 *
	 * @param classNameWithPckgNames = the string in which to replace the package names with just the class names (last element after dot)
	 * @param optPckgsToKeep         = list of package names to leave untouched (no clean-up)
	 * @param optMapOfClassesToSubst = map of class names to substitute (example: if result after package-replacement-operation contains Elephant => Mouse and we want to replace Elephant with Kangaroo, then subClassNames == Map("Elephant" -> "Kangaroo", ...)
	 * @return
	 */
	def replacePkgWithClass(
						   classNameWithPckgNames: String,
						   optPckgsToKeep: Option[List[String]] = None,
						   optMapOfClassesToSubst: Option[Map[String, String]] = None
					   ): String = {
		
		
		// Get the extractions
		val pkgExtractions: List[String] = extractContinue(List(), classNameWithPckgNames.toList)
		
		// Clean up to contain only pkg names
		val justPkgNames: List[String] = pkgExtractions.filter(_.contains('.'))
		
		
		val sub1: List[String] = doSubstitute(optMapOfClassesToSubst, optPckgsToKeep, justPkgNames)
		
		val keep1: List[String] = pickKeepers(optPckgsToKeep, sub1)
		
		val sub2: List[String] = doSubstitute(optMapOfClassesToSubst, optPckgsToKeep, keep1)
		
		
		
		
		val pairs: List[(String, String)] = justPkgNames.zip(sub2)
		
		val justClassNames: String = pairs.foldLeft(classNameWithPckgNames) { case (acc, (old, upd)) => acc.replace(old, upd) }
		
		justClassNames // may contain some package names as per the `keepPckgs` argument
		
		
	}
	
	
	/*
	* Replace _root_.java.lang.String.... etc with simple name String
	*
	* Example:
	* input = Right(final case class User(name: _root_.java.lang.String, favorite_number: _root_.scala.Option[_root_.scala.Int], favorite_color: _root_.scala.Option[_root_.java.lang.String]))
	* output =
	*/
	def cleanScalaTypeNames(str: String): String = {
		str.replace("_root_.java.lang.", "")
			.replace("_root_.scala.", "")
			.replace("_root_.", "")
	}
	
	// ----------------------------------------
	
	
	import scala.reflect.{ClassTag, classTag}
	import scala.reflect.runtime.universe._
	
	
	def getPackageName[T: ClassTag]: String = {
		classTag[T].runtimeClass.getPackage match {
			case null => ""
			case other => other.toString.split("\\ ").last + "."
		}
	}
	
	/*def getPackageName_noclass[T: TypeTag](isEnumType: Boolean = false): String = {
	val entireNameInclPckg: Type = typeOf[T] //typeTag[T].tpe.typeSymbol
	// e.g.
	// val typ = typeOf[Gender.Male.type ]
	// >> val typ: reflect.runtime.universe.Type = data.ScalaCaseClassData.Gender.Male.type
	
	val sym : Symbol = symbolOf[T]
	// e.g.
	//scala>           val sym = symbolOf[Gender.Male.type ]
	//val sym: reflect.runtime.universe.TypeSymbol = object Male
	
	val simpleTypeName: String = sym.toString.split(' ').last
	//          scala> val sym = symbolOf[Gender.Male.type ].toString.split(' ').last
	//val sym: String = Male
	
	val entireNameInclPckgSplits: Array[String] = entireNameInclPckg.toString.split('.')
	//          scala> val ts = typ.toString.split('.')
	//val ts: Array[String] = Array(data, ScalaCaseClassData, Gender, Male, type)
	
	// Preparing to extract just the package name
	// If enum type was given, then index is -1 else not
	val i: Int = isEnumType match {
		case true => entireNameInclPckgSplits.indexOf(simpleTypeName) - 1
		case false => entireNameInclPckgSplits.indexOf(simpleTypeName)
	}
	// PROP: Example: enum type
	//scala> val sym = symbolOf[Gender.Male.type ].toString.split(' ').last
	//val sym: String = Male
	//scala> val ts = typ.toString.split('.')
	//val ts: Array[String] = Array(data, ScalaCaseClassData, Gender, Male, type)
	//scala> val packageNameIndexLast = ts.indexOf(sym) - 1
	//val packageNameIndexLast: Int = 2
	// PROP: Example enum (class) type
	//scala> val sym = symbolOf[CreditCard].toString.split(' ').last
	//val sym: String = CreditCard
	// scala> val ts = typeOf[CreditCard].toString.split('.')
	//val ts: Array[String] = Array(ZioSchemaExamples, Example1_PaymentWireTransfer, Domain, PaymentMethod, CreditCard)
	//scala> val packageNameIndexLast = ts.indexOf(sym) - 1
	//val packageNameIndexLast: Int = 3
	
	// Get package name
	entireNameInclPckgSplits.take(i).mkString(".")
	
	// TODO in case of when the object is layered it in yet another object, this method doesn't return the correct package name:
	//Array(ZioSchemaExamples, Example1_PaymentWireTransfer, Domain, PaymentMethod, CreditCard)
	// NOTE - for class <CreditCard, the result of this function is:
	// scala> ts.take(ts.indexOf(sym)-1).mkString(".")
	//val res21: String = ZioSchemaExamples.Example1_PaymentWireTransfer.Domain
	}*/
	
	
	// NOTE: another version of the inspectClass function except can use for type aliases or enum types which are not classes (otherwise compiler complains when using the classTag[] function
	def inspectType[T: TypeTag] /*(isFromEnum: Boolean = false)*/ : String = {
		// TODO if you want to see the output here, return a Map and print out the result: 'tpe' -> tpe value, 'typeSymbol' -> typeSymbol etc) so no need for printing here
		// ISSUE WITH PRINTING HERE: need it to be info() so can print sequentially if called in the tests, but here cannot call scalatest because itis main folder so cannot use info.
		/*info(typeTag[T].tpe)
		info(typeTag[T].tpe.typeSymbol)
		info(typeTag[T].tpe.typeArgs)
		info(typeTag[T].tpe.typeParams)
		info(typeTag[T].tpe.typeConstructor)
		info()
		info(typeTag[T].getClass)
		info(typeTag[T].getClass.getPackage)
		info(typeTag[T].getClass.getSimpleName)
		info(typeTag[T].getClass.getName)
		info()
		info(typeTag[T].tpe.getClass.getPackage)
		info(typeTag[T].tpe.getClass)
		info(typeTag[T].tpe.getClass.getSimpleName)
		info(typeTag[T].tpe.getClass.getName)
		
		val res = typeTag[T].tpe.toString
		/*if (isFromEnum) {
			return res.split('.').tail.head
		}*/
		return res*/
		
		typeTag[T].tpe.typeSymbol.toString.split(' ').last
	}
	/*import data.ScalaCaseClassData._
	info(inspectType[Gender.Male.type](isFromEnum = true))
	info("credit card: ")
	info(inspectType[ZioSchemaExamples.Example1_PaymentWireTransfer.Domain.PaymentMethod.CreditCard]())*/
	
	
	// NOTE: if isFromEnum == true, then the type result usually after this function is something like Gender.Male.type and this function instead cleans this up to be just "Male"
	def inspectClass[T: ClassTag : TypeTag](isFromEnum: Boolean = false): String = {
		val result: String = typeTag[T].tpe.toString.replace(getPackageName[T], "")
		
		if (isFromEnum) {
			return result.split('.').tail.head
		}
		result
		//.split("\\.").last
	}
	
	
	def getPackageName_obj[T: ClassTag](obj: T): String = {
		classTag[T].runtimeClass.getPackage match {
			case null => ""
			case other => other.toString.split("\\ ").last + "."
		}
	}
	
	//---
	
	def inspectFunc_showByMap[T](f: T)(implicit tag: TypeTag[T]): Map[String, String] = {
		Map(
			"tag.tpe" → s"${tag.tpe}",
			"typeOf[T]" → s"${typeOf[T]}"
		)
	}
	
	def inspectObj_showByMap[T](obj: T)(implicit tag: TypeTag[T]): Map[String, String] = {
		Map(
			"tag.tpe" → s"${tag.tpe}",
			"tag.tpe.typeArgs" → s"${tag.tpe.typeArgs}",
			"tag.tpe.typeParams" → s"${tag.tpe.typeParams}",
			"tag.tpe.paramLists" → s"${tag.tpe.paramLists}",
			"tag.tpe.companion" → s"${tag.tpe.companion}",
			
			"tag.tpe.typeSymbol" → s"${tag.tpe.typeSymbol}",
			"tag.tpe.typeSymbol.name" → s"${tag.tpe.typeSymbol.name}",
			"tag.tpe.typeSymbol.name.decoded" → s"${tag.tpe.typeSymbol.name.decoded}",
			
			
			"typeOf[T]" → s"${typeOf[T]}",
			"typeOf[T].typeSymbol.name" → s"${typeOf[T].typeSymbol.name}",
			"typeOf[T].typeSymbol.name.decoded" → s"${typeOf[T].typeSymbol.name.decoded}"
		)
	}
	
	// These are the key functions for getting the functino type:
	
	def inspectFunc_str[T](f: T)(implicit tag: TypeTag[T]): String = tag.tpe.toString
	
	def inspectFunc_strNoPkg[T: ClassTag](func: T)(implicit tag: TypeTag[T]): String = {
		tag.tpe.toString.replace(getPackageName[T], "")
	}
	
	def inspectFunc_Type[T](f: T)(implicit tag: TypeTag[T]): Type = tag.tpe
	//error//def inspectFunc_T[T](f: T)(implicit tag: TypeTag[T]): T = tag.tpe.asInstanceOf[T]
	
	// Nicer name
	def getLongFuncType[T: TypeTag](f: T): String = {
		val funcTypeStr: String = inspectFunc_str[T](f)
		//info(s"Printing long function type = ${funcTypeStr}")
		
		funcTypeStr
	}
	
	def getShortFuncType[T: TypeTag](
								  f: T,
								  keepPckgs: Option[List[String]] = None,
								  classesToSubs: Option[Map[String, String]] = None
							  ): String = {
		
		val shortFuncTypeStr: String = replacePkgWithClass(classNameWithPckgNames = getLongFuncType[T](f), keepPckgs, classesToSubs)
		
		//info(s"Printing short function type = ${shortFuncTypeStr}")
		
		shortFuncTypeStr
	}
	
	def getFuncType[T: TypeTag](
							  f: T/*,
							  keepPckgs: Option[List[String]] = None,
							  classesToSubs: Option[Map[String, String]] = None*/
						  ): Map[String, String] = {
		
		import utilData.UtilData._
		
		Map(
			"long function type" → getLongFuncType(f),
			"short function type" → getShortFuncType(f, keepPckgs, classesToSubs)
		)
	}
	
	
	// Making shortcut to not have to pass in these 'keepPckgs' and 'classesToSubstitute' values all the time within the schema tests:
	
	
	import utilData.UtilData._
	
	
	def getFuncTypeSubs[T: TypeTag](f: T): String = getShortFuncType(f, keepPckgs, classesToSubs)
	
	/*val fs2 = "(scala.zio.pkg1.pk2.pkg3.Enum3[A,B, C, D], scala.zio.pkg3.pkg4.CaseClass5[A1, A2, A3, A4, A5]) => scala.zio.pkg8.pkg9.CaseClass3[R, A, S]"*/
	
	//assert(replacePkgWithClass(fs2) == "(Enum3[A,B, C, D], CaseClass5[A1, A2, A3, A4, A5]) => CaseClass3[R, A, S]")
}

