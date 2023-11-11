package com.ezd.service.Impl;

import com.ezd.Dto.DataMailDTO;
import com.ezd.Dto.JwtAuthenticationResponse;
import com.ezd.Dto.RefreshTokenRequest;
import com.ezd.Dto.Role;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;
import com.ezd.models.StatusAccount;
import com.ezd.repository.AuthRepository;
import com.ezd.service.AuthenticationService;
import com.ezd.service.JwtService;
import com.ezd.service.MailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	@Autowired
    private MailService mailService;

	private final AuthRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
    private final SpringTemplateEngine templateEngine; // Tiêm SpringTemplateEngine vào đây


	 public Auth signup(SignUpRequest signUpRequest) {
	        Optional<Auth> existingUser = userRepository.findByEmail(signUpRequest.getEmail());
	        if (existingUser.isPresent()) {
	            throw new RuntimeException("Email đã tồn tại.");
	        }

	        Auth user = new Auth();
	        user.setAvatar(null);
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

	        // Thay đổi cách gọi phương thức generateHtmlContent
	        String htmlContent = generateHtmlContent("email-template", Map.of("email", signUpRequest.getEmail(), "password", signUpRequest.getPassword()));

	        DataMailDTO mailStructure = new DataMailDTO();
	        mailStructure.setSubject("Xác Nhận Đăng Ký Tài Khoản Thành Công");
	        mailStructure.setContent(htmlContent);

	        mailService.sendHtmlMail(mailStructure, signUpRequest.getEmail());

	        return userRepository.save(user);
	    }

	    private String generateHtmlContent(String templateName, Map<String, Object> model) {
	        // Sử dụng templateEngine để xử lý template
	        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	        templateEngine.setTemplateResolver(thymeleafTemplateResolver());

	        // Thay đổi cách lấy templateEngine từ context
	        Context context = new Context();
	        context.setVariables(model);
	        return templateEngine.process(templateName, context);
	    }

	    private ClassLoaderTemplateResolver thymeleafTemplateResolver() {
	        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
	        templateResolver.setPrefix("templates/");
	        templateResolver.setSuffix(".html");
	        templateResolver.setTemplateMode("HTML");
	        templateResolver.setCharacterEncoding("UTF-8");
	        return templateResolver;
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
}
