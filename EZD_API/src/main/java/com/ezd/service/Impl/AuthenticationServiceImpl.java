package com.ezd.service.Impl;

import com.ezd.Dto.DataMailDTO;
import com.ezd.Dto.JwtAuthenticationResponse;
import com.ezd.Dto.RefreshTokenRequest;
import com.ezd.Dto.Role;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;
import com.ezd.models.Rank;
import com.ezd.models.StatusAccount;
import com.ezd.repository.AuthRepository;
import com.ezd.service.AuthenticationService;
import com.ezd.service.JwtService;
import com.ezd.service.MailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver; // Chỉnh sửa import
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	@Autowired
	private MailService mailService;
	@Autowired
	private final AuthRepository userRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	public AuthenticationServiceImpl(MailService mailService, AuthRepository userRepository,
			PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService,
			SpringTemplateEngine templateEngine) {
		super();
		this.mailService = mailService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.templateEngine = templateEngine;
	}

	public void resetPassword(String email) {
		// Kiểm tra xem người dùng có tồn tại hay không
		Auth user = userRepository.findByEmail(email).orElseThrow();

		// Tạo mật khẩu mới
		String newPassword = generateRandomPassword();

		// Cập nhật mật khẩu mới trong cơ sở dữ liệu
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		// Gửi email thông báo về mật khẩu mới
		sendPasswordResetEmail(user.getName(), email, newPassword);
	}

	private void sendPasswordResetEmail(String userName, String toEmail, String newPassword) {
		String htmlContent = generateHtmlContent("password-reset",
				Map.of("user", userName, "email", toEmail, "newPassword", newPassword));

		DataMailDTO mailStructure = new DataMailDTO();
		mailStructure.setSubject("Đặt Lại Mật Khẩu Thành Công");

		try {
			Map<String, Object> model = new HashMap<>();
			model.put("user", userName);
			model.put("email", toEmail);
			model.put("newPassword", newPassword);
			mailService.sendHtmlMail(mailStructure, toEmail, model, "password-reset");
		} catch (MessagingException | IOException e) {
			// Xử lý exception nếu cần
			e.printStackTrace();
		}
	}

	private String generateRandomPassword() {
		// Logic để tạo mật khẩu ngẫu nhiên
		// Bạn có thể sử dụng thư viện như Apache Commons Lang để tạo chuỗi ngẫu nhiên.
		return RandomStringUtils.randomAlphanumeric(10);
	}

	public Auth signup(SignUpRequest signUpRequest) {
		Optional<Auth> existingUser = userRepository.findByEmail(signUpRequest.getEmail());
		if (existingUser.isPresent()) {
			throw new RuntimeException("Email đã tồn tại.");
		}
		
		Rank defaultRank = new Rank();
		defaultRank.setId(1L);
		
		Auth user = new Auth();
		List<String> avatars = new ArrayList<>();
		avatars.add("https://res.cloudinary.com/dbdz9u1y6/image/upload/v1698467917/oogfmmehumkcbpxfaqrv.jpg");
		user.setAvatars(avatars);
		user.setName(signUpRequest.getName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		user.setAddress(signUpRequest.getAddress());
		user.setCountry(null);
		user.setPhoneNumber(signUpRequest.getPhoneNumber());
		user.setGender(null);
		user.setBalance(BigDecimal.ZERO);
		user.setStatus(StatusAccount.ON);
		user.setRole(Role.USER);
		user.setBirthDay(signUpRequest.getBirthDay());
		user.setCreatedDate(LocalDateTime.now());
		user.setCurrentRank(defaultRank);
		// Thay đổi cách gọi phương thức generateHtmlContent
		String htmlContent = generateHtmlContent("email",
				Map.of("email", signUpRequest.getEmail(), "password", signUpRequest.getPassword()));

		DataMailDTO mailStructure = new DataMailDTO();
		mailStructure.setSubject("Xác Nhận Đăng Ký Tài Khoản Thành Công");
		mailStructure.setContent(htmlContent);
		System.out.println("html content" + htmlContent);
		try {
			Map<String, Object> model = new HashMap<>();
			model.put("user", signUpRequest.getName());
			model.put("email", signUpRequest.getEmail());
			model.put("password", signUpRequest.getPassword());
			mailService.sendHtmlMail(mailStructure, signUpRequest.getEmail(), model, "email");
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userRepository.save(user);
	}

	private String generateHtmlContent(String templateName, Map<String, Object> model) {
		// Sử dụng templateEngine để xử lý template
		Context context = new Context();
		context.setVariables(model);
		return templateEngine.process(templateName, context);
	}

	public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

		var user = userRepository.findByEmail(signInRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid Email and PassWord"));

		if (user.getRole() == Role.USER || user.getRole() == Role.STAF) {
			var jwt = jwtService.generateToken(user);
			var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(refreshToken);
			return jwtAuthenticationResponse;
		} else {
			throw new IllegalArgumentException("Not is User or STAF");
		}
	}

	public JwtAuthenticationResponse signinAdmin(SignInRequest signInRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

		var user = userRepository.findByEmail(signInRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid Email and PassWord"));
		if (user.getRole() == Role.ADMIN || user.getRole() == Role.STAF) {
			var jwt = jwtService.generateToken(user);
			var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(refreshToken);
			return jwtAuthenticationResponse;
		} else {
			throw new IllegalArgumentException("User is not an ADMIN or STAF");
		}
	}

	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());

		Auth user = userRepository.findByEmail(userEmail).orElseThrow();
		if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
			var jwt = jwtService.generateToken(user);
			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

			return jwtAuthenticationResponse;
		}
		return null;
	}

	public void updatePassword(String userEmail, String currentPassword, String newPassword) {
		Auth user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// Kiểm tra mật khẩu hiện tại
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new IllegalArgumentException("Invalid current password");
		}

		// Cập nhật mật khẩu mới
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public <S extends Auth> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Auth> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public Auth getOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Auth getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Auth getReferenceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Auth> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Auth> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Auth> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Auth entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Iterable<? extends Auth> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Auth> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Auth> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends Auth> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Auth> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Auth> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Auth, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Auth> getAuthById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

}