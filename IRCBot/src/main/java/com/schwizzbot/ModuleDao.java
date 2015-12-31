package com.schwizzbot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ModuleDao {
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	private static final String SELECT_MODULES = "select * from modules";
	
	
	
	public List<String> getModules(){		
		List matches = jdbcTemplate.query(
				SELECT_MODULES, 
				new Object[]{},
				new RowMapper(){
					@Override
					public Object mapRow(ResultSet rs, int rowNum)
					throws SQLException, DataAccessException {
						return rs.getString("NAME");
					}
			});
		Iterator<String> i = matches.iterator();
		List<String> sList = new ArrayList<>();
		while((i!=null)&& i.hasNext()){
			sList.add(i.next());
		}
		return sList;
	}
}
