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
import org.ocbkc.swift.global.GlobalConstant._
import org.ocbkc.swift.global.Logging._

class Welcome
{  def render(ns: NodeSeq): NodeSeq =
   {  val loggedInPlayers = Player.findAll.filter{ p => p.loggedIn_? }

      bind("top", ns,
         "numberOfPlayersLoggedIn" -> Text(loggedInPlayers.count.toString)
      )
   }
}

}
}
