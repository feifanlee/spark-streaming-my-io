package com.github.feifanlee.mapper
import java.util
import com.github.feifanlee.util.JsonUtil.JsonString

/**
  * Created by lifeifan on 2018/12/5.
  */
class JsonMapper extends Mapper{
    override def toJMap(in: String): util.Map[String, Object] = {
        in.json2Map()
    }
}

object JsonMapper{
    def apply(): JsonMapper = new JsonMapper()
}
