package letscode.crowd.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import letscode.crowd.component.GrpEditor;
import letscode.crowd.domain.Grp;
import letscode.crowd.domain.dto.StudentDto;
import letscode.crowd.repo.GrpRepo;

@Route("groups")
public class GrpList extends VerticalLayout {

  private final GrpRepo grpRepo;

  private final GrpEditor grpEditor;

  private Grid<Grp> grpGrid = new Grid<>(Grp.class, false);
  private final Button addNewButton = new Button("New group", VaadinIcon.PLUS.create());
  private final HorizontalLayout toolbar = new HorizontalLayout(addNewButton);

  @Autowired
  public GrpList(GrpRepo grpRepo, GrpEditor grpEditor) {
    this.grpRepo = grpRepo;
    this.grpEditor = grpEditor;

    add(toolbar, grpGrid, grpEditor);

    grpGrid.addColumn("id");
    grpGrid.addColumn("faculty");
    grpGrid.addColumn("groupNumber");


    fillList();
  }



  private void fillList() {
    List<StudentDto> studentDtoList = new ArrayList<>();

      grpGrid.setItems(grpRepo.findAll());
    }



}
