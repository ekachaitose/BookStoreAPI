package scb.challenge.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import scb.challenge.entity.UserEntity;

@Repository
public class UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void save(UserEntity user) {

		String sql = "insert into users (username, password, name, surname, birthdate, status , createdate, updatedate) values(:username,:password,:name,:surname,:birthdate,'Y',NOW(),NOW())";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("username", user.getUsername());
		param.addValue("password", user.getPassword());
		param.addValue("name", user.getName());
		param.addValue("surname", user.getSurname());
		param.addValue("birthdate", user.getBirthdate());
		this.jdbcTemplate.update(sql, param);
	}

	public boolean existsByUsername(String username) {
		UserEntity user = this.findByUsername(username);
		return user != null;
	}

	public UserEntity findByUsername(String username) {
		String sql = "select * from users where username = :username and status = 'Y' ";
		List<UserEntity> result = this.jdbcTemplate.query(sql, new MapSqlParameterSource("username", username),
				new BeanPropertyRowMapper<>(UserEntity.class));
		return result == null || result.isEmpty() ? null : result.get(0);
	}

	public boolean deleteByUsername(String username) {
		String sql = "update users set status = 'N' where username = :username";
		return  this.jdbcTemplate.update(sql, new MapSqlParameterSource("username", username)) > 0;
	}

}
