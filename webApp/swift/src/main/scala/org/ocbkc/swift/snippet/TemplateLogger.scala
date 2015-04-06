package org.ocbkc.swift.snippet

import _root_.scala.xml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import org.ocbkc.swift.global.Logging._

/** Use methods of this class to create log messages directly in a template.
  */
class TemplateLogger
{  def threadid:NodeSeq =
   {  logthreadid
      NodeSeq.Empty
   }
}

