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

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janek.book.domain.BookInfo;
import com.janek.book.domain.BookRepository;

/*
 * 통합 테스트(모든 Bean들을 IoC에 올리고 테스트하는 것)
 * @SpringBootTest(webEnvironment = WebEnvironment.MOCK) -> 실제 톰캣이 아닌 다른 톰캣으로 테스트
 * @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) -> 실제 톰캐을 통해 테스트
 * @AutoConfigureMockMvc -> MockMvc를 IoC에 등록해줌
 * @Transactional -> 각각의 테스트 함수가 종료될 때 마다 트랜잭션을 rollback
*/

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 실제 톰캣이 아닌 다른 톰캣으로 테스트
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach	// 모든 테스트 함수가 각각 실행된다.
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE bookinfo AUTO_INCREMENT = 3").executeUpdate();
	}

	// BDDMockito 패턴 given, when, then
	@Test
	public void save_Test() throws Exception {
		// given
		BookInfo book = new BookInfo(null, "TEST", "test man");
		String content = new ObjectMapper().writeValueAsString(book);

		// when (테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/book") 	// post 방식으로 함수가 실행될 때
				.contentType(MediaType.APPLICATION_JSON_VALUE) 		// 요청되는 데이터 형식
				.content(content) 									// 전달되는 데이터
				.accept(MediaType.APPLICATION_JSON_VALUE));			// 응답받는 데이터 형식

		// then (검증)
		resultAction.andExpect(status().isCreated()) 				// 기대하는 결과 상태값
				.andExpect(jsonPath("$.title").value("TEST")) 		// 기대하는 결과 값
				.andDo(MockMvcResultHandlers.print()); 				// 결과 값을 콘솔에 출력
	}
	
	@Test
	public void findAll_Test() throws Exception {
		// given
		List<BookInfo> books = new ArrayList<>();
		books.add(new BookInfo(null, "테스트1", "test"));
		books.add(new BookInfo(null, "테스트2", "test"));
		bookRepository.saveAll(books);
		
		// when
		ResultActions action = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_VALUE));
		
		// then
		action.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findById_Test() throws Exception {
		// given
		Long id = 3l;
		BookInfo book = new BookInfo(null, "TEST", "test man");
		bookRepository.save(book);
		
		// when
		ResultActions action = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_VALUE));
		
		//then
		action.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("TEST"))
			.andExpect(jsonPath("$.id").value(3))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void updateById_test() throws Exception {
		// given
		Long id = 3l;
		BookInfo book1 = new BookInfo(null, "TEST", "test man");
		bookRepository.save(book1);
		BookInfo book2 = new BookInfo(null, "update", "test");
		String content = new ObjectMapper().writeValueAsString(book2);
		
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
		Long id =3l;
		BookInfo book1 = new BookInfo(null, "TEST", "test man");
		bookRepository.save(book1);
		
		// when
		ResultActions action = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN_VALUE));	// 성공시에 String 형식의 데이터를 받음
		
		// then
		action.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = action.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		// 응답 받은 데이터를 String으로 받음
		
		assertEquals("oK", result);
	}
}
