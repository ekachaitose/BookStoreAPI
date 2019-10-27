package scb.challenge.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import scb.challenge.entity.OrderEntity;

@Repository
public class OrderRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<OrderEntity> findOrderByUserID(Integer userId) {
		String sql = "select * from orders where userId = :userId and status = 'Y'";
		return this.jdbcTemplate.query(sql, new MapSqlParameterSource("userId", userId),
				new BeanPropertyRowMapper<>(OrderEntity.class));
	}

	public List<OrderEntity> findOrderByOrderId(List<Integer> orders) {
		String sql = "select * from orders where id in (:id) and status = 'Y'";
		return this.jdbcTemplate.query(sql, new MapSqlParameterSource("id", orders),
				new BeanPropertyRowMapper<>(OrderEntity.class));
	}
	
	public void save(OrderEntity orderEntity ) {
		String sql = "insert into orders (orderno, bookId, bookName, userId, bookPrice, createdate, status) values(:orderno,:bookId,:bookName,:userId,:bookPrice,NOW(),'Y')";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("orderno", orderEntity.getOrderNo());
		param.addValue("bookId", orderEntity.getBookId());
		param.addValue("bookName", orderEntity.getBookName());
		param.addValue("userId", orderEntity.getUserId());
		param.addValue("bookPrice", orderEntity.getBookPrice());
		this.jdbcTemplate.update(sql, param);
	}
}
