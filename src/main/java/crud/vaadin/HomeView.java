package crud.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.label.RichText;

import javax.annotation.PostConstruct;

@SpringView(name = HomeView.VIEW_NAME)
public class HomeView extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    static final String VIEW_NAME = "";

    @PostConstruct
    void init() {
        //DisclosurePanel aboutBox = new DisclosurePanel("Spring Boot JPA CRUD example with Vaadin UI", new RichText().withMarkDownResource("/welcome.md"));
        addComponent(new RichText().withMarkDownResource("/welcome.md"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
