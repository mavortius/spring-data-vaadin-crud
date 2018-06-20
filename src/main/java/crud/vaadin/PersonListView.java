package crud.vaadin;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.data.provider.Sort;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import crud.backend.Person;
import crud.backend.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@SpringView(name = PersonListView.VIEW_NAME)
public class PersonListView extends MVerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    static final String VIEW_NAME = "persons";

    private PersonRepository repo;
    private PersonForm personForm;
    private EventBus.UIEventBus eventBus;

    private MGrid<Person> list = new MGrid<>(Person.class)
            .withProperties("name", "email")
            .withColumnHeaders("Name", "Email")
            // not yet supported by V8
            //.setSortableProperties("name", "email")
            .withFullWidth();

    private MTextField filterByName = new MTextField()
            .withPlaceholder("Filter by name");
    private Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Are you sure you want to delete the entry?", this::remove);

    private FilterablePageableDataProvider<Person, Object> dataProvider = new FilterablePageableDataProvider<Person, Object>() {
        @Override
        protected Page<Person> fetchFromBackEnd(Query<Person, Object> query, Pageable pageable) {
            return repo.findByNameContainsIgnoreCase(getFilter(), pageable);
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
            return Math.toIntExact(repo.countByNameContainsIgnoreCase(getFilter()));
        }
    };

    public PersonListView(PersonRepository r, PersonForm f, EventBus.UIEventBus b) {
        this.repo = r;
        this.personForm = f;
        this.eventBus = b;
    }

    @PostConstruct
    void init() {
        addComponents(new MHorizontalLayout(filterByName, addNew, edit, delete), list);
        expand(list);
        list.setDataProvider(dataProvider);
        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(e -> setFilter(e.getValue()));

        // Listen to change events emitted by PersonForm see onEvent method
        eventBus.subscribe(this);
    }

    private void setFilter(String value) {
        dataProvider.setFilter(value);
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void add(Button.ClickEvent clickEvent) {
        edit(new Person());
    }

    private void edit(Button.ClickEvent clickEvent) {
        edit(list.asSingleSelect().getValue());
    }

    private void remove() {
        repo.delete(list.asSingleSelect().getValue());
        list.deselectAll();
        Notification.show("Person deleted", Notification.Type.WARNING_MESSAGE);
        setFilter("");
    }

    private void edit(final Person phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(PersonModifiedEvent event) {
        setFilter("");
        personForm.closePopup();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
