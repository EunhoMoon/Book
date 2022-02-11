import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';

const SaveForm = (props) => {
  const [book, setbook] = useState({
    title: '',
    author: '',
  });

  const changeValue = (e) => {
    // 호출한 input의 값이 변경될 때 마다 그 값을 받는다.
    setbook({
      ...book, // 기존값의 유지를 위해
      [e.target.name]: e.target.value, // 새로 받은 값을 덮어씌운다.
      // [e.target...] : 객체 안에서 key를 [ ]로 감싸면 그 안에 넣은 레퍼런스가 가리키는 실제 값이 key 값으로 사용
    });
  };

  const submitBook = (e) => {
    e.preventDefault(); // submit이 action을 하지 못하게 막음
    fetch('http://localhost:9111/book', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book), // java script 객체를 json으로 변경하여 요청
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === 201) {
          // 요청에 따른 응답의 status값을 비교
          return res.json;
        } else {
          return null;
        }
      })
      .then((res) => {
        if (res != null) {
          props.history.push('/');
        } else {
          alert('책 등록에 실패하였습니다.');
        }
      });
  };

  return (
    <Form onSubmit={submitBook}>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>Title</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter title"
          onChange={changeValue}
          name={'title'}
        />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>Author</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter author"
          onChange={changeValue}
          name={'author'}
        />
      </Form.Group>
      <Button variant="primary" type="submit">
        Submit
      </Button>
    </Form>
  );
};

export default SaveForm;
