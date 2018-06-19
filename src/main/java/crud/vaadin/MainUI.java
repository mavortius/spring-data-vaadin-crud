package crud.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemusa.sidemenu.SideMenu;

@Title(MainUI.APP_TITLE)
@Theme(ValoTheme.THEME_NAME)
@Viewport("user-scalable=no,initial-scale=1.0")
@SpringUI
@SpringViewDisplay
public class MainUI extends UI implements ViewDisplay {
    private static final long serialVersionUID = 1L;

    public static final String APP_TITLE = "PhoneBook";

    private SideMenu sideMenu;

    @Override
    protected void init(VaadinRequest request) {
        //ClassResource logo = new ClassResource("/images/logo.png");

        sideMenu = new SideMenu();
        sideMenu.setMenuCaption(APP_TITLE);
        sideMenu.addNavigation("Home", VaadinIcons.HOME, HomeView.VIEW_NAME);
        sideMenu.addNavigation("Person List", PersonListView.VIEW_NAME);

        setContent(sideMenu);
    }

    @Override
    public void showView(View view) {
        sideMenu.setContent((Component) view);
    }
}
