package com.github.feifanlee.mapper

import com.github.feifanlee.util.ConfigUtil

/**
  * Created by lifeifan on 2018/8/17.
  */
object MapperBuilder {
    def build():Mapper={
        val prop = ConfigUtil.getProps
        prop.getProperty("mapper.class") match {
            case "csv"=> CsvMapper.apply(prop)
            case "json"=> JsonMapper.apply()
            case "avrojson"=> AvrojsonMapper.apply()
            case null=> new BaseMapper()
            case clz:String=> Class.forName(clz).asSubclass(classOf[Mapper]).newInstance()
            case _=> new BaseMapper()
        }
    }
}
