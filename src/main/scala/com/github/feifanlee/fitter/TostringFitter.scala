package com.github.feifanlee.fitter
import java.util
import scala.collection.JavaConversions._

/**
  * Created by lifeifan on 2019/2/28.
  */
class TostringFitter extends Fitter{
    override def fit(line: util.Map[String, Object]): util.Map[String, Object] = {
        val rst = new util.HashMap[String,Object]()
        line.entrySet().foreach(kv=>{
            rst.put(kv.getKey,kv.getValue.toString)
        })
        rst
    }
}
