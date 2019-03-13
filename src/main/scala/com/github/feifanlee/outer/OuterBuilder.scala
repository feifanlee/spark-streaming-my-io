package com.github.feifanlee.outer

import com.github.feifanlee.util.ConfigUtil

/**
  * Created by lifeifan on 2018/8/17.
  */
object OuterBuilder {
    def build():Outer={
        val prop = ConfigUtil.getProps
        prop.getProperty("outer.class") match {
            case "mybatis"=> MybatisOuter.apply(prop)
            case "hbase"=> HbaseOuter.apply(prop)
            case "kudu"=> KuduOuter.apply(prop)
            case null => new BaseOuter
            case clz:String=> Class.forName(clz).asSubclass(classOf[Outer]).newInstance()
            case _ => new BaseOuter
        }
    }
}
