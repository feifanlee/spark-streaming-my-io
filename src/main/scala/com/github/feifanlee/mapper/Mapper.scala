package com.github.feifanlee.mapper

/**
  * Created by lifeifan on 2018/8/16.
  */
trait Mapper {
    def toMap(in:String):java.util.Map[String,Object]
}
