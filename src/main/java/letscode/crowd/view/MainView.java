package letscode.crowd.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;


@Route("")
public class MainView extends VerticalLayout {

  final Tabs tabs = new Tabs();


  @Autowired
  public MainView() {
    tabs.add(createMenuItems());
    tabs.setOrientation(Tabs.Orientation.VERTICAL);
    tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
    add(tabs);
  }


  private Component[] createMenuItems() {
    return new Tab[]{createTab("STUDENTS", StudentList.class), createTab("GROUPS", GrpList.class)};
  }

  private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
    final Tab tab = new Tab();
    tab.add(new RouterLink(text, navigationTarget));
    ComponentUtil.setData(tab, Class.class, navigationTarget);
    return tab;
  }
}
