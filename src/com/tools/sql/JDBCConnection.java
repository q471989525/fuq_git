package com.tools.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC  连接数据库
 * @author fuq
 * @version 1.00
 */
public class JDBCConnection {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //数据库的连接手柄
        Connection conn = null;
        //执行SQL语句手柄
        PreparedStatement ps = null;
        //实例化MySql驱动
        Class.forName("com.mysql.jdbc.Driver");
        //连接数据库并返回数据库的连接手柄
        conn = DriverManager.getConnection("jdbc:mysql://192.168.2.201:3306/mxjt_lf?useUnicode=true&characterEncoding=UTF-8", "root", "root123");
        ps = conn.prepareStatement("sql");
        ps.setString(1, "ps1");
        ps.execute();
        conn.close();
    }
}
