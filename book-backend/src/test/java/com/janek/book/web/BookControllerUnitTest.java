package com.janek.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janek.book.domain.BookInfo;
import com.janek.book.service.BookService;

import lombok.extern.slf4j.Slf4j;

// 단위 테스트(Controller, Filter, ControllerAdvice 관련 logic만 IoC에 띄우는 것) 

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean	// IoC 환경에 Bean으로 등록됨
	private BookService bookService; 
	
	// BDDMockito 패턴 given, when, then
	@Test
	public void save_Test() throws Exception {
		// given
		BookInfo book = new BookInfo(null, "TEST", "test man");	
		String content = new ObjectMapper().writeValueAsString(book);
		// ObjectMapper().writeValueAsString() : Object안의 데이터를 String으로 바꿔준다.
		when(bookService.saveBookInfo(book)).thenReturn(new BookInfo(1l, "TEST", "test man"));
		
		// when	(테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/book")	// post 방식으로 함수가 실행될 때
				.contentType(MediaType.APPLICATION_JSON_VALUE)		// 요청되는 데이터 형식
				.content(content)									// 전달되는 데이터
				.accept(MediaType.APPLICATION_JSON_VALUE));			// 응답받는 데이터 형식
			
		// then (검증)
		resultAction
			.andExpect(status().isCreated())					// 기대하는 결과 상태값 
			.andExpect(jsonPath("$.title").value("TEST"))		// 기대하는 결과 값
			.andDo(MockMvcResultHandlers.print());				// 결과 값을 콘솔에 출력
	}
	
	@Test
	public void findAll_Test() throws Exception {
		// given
		List<BookInfo> books = new ArrayList<>();
		books.add(new BookInfo(1L, "테스트1", "test"));
		books.add(new BookInfo(2L, "테스트2", "test"));
		
		when(bookService.getAllBookInfo()).thenReturn(books);
		
		// when
		ResultActions action = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_VALUE));
		
		// then
		action.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$.[0].title").value("테스트1"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_Test() throws Exception {
		// given
		Long id = 1l;
		BookInfo book = new BookInfo(1l, "TEST", "test man");
		when(bookService.getBookInfo(id)).thenReturn(book);
		
		// when
		ResultActions action = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_VALUE));
		
		//then
		action.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("TEST"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void updateById_test() throws Exception {
		// given
		Long id = 1l;
		BookInfo book = new BookInfo(null, "update", "test");
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookService.updateBookInfo(id, book)).thenReturn(new BookInfo(1l, "update", "test man"));
		
		//when
		ResultActions action = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)		// 요청되는 데이터 형식
				.content(content)									// 전달되는 데이터
				.accept(MediaType.APPLICATION_JSON_VALUE));			// 응답받는 데이터 형식
		
		//then
		action.andExpect(status().isOk())					// 기대하는 결과 상태값 
			.andExpect(jsonPath("$.title").value("update"))		// 기대하는 결과 값
			.andDo(MockMvcResultHandlers.print());				// 결과 값을 콘솔에 출력
	}
	
	@Test
	public void deleteBookInfo_test() throws Exception {
		//given
		Long id =1l;
		when(bookService.deleteBookInfo(id)).thenReturn("ok");
		
		// when
		ResultActions action = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN_VALUE));	// 성공시에 String 형식의 데이터를 받음
		
		// then
		action.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = action.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		// 응답 받은 데이터를 String으로 받음
		
		log.info(result);
		assertEquals("ok", result);
	}
}
