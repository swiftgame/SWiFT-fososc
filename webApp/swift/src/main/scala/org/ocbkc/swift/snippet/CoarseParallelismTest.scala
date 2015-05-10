package org.ocbkc.swift 
{
package snippet 
{
import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import org.ocbkc.swift.lib._
import org.ocbkc.swift.model.Player
import Helpers._
import System.err.println
import org.ocbkc.swift.global.GlobalConstant._
import org.ocbkc.swift.global.TestSettings._
import org.ocbkc.swift.parser._
import scala.util.parsing.combinator.Parsers //{Success, Failure}
import org.ocbkc.swift.coord.ses._
import org.ocbkc.swift.global.Logging._

   /** [&y2015.03.04.01:33:22& Still in progress] This is a test used during the development of CoarseParallelism applied to Ocevohut. During the boot of Lift, an Ocevohu-system instance is initialised and started. When the user U goes to this page, it shows an overview of all current mutation requests requiring human interaction (a table, with each row representing such a request, and also showing the parent). Clicking on the request brings U to a page in which he can enter a child for that particular parent. For now, the genotype consists of point in 3D space. As soon as he submits the new child, it is communicated to Ocevohut (by means of CoarseParallelism).
  */
class CoarseParallelismTest
{ 
/* 
{ StartOfUnfinishedCode
   def tableRows(ns:NodeSeq):NodeSeq =
   {  val header = bind(
         "top", chooseTemplate("top", "row", ns),
         "parentId"           -> <b>parent ID</b>,
         "requestId"  -> <b>mutation request ID</b>
         "produceChild"       -> <b>produce child!</b>
         )
      header ++
      TODOgetMutationRequests.flatMap(
      mr =>
      {  
      }
   }

   def render(ns: NodeSeq): NodeSeq =
   {  bind( "form", ns,
         "x" -> SHtml.link
         TODO
   }      
} EndOfUnfinishedCode */
}

}
}
