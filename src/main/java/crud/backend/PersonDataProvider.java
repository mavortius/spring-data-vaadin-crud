package crud.backend;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.data.provider.Sort;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.util.List;

@SpringComponent
public class PersonDataProvider extends FilterablePageableDataProvider<Person, Object> {

    private final PersonRepository repository;

    public PersonDataProvider(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Page<Person> fetchFromBackEnd(Query<Person, Object> query, Pageable pageable) {
        return repository.findByNameContainsIgnoreCase(getFilter(), pageable);
    }

    private String getFilter() {
        return getOptionalFilter().orElse("");
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return Sort.asc("name").build();
    }

    @Override
    protected int sizeInBackEnd(Query<Person, Object> query) {
        return Math.toIntExact(repository.countByNameContainsIgnoreCase(getFilter()));
    }
}
