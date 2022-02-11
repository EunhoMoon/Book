import React, { useEffect, useState } from 'react';
import BookItem from '../../components/BookItem';

const Home = () => {
  const [books, setBooks] = useState([]);

  // 함수 실행시 최초 한 번 실행(useEffect)
  useEffect(() => {
    fetch('http://localhost:9111/book', {
      method: 'GET', // default가 GET이라 생략 가능
    })
      .then((res) => res.json())
      .then((res) => {
        setBooks(res);
      }); // 비동기 함수
  }, []);

  return (
    <div>
      {books.map((book) => (
        <div className="m-2" key={book.id}>
          <BookItem book={book} />
        </div>
      ))}
    </div>
  );
};

export default Home;
