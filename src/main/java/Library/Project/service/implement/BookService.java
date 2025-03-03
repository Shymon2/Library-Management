package Library.Project.service.implement;

import Library.Project.dto.Request.BookSearchRequest;
import Library.Project.entity.Book;
import Library.Project.entity.Category;
import Library.Project.enums.ErrorCode;
import Library.Project.exception.AppException;
import Library.Project.repository.BookRepository;
import Library.Project.service.interfaces.IBookService;
import Library.Project.dto.Request.BookRequestDTO;
import Library.Project.dto.Request.CategoryDTO;
import Library.Project.dto.Response.BookDetailResponse;
import Library.Project.dto.Response.PageResponse;
import Library.Project.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;


    @Override
    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
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
    public Book addNewBook(BookRequestDTO request) {
        if (existsByTitleAndAuthor(request.getTitle(), request.getAuthor())){
            Book book = bookRepository.findBookByTitleAndAuthor(request.getTitle(), request.getAuthor());
            book = updateBook(request, book.getId());
            bookRepository.save(book);
            return book;
        }
        else {
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
            return newbook;
        }
    }

    @Override
    public PageResponse findBooksByCriteria(BookSearchRequest request, int pageNo, int pageSize) {
        Specification<Book> spec = BookSpecification.filterBooks(request);
        Page<Book> bookList = bookRepository.findAll(spec, PageRequest.of(pageNo - 1, pageSize));

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
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(bookList.getTotalPages())
                .items(responseList)
                .build();
    }


    @Override
    public PageResponse findALlBook(int pageNo, int pageSize) {
        Page<Book> bookFound = bookRepository.findBookByIsDelete(PageRequest.of(pageNo - 1, pageSize), false);
        if(bookFound.isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
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
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(bookFound.getTotalPages())
                .items(bookList)
                .build();
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        book.getCategories().clear();
        bookRepository.save(book);
        bookRepository.delete(book);
    }

    @Override
    public Book updateBook(BookRequestDTO request, Long id) {
        //find book by id
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
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

        return book;
    }

    @Override
    public void updateBookQuantity(Long bookId, int quantity) {
        Book book = findBookById(bookId);
        book.setQuantity(quantity);
        bookRepository.save(book);
    }

    @Override
    public Boolean existsByTitleAndAuthor(String title, String author) {
        return bookRepository.existsByTitleAndAuthor(title, author) ;
    }

    public BookDetailResponse convertToResponse(Book book){
        Set<String> categories = new HashSet<>();
        book.getCategories().forEach(a -> categories.add(a.getCategoryName()));
        return BookDetailResponse.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .quantity(book.getQuantity())
                .categories(categories)
                .build();
    }
}
