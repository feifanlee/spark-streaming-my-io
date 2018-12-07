package com.xc
import java.util

import com.github.feifanlee.fitter.Fitter
import com.github.feifanlee.util.HbaseUtil
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by lifeifan on 2018/12/5.
  */
class AddFitter extends Fitter{
    override def fit(line: util.Map[String, Object]): util.Map[String, Object] = {
        val map = HbaseUtil.getRow("lfftest","1-2","cf1")
        val col1 = Bytes.toInt(map.get("col1".getBytes))

        if(line.containsKey("col1")) {
            line.put("col1", (line.get("col1").asInstanceOf[Integer] +col1).asInstanceOf[Object])
        }
        if(line.containsKey("col2")){
            line.put("col2", (line.get("col2").asInstanceOf[Integer] +col1).asInstanceOf[Object])
        }
        if(line.containsKey("col3")){
            line.put("col3", (line.get("col3").asInstanceOf[Integer] +col1).asInstanceOf[Object])
        }
        line
    }
}
