package com.github.feifanlee.mapper

import java.io.{File, FileInputStream}
import java.util.Properties

/**
  * Created by lifeifan on 2018/8/17.
  */
object MapperBuilder {
    def build():Mapper={
        val is = new FileInputStream(new File("my-io-config.properties"))
        val prop = new Properties()
        prop.load(is)
        is.close()
        prop.getProperty("mapper.class") match {
            case "csv"=> CsvMapper.apply(prop)
            case null=> new BaseMapper()
            case clz:String=> Class.forName(clz).asSubclass(classOf[Mapper]).newInstance()
            case _=> new BaseMapper()
        }
    }
}
