package com.github.feifanlee.mapper
import java.util.Properties

import scala.collection.JavaConversions._

/**
  * Created by lifeifan on 2018/8/16.
  */
class CsvMapper(sep:String,meta:(List[String])=>java.util.Map[String,Object]) extends Mapper{
    override def toJMap(in: String): java.util.Map[String, Object] = {
        val data = in.split(sep)
        meta(data.toList)
    }
}

object CsvMapper{
    //col:typ,col:typ....
    //TODO: extract to meta util
    def parseMeta(prop:String): (List[String])=>java.util.Map[String,Object] ={
        val meta = prop.split(",")
                .map(kv=>(kv.split(":")(0),kv.split(":")(1)))
        (data:List[String])=>{
            val kvlist = data.zip(meta).map(dm=> {
                val (col, typ) = dm._2
                val data = dm._1
                val jdata: Object = typ match {
                    case "string" => new java.lang.String(data)
                    case "int" => new java.lang.Integer(data.toInt)
                    case "double" => new java.lang.Double(data.toDouble)
                    case "float" => new java.lang.Float(data.toFloat)
                    case _ => data
                }
                (col, jdata)
            })
            scala.collection.mutable.Map(kvlist:_*)
        }
    }

    def apply(prop:Properties): CsvMapper = {
        val sep = prop.getProperty("mapper.csv.sep",",")
        val cols = prop.getProperty("mapper.csv.meta")
        val meta = CsvMapper.parseMeta(cols)
        new CsvMapper(sep, meta)
    }
}
