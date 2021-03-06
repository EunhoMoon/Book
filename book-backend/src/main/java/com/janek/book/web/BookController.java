package com.janek.book.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.janek.book.domain.BookInfo;
import com.janek.book.service.BookService;

import lombok.RequiredArgsConstructor;

// scurity(라이브러리 적용) - CORS 정책을 가지고 있다.(security가 cors를 해제해 줘야 한다. @CrossOrigin 사용이 아닌 필터를 따로 만들어줘야 함)
@RequiredArgsConstructor
@RestController
public class BookController {
	
	private final BookService bookService;
	
	@CrossOrigin
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody BookInfo book) {
		return new ResponseEntity<>(bookService.saveBookInfo(book), HttpStatus.CREATED);	// 200
	}

	@CrossOrigin
	@GetMapping("/book")
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(bookService.getAllBookInfo(), HttpStatus.OK);	// 200
	}
	
	@CrossOrigin
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) throws IllegalAccessException {
		return new ResponseEntity<>(bookService.getBookInfo(id), HttpStatus.OK);	// 200
	}
	
	@CrossOrigin
	@PutMapping("/book/{id}")
	public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody BookInfo book) {
		return new ResponseEntity<>(bookService.updateBookInfo(id, book), HttpStatus.OK);	// 200
	}
	
	@CrossOrigin
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		return new ResponseEntity<>(bookService.deleteBookInfo(id), HttpStatus.OK);	// 200
	}
	
}
