package letscode.crowd.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import letscode.crowd.component.StudentEditor;
import letscode.crowd.domain.Student;
import letscode.crowd.domain.dto.StudentDto;
import letscode.crowd.repo.StudentRepo;

@Route("students")
public class StudentList extends VerticalLayout {

  private final StudentRepo studentRepo;

  private final StudentEditor studentEditor;

  private Grid<StudentDto> studentGrid = new Grid<>(StudentDto.class, false);
  private final TextField filterByName = new TextField();
  private final Button addNewButton = new Button("New student", VaadinIcon.PLUS.create());
  private final HorizontalLayout toolbar = new HorizontalLayout(filterByName, addNewButton);

  @Autowired
  public StudentList(StudentRepo studentRepo, StudentEditor studentEditor) {
    this.studentRepo = studentRepo;
    this.studentEditor = studentEditor;

    filterByName.setPlaceholder("Filter by name");
    filterByName.setValueChangeMode(ValueChangeMode.EAGER);
    filterByName.addValueChangeListener(field -> fillList(field.getValue()));

    add(toolbar, studentGrid, studentEditor);

    studentGrid.addColumn("id");
    studentGrid.addColumn("fullName");
    studentGrid.addColumn("birthday");
    studentGrid.addColumn("faculty");
    studentGrid.addColumn("groupNumber");
//        .asSingleSelect();
//        .addValueChangeListener(e -> studentEditor.editStudent( StudentDto.of(e.getValue())));

    addNewButton.addClickListener(e -> studentEditor.editStudent(new Student()));

    studentEditor.setChangeHandler(() -> {
      studentEditor.setVisible(false);
      fillList(filterByName.getValue());
    });

    fillList("");
  }

  private void fillList(String name) {
    List<StudentDto> studentDtoList = new ArrayList<>();

    if (name.isEmpty()) {

      this.studentRepo.findAll().forEach(student -> {
        studentDtoList.add(StudentDto.of(student));
      });

      studentGrid.setItems(studentDtoList);
    } else {
      this.studentRepo.findByName(name).forEach(student -> {
        studentDtoList.add(StudentDto.of(student));
      });

      studentGrid.setItems(studentDtoList);
    }
  }
}
