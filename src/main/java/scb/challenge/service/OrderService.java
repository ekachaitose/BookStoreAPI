package scb.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scb.challenge.entity.OrderEntity;
import scb.challenge.exception.ApiException;
import scb.challenge.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepositoy;

	public List<OrderEntity> findOrderByUserId(Integer userId) {
		try {
			return this.orderRepositoy.findOrderByUserID(userId);
		} catch (Exception e) {
			throw new ApiException("Fail to get order.", HttpStatus.EXPECTATION_FAILED);
		}
	}

	public List<OrderEntity> findOrderByUserId(List<Integer> orders) {
		try {
			return this.orderRepositoy.findOrderByOrderId(orders);
		} catch (Exception e) {
			throw new ApiException("Fail to get order.", HttpStatus.EXPECTATION_FAILED);
		}
	}

	public void addOrder(OrderEntity orderEntity) {
		try {
			this.orderRepositoy.save(orderEntity);
			// return this.orderRepositoy.addOrder(orderEntity);
		} catch (Exception e) {
			throw new ApiException("Fail to set order.", HttpStatus.EXPECTATION_FAILED);
		}
	}

	@Transactional(rollbackFor = ApiException.class)
	public void save(List<OrderEntity> prepareInsert) {
		try {
			for (OrderEntity order : prepareInsert) {
				this.orderRepositoy.save(order);
			}
		} catch (Exception e) {
			throw new ApiException("Fail to set order.", HttpStatus.EXPECTATION_FAILED);
		}
	}
	
}
