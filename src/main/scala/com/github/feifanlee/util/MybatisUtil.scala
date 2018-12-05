package com.github.feifanlee.util

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.{SqlSession, SqlSessionFactory, SqlSessionFactoryBuilder}

/**
  * Created by lifeifan on 2018/8/13.
  */
object MybatisUtil {

    val fileName = "mybatis.xml"

    lazy val sessionFactory:SqlSessionFactory = {
        try {
//            val is = new FileInputStream(new File(fileName))
            val is = Resources.getResourceAsStream(fileName)
            val mapis = Resources.getResourceAsStream("mapper.xml")
            if(is==null) println("mybatis.xml is null")
            if(mapis==null) println("mapper.xml is null")
            val builder = new SqlSessionFactoryBuilder
            val factory = builder.build(is)
            factory
        }catch{
            case e => println("MybatMybatisis init failed:"+fileName)
                throw e
                null
        }
    }

    var session:Option[SqlSession] = None

    def getSingleSession(): SqlSession ={
        session.getOrElse(sessionFactory.openSession())
    }

    def close(): Unit ={
        if(session.isDefined){
            session.get.close()
        }
    }

    def openSession(): SqlSession ={
        sessionFactory.openSession(true)
    }

}
