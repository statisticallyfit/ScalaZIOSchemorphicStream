//package utilTest
//
////import org.scalatest.Informer
//
//import org.scalatest.{Informer, Informing}
//import utilMain.UtilMain
//
///**
// * REASON: for inheriting UtilMain - to get its functions callable from test here while also restricting with self-type so can call info instead of print stattements so can print sequentially
// *
// * NOTE: no more need for that because no more print statements in the UtilMain so no more concern that they don't print in order - info only gets called in the tests themselves.
// */
//trait UtilTest extends UtilMain { this : Informing ⇒ //org.scalatest.featurespec.AnyFeatureSpecLike ⇒
//
//
//}
