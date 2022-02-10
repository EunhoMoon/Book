package com.janek.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.janek.book.domain.BookRepository;

/*
 * 단위 테스트(Service 관련)
 * BookRepository -> 가짜 객체로 만들 수 있다.
*/

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	@InjectMocks	
	// BookService객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 객체를 주입
	private BookService bookService;
	
	@Mock
	private BookRepository bookRepository;
	
}
