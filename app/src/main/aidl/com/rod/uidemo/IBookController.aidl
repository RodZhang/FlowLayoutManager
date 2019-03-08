// IBookController.aidl
package com.rod.uidemo;

import com.rod.uidemo.Book;

interface IBookController {

    List<Book> getBooks();

    int addBook(inout Book book);
}
