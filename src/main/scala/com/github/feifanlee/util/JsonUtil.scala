package com.github.feifanlee.util

import com.fasterxml.jackson.databind.ObjectMapper

/**
  * Created by lifeifan on 2018/12/4.
  */
object JsonUtil {
    implicit class JsonString(json:String){
        def json2Map():java.util.HashMap[String,Object]={
            val mapper = new ObjectMapper
            mapper.readValue(json, classOf[java.util.HashMap[String, Object]])
        }
    }
}
