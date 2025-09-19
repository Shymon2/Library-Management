package Library.Project.service.implement;

import Library.Project.dto.request.library.BookSearchRequest;
import Library.Project.entity.Book;
import Library.Project.entity.Category;
import Library.Project.constant.enums.ErrorCodeFail;
import Library.Project.exception.AppException;
import Library.Project.repository.BookRepository;
import Library.Project.service.interfaces.IBookService;
import Library.Project.dto.request.library.BookRequestDTO;
import Library.Project.dto.request.library.CategoryDTO;
import Library.Project.dto.response.LibraryResponse.BookDetailResponse;
import Library.Project.dto.response.ApiResponse.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookService implements IBookService {
    private final ObjectMapper objectMapper;
    private final BookRepository bookRepository;
    private final CategoryService categoryService;


    @Override
    public List<BookDetailResponse> findBookByName(String name) {
        List<Book> book = bookRepository.findBookByName(name);

        if (Objects.isNull(book))
            throw new AppException(ErrorCodeFail.NOT_FOUND);

        List<BookDetailResponse> res = new ArrayList<>();
        for (Book b : book) {
            res.add(objectMapper.convertValue(b, BookDetailResponse.class));
        }

        return res;
    }

    private Set<CategoryDTO> convertToCategoryDTO(Set<Category> categories) {
        Set<CategoryDTO> categoryDTO = new HashSet<>();
        categories.forEach(a ->
                categoryDTO.add(CategoryDTO.builder()
                        .categoryName(a.getCategoryName())
                        .build())
        );

        return categoryDTO;
    }

    @Override
    public Object addNewBook(BookRequestDTO request) {
        if (existsByTitleAndAuthor(request.getTitle(), request.getAuthor())) {
            throw new AppException(ErrorCodeFail.ALREADY_EXISTED);
        }

        Book newbook = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .quantity(request.getQuantity())
                .categories(new HashSet<>())
                .build();

        //remove duplicate category
        Set<String> newCategoryName = new HashSet<>();
        request.getCategories().forEach(a -> newCategoryName.add(a.getCategoryName()));

        newCategoryName.forEach(a -> {
            Category category;

            if (categoryService.existsByCategoryName(a)) {
                // Fetch the existing Category entity
                category = categoryService.getCategoryByName(a);
            } else {
                // Create and save a new Category entity
                category = new Category();
                category.setCategoryName(a);
            }
            // Add category to the Book
            newbook.saveCategory(category);
        });
        bookRepository.save(newbook);

        return null;
    }

    @Override
    public PageResponse<List<BookDetailResponse>> findBooksByCriteria(BookSearchRequest request, int pageNo, int pageSize) {
        Page<Book> bookList = bookRepository.search(request.getTitle(), request.getAuthor(),
                request.getCategory(), PageRequest.of(pageNo - 1, pageSize));

        List<BookDetailResponse> responseList = new ArrayList<>();
        bookList.forEach(a -> {
            Set<String> categories = new HashSet<>();
            a.getCategories().forEach(b -> categories.add(b.getCategoryName()));

            responseList.add(BookDetailResponse.builder()
                    .title(a.getTitle())
                    .author(a.getAuthor())
                    .quantity(a.getQuantity())
                    .categories(categories)
                    .build());
        });
        return PageResponse.<List<BookDetailResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(bookList.getTotalPages())
                .items(responseList)
                .build();
    }


    @Override
    public PageResponse<List<BookDetailResponse>> findALlBook(int pageNo, int pageSize) {
        Page<Book> bookFound = bookRepository.findBookByIsDelete(PageRequest.of(pageNo - 1, pageSize), false);
        if (bookFound.isEmpty()) {
            throw new AppException(ErrorCodeFail.NOT_FOUND);
        }
        List<BookDetailResponse> bookList = new ArrayList<>();
        bookFound.getContent().forEach(a -> {
            Set<String> cateList = new HashSet<>();
            a.getCategories().forEach(b -> cateList.add(b.getCategoryName()));
            bookList.add(BookDetailResponse.builder()
                    .title(a.getTitle())
                    .author(a.getAuthor())
                    .quantity(a.getQuantity())
                    .categories(cateList)
                    .build());
        });
        return PageResponse.<List<BookDetailResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(bookFound.getTotalPages())
                .items(bookList)
                .build();
    }

    @Override
    public Object deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCodeFail.NOT_FOUND));
        book.getCategories().clear();
        bookRepository.save(book);
        bookRepository.delete(book);

        return null;
    }

    @Override
    public Object updateBook(BookRequestDTO request, Long id) {
        //find book by id
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCodeFail.NOT_FOUND));
        //get the old category
        Set<Category> oldCate = book.getCategories();

        //clear old category
        book.getCategories().clear();

        //set new casual info
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setQuantity(request.getQuantity());

        //remove duplicate category name
        Set<String> newCategoryName = new HashSet<>();
        request.getCategories().forEach(a -> newCategoryName.add(a.getCategoryName()));

        //add new category
        newCategoryName.forEach(a -> {
            Category category;

            if (categoryService.existsByCategoryName(a)) {
                // Fetch the existing Category entity
                category = categoryService.getCategoryByName(a);
            } else {
                // Create and save a new Category entity
                category = new Category();
                category.setCategoryName(a);
            }
            // Add category to the Book
            book.saveCategory(category);
        });

        //re-assign old category
        oldCate.forEach(book::saveCategory);

        bookRepository.save(book);

        return null;
    }

    @Override
    public void updateBookQuantity(Long bookId, int quantity) {
        Book book = bookRepository.findBookById(bookId);
        book.setQuantity(quantity);
        bookRepository.save(book);
    }

    @Override
    public Boolean existsByTitleAndAuthor(String title, String author) {
        return bookRepository.existsByTitleAndAuthor(title, author);
    }


}
