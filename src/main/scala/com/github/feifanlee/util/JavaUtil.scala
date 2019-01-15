package com.github.feifanlee.util

import java.util
import scala.collection.JavaConversions._

/**
  * Created by lifeifan on 2018/12/13.
  */
object JavaUtil {
    implicit class JavaObject(val obj:Object){
        def toJInt:java.lang.Integer = obj.asInstanceOf[java.lang.Integer]
        def toJDouble : java.lang.Double = obj.asInstanceOf[java.lang.Double]
        def toJFloat : java.lang.Float = obj.asInstanceOf[java.lang.Float]
        def toJString : java.lang.String = obj.toString
    }

//    implicit class JavaRef[T<:Object](val obj: T){
//        def toObj : Object = obj.asInstanceOf[Object]
//    }
//
//    implicit class ScalaRef(val obj: AnyVal){
//        def toObj : Object = obj.asInstanceOf[Object]
//    }

    implicit class JavaMap(val map:java.util.Map[String,Object]){
        def lowwerKey:java.util.Map[String,Object] = {
            val newmap = new util.HashMap[String,Object]()
            map.keySet().foreach(key=>{
                newmap.put(key.toLowerCase,map.get(key))
            })
            newmap
        }
    }
}
