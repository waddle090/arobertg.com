package com.schwizzbot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class InsultDao {
	//DI
		private JdbcTemplate jdbcTemplate;
		public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
			this.jdbcTemplate = jdbcTemplate;
		}
		private static final String SELECT_INSULT = "select RESPONSE from insults where LOWER(insult) = ?";
		private static final String SELECT_RESPONSE = "select RESPONSE from insults where LOWER(RESPONSE) = ? AND INSULT = ?";
		private static final String SELECT_RANDOM_INSULT = "select INSULT from insults ORDER BY RAND() LIMIT 0,1";
		
		
		public boolean checkResponse(String response, String insult){
			List matches = jdbcTemplate.query(
					SELECT_RESPONSE, 
					new Object[]{response.toLowerCase(), insult},
					new RowMapper(){
						@Override
						public Object mapRow(ResultSet rs, int rowNum)
						throws SQLException, DataAccessException {
							return rs.getString("RESPONSE");
						}
				});
			Iterator<String> i = matches.iterator();
			List<String> sList = new ArrayList<>();
			while((i!=null)&& i.hasNext()){
				sList.add(i.next());
			}
			return !sList.isEmpty();
		}
		
		public ImmutablePair<Boolean, String> checkInsult(String insult){		
			List matches = jdbcTemplate.query(
					SELECT_INSULT, 
					new Object[]{insult.toLowerCase()},
					new RowMapper(){
						@Override
						public Object mapRow(ResultSet rs, int rowNum)
						throws SQLException, DataAccessException {
							return rs.getString("RESPONSE");
						}
				});
			Iterator<String> i = matches.iterator();
			List<String> sList = new ArrayList<>();
			while((i!=null)&& i.hasNext()){
				sList.add(i.next());
			}
			return new ImmutablePair(!sList.isEmpty(),sList.isEmpty()?"":sList.get(0));
		}
		public String getRandomInsult(){		
			List matches = jdbcTemplate.query(
					SELECT_RANDOM_INSULT, 
					new Object[]{},
					new RowMapper(){
						@Override
						public Object mapRow(ResultSet rs, int rowNum)
						throws SQLException, DataAccessException {
							return rs.getString("INSULT");
						}
				});
			Iterator<String> i = matches.iterator();
			List<String> sList = new ArrayList<>();
			while((i!=null)&& i.hasNext()){
				sList.add(i.next());
			}
			return sList.isEmpty()?"":sList.get(0);
		}
		
}
