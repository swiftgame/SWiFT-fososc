package org.ocbkc.generic
{

package random
{
import System._
import scala.util.Random

object RandomExtras
{  def nextBetween(ranSeq: Random, min:Int, max:Int):Int = 
   {  min + ranSeq.nextInt( max - min + 1 )
   }

   /** min is inclusive, max is exclusive
     */
   def nextBetween(ranSeq: Random, min:Double, max:Double):Double =
   {  min + ranSeq.nextDouble * (max - min)
   }

   def pickRandomElementFromList[A](list:List[A], rs:Random):Option[A] =
   {  list match
      {  case Nil => None
         case _   => Some(list(rs.nextInt( list.length )))
      }
   }
}
}

object ListUtils
{  def takeNumOrAll[A](list:List[A], num:Int) =
   {  if( num > -1 )
         list.take(num)
      else
         list
   }

   /**  @param f: function which maps element if inList (A) to value of type C, and gets a value of type B as context information originating from the previous time f was applied to the element at the left. 
   *
   */
   // <&y2012.10.23.23:40:37& todo: move to general lib>
   def mapWithLeftContext[A,B,C](inList:List[A], leftContext:B, f:(A,B) => (C,B) ):List[C] =
   {  inList match
      {  case x::xs  => {  val (newX, nextLeftContext) = f(x,leftContext)
                        newX::mapWithLeftContext(xs, nextLeftContext, f)
                     }
         case List() => List()
      }
   }
}

object DateTime
{  import org.ocbkc.swift.global.Types._

   val dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss") // also for reuse!

   def timeInMillis2dateString(time:TimeInMillis) =
   {  dateFormat.format(time).toString
   }
}


/*
//import javax.mail._ <&y2012.06.25.19:45:04& remove this, because I found net.liftweb.util.Mailer.  to do this>
//import javax.mail.internet._


object Mail
{  def send(aFromEmailAddr:String, aToEmailAddr:String, aSubject:String, aBody:String) = 
   {  //Here, no Authenticator argument is used (it is null).
      //Authenticators are used to prompt the user for user
      //name and password.
      val session:Session = Session.getDefaultInstance( fMailServerConfig, null )
      val message = new MimeMessage( session )
      try {
         //the "from" address may be set in code, or set in the
         //config file under "mail.from" ; here, the latter style is used
         //message.setFrom( new InternetAddress(aFromEmailAddr) );
         message.addRecipient(
         Message.RecipientType.TO, new InternetAddress(aToEmailAddr)
         )
         message.setSubject( aSubject )
         message.setText( aBody )
         Transport.send( message )
      }
      catch (ex:MessagingException){
         err.println("Cannot send email. " + ex)
      }
   }
}
*/

/** Package intended to do "coarse parallelism": simulating running function-applications in parallel, just as threads, but then without using threads. This works for function-applications in which there is much "waiting" on other external programs or threads (already existing threads, or threads created elsewhere), and each function application does have to do a lot of work from the perspective of the computer.

Note that the function can only be considered a function, if you also consider the FunAppId as an argument to the function (then it produces a single result for each unique combination of arguments).

See log of this package in the comment at the end of the package (not included in javadoc).
*/

package coarseParallelism
{
/**
  */
object Types
{  /**   The output Bool = true means that the resultProcessor was applied succesfully.
     */
   /* <&y2015.05.08.22:25:30& do I need that (see line 104)?>
    */
   type ResultProcessorType[InputType__TP] = ( List[InputType__TP] => Bool )
}

object TestCoarseParallelism extends ParallelFunAppRequester 
{  def main(args: Array[String]) =
   {  val list = List(1,2,3,4,5,8)
      
      object parallelFunction[]
      {  def start(input:InputType__TP, requester:ParallelFunAppRequester):FunAppId =
         {  log("start( input = " + input.toString)
            // start thread here (normally you assume some external thread to exist or come into existence, but this is for testing purposes.)
            startThread(this)
         }       
      }
   }
   /* finish:
   TestThread Thread
   {  mainThreadThingTODO
      {  sleepTODO random seconds
      }      
   }
   */
}

trait ApplicableInParallel[InputType__TP, ResultType__TP]
{  val ResultProcessors:List[ResultProcessorType[InputType_TP]]

   case class FunAppRequest(input:InputType__TP, output:Some[ResultType__TP], resultProcessors:List[ResultProcessorType])
   {
   /* log
      {  o &y2015.05.08.14:15:33& if this code is going to be refactered to do first in first out for resultProcessors, better change to Queue instead of List.
      }
   */
   }

   case class FunAppPairs(input:InputType__TP, output:Some[ResultType__TP])
   {  
   }

   // { Code to manipulate and analyse FunAppPairs
   /* log
      { o <should do &y2015.05.08.14:36:10& make a separate class for manipulating lists of funappRequests and put this function there.>
      }
   */
   def FunAppRequests2FunAppPairs(fars:List[FunAppRequest]):List[FunAppPair] =
   {  fars.map{ far => FunAppPair(far.input, far.output) }
   }

   def AllFunAppPairsDefined:Bool(faps:List[FunAppPairs]) =
   {  !faps.exists{ fap => fap.output.isEmpty }
   }
   // }

   object FunAppRequest
   {  private val funappRequests:List[FunAppRequest] = Nil
      /* log
         {  o &y2015.05.08.14:13:23& most efficient may be using a Queue if you want to do first in first out. Also see http://www.scala-lang.org/docu/files/collections-api/collections_40.html      
         }
      */
         
      /** Add it such that each FunAppRequest in the list has a unique input.
        */
      def addRequest(input:InputType__TP, resultProcessor:ResultProcessorType) =
      {  funappRequests.find{ far => far.input == input } match
         {  case Some(far) =>
            {  far.resultProcessors = resultProcessor :: far.resultProcessors
            }
            case None      =>
            {  far.resultProcessors = FunAppRequest(input, None, List(resultProcessors)
            }
         }
      }

      def getRequestsOf(resultProcessor: ResultProcessorType):List[FunAppPair] =
      {  FunAppRequests2FunAppPairs(funappRequests.filter{ far => far.resultProcessors.contain(resultProcessor) })
      }      
   }

   /** If subsequent calls are made, the assumption is that different resultProcessors are provided. If you want to let one resultProcessor process more than one result, call request(inputList ...). The idea is that the request is analogous to a function call, for which it also holds that a specific ``piece'' of code is the receiver of the result.
       @param resultProcessor The assumption is that this code does not take long to execute. Otherwise, it may make another thread which has other responsibilities as well too slow.
     */
   /* log
      {  o <& &y2015.05.08.21:49:37& is the assumption on line 176 really needed?>
         o <&y2015.05.05.17:16:43& perhaps in the future, allow deviation from the current assumption at resultProcessor.>
      }
   */
   def request(input:InputType__TP, resultProcessor: ResultProcessorType) =
   {  funappRequests.addRequest(input, resultProcessor)
      resultProcessors ::= resultProcessors
      callResultProcessors // the results may already have been calculated in the past.
      /* log
         {  o <&y2015.05.05.17:21:39& instead of doing a callResultProcessors, consider only checking whether the requests of the given resultProcessor are granted. Considerations are speed of execution, checking and calling all resultProcessors in the thread that called this method, may slow things down for that thread: it "expected" to only do a request, but in fact it may be running a lot of resultProcessors of previous requests made by "others".
         }
      */
   }

   def request(inputList:List[InputType__TP], resultProcessor:ResultProcessorType) =
   {  funappRequests ++= inputList.map{ FunApp(_, None) }
   }

   /** Call this method as soon as a result is known. This code will be called, in general, by another thread than the thread that ran the request.
     */
   def postResult(input:InputType__TP, result:ResultType__TP) =
   {  addResult(input, result)
      callResultProcessors
   }

   /** Calls result processors, but only if all the required results for that processor have arrived.
     */

   /* log
      {  o <&y2015.05.08.14:30:00& COULDDO: target for optimisation!>
      }
   */
   def callResultProcessors
   {  resultProcessors.foreach
      {  rp =>
         {  val funAppPairs = funappRequests.getRequestsOf(rp)
            if(AllFunAppPairsDefined(funAppPairs))
            {  rp(funAppPairs)
            }
         }
      }
   }
}

trait FunApp[InputType__TP, ResultType__TP](input: InputType__TP)
{  var result: Option[ResultType__TP]
}


/** 
  * An instance of this objects forms the connection point between the threads requesting a fnction application and the ones carrying it out. It is connected to a specific object which is ApplicableInParallel. Threads who are intended to deliver results of applications, check this object to see whether there are requests applicable to them, and then deliver them here.
  */
}

/* 

log
{  [&y2015.05.05.17:08:58& See DSID&y2015.05.05& for a draft drawing with an overview of the high level architecture of the package.]
<&y2015.02.27.22:36:59& investigate whether this can be made really functional by for example also providing a state argument to the start function.>[&y2015.05.05.17:09:49& In fact already solved, by also creating a "FunAppId", which can be interpreted as a tacit third argument that is unique for each call of the function ApplicableInParallel.]
}

*/

}
