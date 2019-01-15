package com.github.feifanlee.mapper
import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.feifanlee.util.JsonUtil._

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
  * Created by lifeifan on 2019/1/15.
  */
class AvrojsonMapper extends Mapper {
    override def toJMap(in: String): util.Map[String, Object] = {
        val map = in.json2Map()
        map.entrySet().foreach(kv => {
            //            val k = kv.getKey
            val v = kv.getValue
            if (v.isInstanceOf[java.util.Map[String, Object]]) {
                val valstruc = v.asInstanceOf[java.util.Map[String, Object]].entrySet()
                if(valstruc.size()==1){
                    valstruc.foreach(vs =>{
                        kv.setValue(vs.getValue)
                    })
                }
            }
        })
        return map
    }
}

object AvrojsonMapper{
    def apply(): AvrojsonMapper = new AvrojsonMapper()

}
