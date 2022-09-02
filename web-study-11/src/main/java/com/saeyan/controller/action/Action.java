package com.saeyan.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	// 추상메서드
	//모든 모델들은 액션을 상속받아야 하므로 액션 객체라고 부른다.
	//팩토리 패턴 . 요청이 들어오면 요청에 대한
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;



}
