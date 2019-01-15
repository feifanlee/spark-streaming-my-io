package com.xc
import java.util

import com.github.feifanlee.fitter.Fitter
import com.github.feifanlee.util.HbaseUtil
import com.github.feifanlee.util.HbaseUtil._
import com.github.feifanlee.util.JavaUtil._
/**
  * Created by lifeifan on 2018/12/5.
  */
class AddFitter extends Fitter{
    override def fit(line: java.util.Map[String, Object]): java.util.Map[String, Object] = {
        val map = HbaseUtil.getRow("lfftest","1-2","cf1")
        val col1 = map.get("col1".getBytes).toJInt

        if(line.containsKey("col1")) {
            line.put("col1", new java.lang.Integer(line.get("col1").toJInt +col1))
        }
        if(line.containsKey("col2")){
            line.put("col2", new java.lang.Integer(line.get("col2").toJInt +col1))
        }
        if(line.containsKey("col3")){
            line.put("col3", new java.lang.Integer(line.get("col3").toJInt +col1))
        }
        line
    }
}
