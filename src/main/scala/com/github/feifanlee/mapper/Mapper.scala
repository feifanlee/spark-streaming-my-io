package com.github.feifanlee.mapper

/**
  * Created by lifeifan on 2018/8/16.
  */
trait Mapper {
    def toJMap(in:String):java.util.Map[String,Object]
}
