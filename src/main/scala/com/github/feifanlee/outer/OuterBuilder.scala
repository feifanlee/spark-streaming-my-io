package com.github.feifanlee.outer

import java.io.{File, FileInputStream}
import java.util.Properties

/**
  * Created by lifeifan on 2018/8/17.
  */
object OuterBuilder {
    def build():Outer={
        val is = new FileInputStream(new File("my-io-config.properties"))
        val prop = new Properties()
        prop.load(is)
        is.close()
        prop.getProperty("outer.class") match {
            case "mybatis"=> MybatisOuter.apply(prop)
            case null => new BaseOuter
            case clz:String=> Class.forName(clz).asSubclass(classOf[Outer]).newInstance()
            case _ => new BaseOuter
        }
    }
}
