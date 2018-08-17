package com.github.feifanlee.outer

/**
  * Created by lifeifan on 2018/8/16.
  */
trait Outer {
    def init()
    def out(line : java.util.Map[String,Object])
    def close()
}
