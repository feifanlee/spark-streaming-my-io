package com.github.feifanlee.util

import java.io.{File, FileInputStream}
import java.util.Properties

/**
  * Created by lifeifan on 2018/11/22.
  */
object ConfigUtil {
    lazy val props = {
        val is = new FileInputStream(new File("my-io-config.properties"))
        val prop = new Properties()
        prop.load(is)
        is.close()
        prop
    }
    def getProps = props

    def get(key:String):String={
        props.getProperty(key)
    }
}
