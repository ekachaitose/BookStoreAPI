package scb.challenge.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import scb.challenge.entity.OrderEntity;
import scb.challenge.entity.UserEntity;
import scb.challenge.exception.ApiException;
import scb.challenge.model.request.UserOrderRequest;
import scb.challenge.model.request.UserRequest;
import scb.challenge.model.response.UserOrderResponse;
import scb.challenge.model.response.UserResponse;
import scb.challenge.model.rest.BookClientResponse;
import scb.challenge.rest.BookRestClient;
import scb.challenge.service.OrderService;
import scb.challenge.service.UserService;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

	SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	@Autowired
	private UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	BookRestClient bookRestClient;

	@GetMapping("")
	@ApiOperation(value = "Gets information about the logged in user")
	public ResponseEntity<UserResponse> getUser(Principal principal) {
		UserResponse resp = null;
		if (principal != null) {
			String name = principal.getName();
			UserEntity user = userService.search(name);
			if (user != null) {
				resp = new UserResponse();
				resp.setName(user.getName());
				resp.setSurname(user.getSurname());
				if (user.getBirthdate() != null)
					resp.setDateOfBirth(sf.format(user.getBirthdate()));

				List<OrderEntity> orders = this.orderService.findOrderByUserId(user.getId());
				if (orders != null & !orders.isEmpty()) {
					List<Integer> lstBooks = new ArrayList<>();
					for (OrderEntity order : orders) {
						lstBooks.add(order.getBookId());
					}
					resp.setBooks(lstBooks);
				}
			} else
				throw new ApiException("User '" + name + "' not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@DeleteMapping("")
	@ApiOperation(value = "Delete logged in user’s record and order history.")
	public ResponseEntity<UserResponse> deleteUser(Principal principal) {
		if (principal != null) {
			String name = principal.getName();
			UserEntity user = userService.search(name);
			if (user != null) {
				this.userService.delete(name);
				
			} else
				throw new ApiException("User '" + name + "' not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("")
	@ApiOperation(value = "Create a	user account and store user’s information")
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws ParseException {
		UserResponse resp = null;
		UserEntity user = new UserEntity();
		user.setUsername(userRequest.getUsername());
		user.setPassword(userRequest.getPassword());
		user.setName(userRequest.getName());
		user.setSurname(userRequest.getSurname());
		if (userRequest.getDateOfBirth() != null)
			user.setBirthdate(sf.parse(userRequest.getDateOfBirth()));

		userService.signup(user);
		resp = new UserResponse();
		resp.setUsername(userRequest.getUsername());
		resp.setPassword(userRequest.getPassword());
		resp.setDateOfBirth(userRequest.getDateOfBirth());
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@PostMapping("orders")
	@ApiOperation(value = "Order books and store order information")
	public ResponseEntity<UserOrderResponse> getUserOrder(@RequestBody UserOrderRequest reqBook, Principal principal) {
		UserOrderResponse userOrder = null;
		if (reqBook != null) {

			String name = principal.getName();
			UserEntity user = userService.search(name);

			List<BookClientResponse> books = this.bookRestClient.books();

			List<OrderEntity> prepareInsert = new ArrayList<>();

			Date todayDate = new Date();
			sf = new SimpleDateFormat("yyyyMMdd");
			String todayDateStr = sf.format(todayDate);
			String runNo = "" + ((int) (Math.random() * 9000));
			String orderRunNo = todayDateStr + runNo;
			for (Integer id : reqBook.getOrders()) {
				Optional<BookClientResponse> optBookDetail = books.stream().filter(c -> c.getId().compareTo(id) == 0)
						.findFirst();
				if (optBookDetail.isPresent()) {
					BookClientResponse bookDetail = optBookDetail.get();
					OrderEntity order = new OrderEntity();
					order.setBookId(bookDetail.getId());
					order.setBookName(bookDetail.getBook_name());
					order.setBookPrice(bookDetail.getPrice());
					order.setUserId(user.getId());
					order.setOrderNo(orderRunNo);
					prepareInsert.add(order);
				} else {
					throw new ApiException("Book id '" + id + "' not found", HttpStatus.NOT_FOUND);
				}
			}

			if (!prepareInsert.isEmpty()) {
				this.orderService.save(prepareInsert);

				userOrder = new UserOrderResponse();
				BigDecimal sum = prepareInsert.stream().map(c -> c.getBookPrice()).reduce(BigDecimal::add)
						.orElse(BigDecimal.ZERO);
				userOrder.setPrice(sum);
			}
		} else
			throw new ApiException("Invaid request message.", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(userOrder, HttpStatus.OK);
	}

}
