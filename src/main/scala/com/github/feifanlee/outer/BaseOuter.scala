package com.github.feifanlee.outer
import java.util

/**
  * Created by lifeifan on 2018/8/17.
  */
class BaseOuter extends Outer{
    override def init(): Unit = {

    }

    override def out(line: util.Map[String, Object]): Unit = {
        println(line)
    }

    override def close(): Unit = {

    }
}
