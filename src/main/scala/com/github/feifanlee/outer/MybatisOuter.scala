package com.github.feifanlee.outer
import java.util
import java.util.Properties

import com.github.feifanlee.util.MybatisUtil
import org.apache.ibatis.session.SqlSession

/**
  * Created by lifeifan on 2018/8/16.
  */
class MybatisOuter(sqlid:String) extends Outer{
    var session:SqlSession=null
    override def init(): Unit = {
        this.session = MybatisUtil.openSession()
    }

    override def out(line: util.Map[String, Object]): Unit = {
        session.insert(sqlid,line)
    }

    override def close(): Unit = {
        session.commit()
        session.close()
    }
}

object MybatisOuter{
    def apply(prop:Properties): MybatisOuter = {
        val sqlid = prop.getProperty("outer.mybatis.sqlid")

        new MybatisOuter(sqlid)
    }
}
