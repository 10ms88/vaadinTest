package letscode.crowd.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import letscode.crowd.component.GrpEditor;
import letscode.crowd.domain.Grp;
import letscode.crowd.repo.GrpRepo;

@Route("groups")
public class GrpList extends VerticalLayout {

  private final GrpRepo grpRepo;
  private final GrpEditor grpEditor;
  private Grid<Grp> grpGrid = new Grid<>(Grp.class, false);
  private final Button studentsButton = new Button("Goes to STUDENTS");
  private final Button addNewButton = new Button("New group", VaadinIcon.PLUS.create());
  private final HorizontalLayout toolbar = new HorizontalLayout(studentsButton, addNewButton);

  @Autowired
  public GrpList(GrpRepo grpRepo, GrpEditor grpEditor) {
    this.grpRepo = grpRepo;
    this.grpEditor = grpEditor;

    add(toolbar, grpEditor, grpGrid);

    grpGrid.addColumn("id");
    grpGrid.addColumn("faculty");
    grpGrid.addColumn("groupNumber");

    grpGrid.asSingleSelect().addValueChangeListener(e -> {
      grpEditor.editGrp(e.getValue());
    });

    studentsButton.addClickListener(e -> studentsButton.getUI().ifPresent(ui -> ui.navigate("students")));
    addNewButton.addClickListener(e -> grpEditor.editGrp(new Grp()));

    grpEditor.setChangeHandler(() -> {
      grpEditor.setVisible(false);
      fillList();
    });

    fillList();
  }

  private void fillList() {
    grpGrid.setItems(grpRepo.findAll());
  }
}
