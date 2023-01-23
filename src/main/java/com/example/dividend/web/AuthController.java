package com.example.dividend.web;

import com.example.dividend.model.Auth;
import com.example.dividend.persist.entity.MemberEntity;
import com.example.dividend.security.TokenProvider;
import com.example.dividend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final MemberService memberService;

	private final TokenProvider tokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request) {
		MemberEntity result = this.memberService.register(request);

		return ResponseEntity.ok(result);
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request) {
		var member = this.memberService.authenticate(request);
		var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
		log.info("user login -> " + request.getUsername());
		return ResponseEntity.ok(token);
	}


}
