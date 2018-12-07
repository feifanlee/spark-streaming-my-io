package com.github.feifanlee.fitter

import com.github.feifanlee.util.ConfigUtil

/**
  * Created by lifeifan on 2018/12/5.
  */
object FitterBuilder {
    def build(): Fitter ={
        val prop = ConfigUtil.getProps
        prop.getProperty("fitter.class") match {
            case null => new BaseFitter
            case clz:String=> Class.forName(clz).asSubclass(classOf[Fitter]).newInstance()
            case _ => new BaseFitter
        }
    }
}
