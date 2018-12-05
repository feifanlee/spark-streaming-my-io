package com.github.feifanlee.mapper
import java.util

/**
  * Created by lifeifan on 2018/8/17.
  */
class BaseMapper extends Mapper{
    override def toJMap(in: String): util.Map[String, Object] = {
        val map = new java.util.HashMap[String,Object]
        map.put("data",new java.lang.String(in))
        map
    }
}
