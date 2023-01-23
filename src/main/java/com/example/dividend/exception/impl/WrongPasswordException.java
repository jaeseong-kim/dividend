package com.example.dividend.exception.impl;

import com.example.dividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "패스워드가 틀립니다.";
	}
}
