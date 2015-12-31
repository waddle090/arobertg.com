package com.schwizzbot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


public class Dao {
	
	
	
	public static void removeQuoteById(DataSource ds, int id){
		Connection con = null;
        Statement stmt = null;
        
        try {
            con = ds.getConnection();
            stmt = con.createStatement();            
            stmt.executeUpdate("DELETE from quotes where id = " + id);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                try {
        
                    if(stmt != null) stmt.close();
                    if(con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        
	}
	public static String getQuoteById(DataSource ds, int id){
		List<String> retList = new ArrayList<>();
		Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            //SELECT * FROM `table` ORDER BY RAND() LIMIT 0,1;
            rs = stmt.executeQuery("select ID, MESSAGE, AUTHOR from quotes where id = " + id);
            while(rs.next()){
            	retList.add("[" + rs.getInt("ID") + "] " + rs.getString("MESSAGE")+ " - " +rs.getString("AUTHOR"));
//                System.out.println(rs.getString("MESSAGE")+ " - " +rs.getString("AUTHOR"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                try {
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                    if(con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return retList.isEmpty()?"":retList.get(0);
	}
	public static String getRandomQuote(DataSource ds){
//		DataSource ds = null;
        
        List<String> retList = new ArrayList<>();
		Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            //SELECT * FROM `table` ORDER BY RAND() LIMIT 0,1;
            rs = stmt.executeQuery("select ID, MESSAGE, AUTHOR from quotes ORDER BY RAND() LIMIT 0,1");
            while(rs.next()){
            	retList.add("[" + rs.getInt("ID") + "] " + rs.getString("MESSAGE")+ " - " +rs.getString("AUTHOR"));
//                System.out.println(rs.getString("MESSAGE")+ " - " +rs.getString("AUTHOR"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                try {
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                    if(con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return retList.isEmpty()?"":retList.get(0);
	}
	public static void insertQuote(DataSource ds, String author, String message, String user){
//		DataSource ds = null;
        
        List<String> retList = new ArrayList<>();
		Connection con = null;
        PreparedStatement stmt = null;
//        ResultSet rs = null;
        try {
            con = ds.getConnection();
            
            stmt = con.prepareStatement("INSERT INTO quotes SET MESSAGE = ?, AUTHOR = ?, USER = ?");
    		//Bind values into the parameters.
    	      stmt.setString(1, message);
    	      stmt.setString(2, author);
    	      stmt.setString(3, user);
    	      stmt.executeUpdate();
    	      
        } catch (MySQLIntegrityConstraintViolationException e){
        	System.out.println("no record inserted, quote already exists");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
//                    if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
}
