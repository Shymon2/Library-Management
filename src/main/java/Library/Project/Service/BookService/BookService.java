package Library.Project.Service.BookService;

import Library.Project.Configuration.Translator;
import Library.Project.Exception.ResourcesNotFoundException;
import Library.Project.Model.Book;
import Library.Project.Model.Category;
import Library.Project.Repository.BookRepository;
import Library.Project.Service.CategoryService.CategoryService;
import Library.Project.dto.Request.BookRequestDTO;
import Library.Project.dto.Request.CategoryDTO;
import Library.Project.dto.Response.BookDetailResponse;
import Library.Project.dto.Response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                new ResourcesNotFoundException(Translator.toLocale("book.not.found")));
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
    public List<BookDetailResponse> findBookByTitle(String title) {
        List<Book> bookList = bookRepository.findByTitle(title);
        if (bookList.isEmpty()) {
            throw new ResourcesNotFoundException(Translator.toLocale("book.not.found"));
        }
        return convertToBookDetailResponse(bookList);
    }

    private List<BookDetailResponse> convertToBookDetailResponse(List<Book> bookList) {
        List<BookDetailResponse> bookDetailResponseList = new ArrayList<>();
        bookList.forEach(b ->
                bookDetailResponseList.add(BookDetailResponse.builder()
                        .title(b.getTitle())
                        .author(b.getAuthor())
                        .quantity(b.getQuantity())
                        .categories(convertToCategoryDTO(b.getCategories()))
                        .build())
        );

        return bookDetailResponseList;
    }

    @Override
    public List<BookDetailResponse> findBookByAuthor(String author) {
        List<Book> bookList = bookRepository.findByAuthor(author);
        if (bookList.isEmpty()) {
            throw new ResourcesNotFoundException(Translator.toLocale("book.not.found"));
        }
        return convertToBookDetailResponse(bookList);
    }

    @Override
    public List<BookDetailResponse> findBookByTitleAndAuthor(String title, String author) {
        List<Book> bookList = bookRepository.findByTitleAndAuthor(title, author);
        if (bookList.isEmpty()) {
            throw new ResourcesNotFoundException(Translator.toLocale("book.not.found"));
        }
        return convertToBookDetailResponse(bookList);
    }

    @Override
    public PageResponse findALlBook(int pageNo, int pageSize) {
        Page<Book> bookFound = bookRepository.findAll(PageRequest.of(pageNo - 1, pageSize));
        if(bookFound.isEmpty()){
            throw new ResourcesNotFoundException(Translator.toLocale("book.library.empty"));
        }
        List<BookDetailResponse> bookList = bookFound.stream().map(book -> BookDetailResponse.builder()
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .quantity(book.getQuantity())
                        .categories(convertToCategoryDTO(book.getCategories()))
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(bookFound.getTotalPages())
                .items(bookList)
                .build();
    }

    @Override
    public Book deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResourcesNotFoundException(Translator.toLocale("book.not.found")));
        book.getCategories().clear();
        bookRepository.save(book);
        bookRepository.delete(book);
        return book;
    }

    @Override
    public Book updateBook(BookRequestDTO request, Long id) {
        //find book by id
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResourcesNotFoundException(Translator.toLocale("book.not.found")));
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


}
