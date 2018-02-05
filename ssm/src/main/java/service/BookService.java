package service;

import dto.AppointExecution;
import entity.Book;

import java.util.List;

public interface BookService {
    /**
     * 查询一本图书
     *
     * @param bookId
     * @return
     */
    Book getById(long bookId);

    /**
     * 查询所有图书
     *
     * @return
     */
    List<Book> getList();

    /**
     * 预约图书
     *
     * @return
     */
    AppointExecution appoint(long bookId,long studentId);
}
