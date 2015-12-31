package com.schwizzbot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class QuoteDao{
	//DI
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	private static final String DELETE = "DELETE from quotes where id = ?";
	private static final String QUOTE_BY_ID= "select ID, MESSAGE from quotes where id = ?";
	private static final String RANDOM_QUOTE = "select ID, MESSAGE from quotes ORDER BY RAND() LIMIT 0,1";
	private static final String ADD_QUOTE = "INSERT INTO quotes SET MESSAGE = ?, AUTHOR = ?, USER = ?";
	private static final String SELECT_ALL_QUOTES = "select * from quotes";
	
	public void removeQuoteById(int id){		
		jdbcTemplate.update(DELETE, new Object[]{id});
	}
	
	public String getQuoteById(int id){		
		List matches = jdbcTemplate.query(
				QUOTE_BY_ID, 
				new Object[]{id},
				new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs, int rowNum)
					throws SQLException, DataAccessException {
						return "[" + rs.getInt("ID") + "] " + rs.getString("MESSAGE");
					}
			});
		Iterator<String> i = matches.iterator();
		List<String> sList = new ArrayList<>();
		while((i!=null)&& i.hasNext()){
			sList.add(i.next());
		}
		return sList.isEmpty()?"":sList.get(0);
	}
	
	public String getRandomQuote(){		
		List matches = jdbcTemplate.query(
				RANDOM_QUOTE, 
				new Object[]{},
				new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs, int rowNum)
					throws SQLException, DataAccessException {
						return "[" + rs.getInt("ID") + "] " + rs.getString("MESSAGE");
					}
			});
		Iterator<String> i = matches.iterator();
		List<String> sList = new ArrayList<>();
		while((i!=null)&& i.hasNext()){
			sList.add(i.next());
		}
		return sList.isEmpty()?"":sList.get(0);
	}
	
	public int insertQuote(String author, String message, String user){
		jdbcTemplate.update(ADD_QUOTE, new Object[]{message, author, user});
		List matches = jdbcTemplate.query("select id from quotes order by id desc limit 1", new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int rowNum)
			throws SQLException, DataAccessException {
				return rs.getInt("ID");
			}});
		
		Iterator<Integer> i = matches.iterator();
		ArrayList<Integer> iList = new ArrayList<>();
		while((i!=null)&& i.hasNext()){
			iList.add(i.next());
		}
		return iList.isEmpty()?0:iList.get(0); 
	}
	
	public void printAllQuotes(){
		//SELECT_ALL_QUOTES
		List matches = jdbcTemplate.query(
				SELECT_ALL_QUOTES, 
				new Object[]{},
				new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs, int rowNum)
					throws SQLException, DataAccessException {
						return "[" + rs.getInt("ID") + "] " + rs.getString("MESSAGE")+ " - " +rs.getString("AUTHOR");
					}
			});
		Iterator<String> i = matches.iterator();
		List<String> sList = new ArrayList<>();
		while((i!=null)&& i.hasNext()){
			System.out.println(i.next());
		}		
	}
	
	
	private List<String> select(){
		String QUERY = "SELECT 1=1";
		
		List matches = jdbcTemplate.query(
				QUERY, 
				new Object[]{},
				new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs, int rowNum)
					throws SQLException, DataAccessException {
						return "test";
					}
			});
		Iterator<String> i = matches.iterator();
		List<String> sList = new ArrayList<>();
		while((i!=null)&& i.hasNext()){
			sList.add(i.next());
		}
		return sList;
	}
	
	private void updateRow(){
		String QUERY = "";		
		jdbcTemplate.update(QUERY, new Object[]{});
	}
}
