package letscode.crowd.component;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Position;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import letscode.crowd.domain.Student;
import letscode.crowd.repo.GrpRepo;
import letscode.crowd.repo.StudentRepo;


@SpringComponent
@UIScope
public class StudentEditor extends VerticalLayout implements KeyNotifier {

  private final StudentRepo studentRepo;
  private final GrpRepo grpRepo;

  private Student student;

  private TextField firstName = new TextField("First name");
  private TextField lastName = new TextField("Last name");
  private TextField patronymic = new TextField("Patronymic");
  private DatePicker birthday = new DatePicker("Birthday");
  private TextField grpId = new TextField("GroupId");


  private Button save = new Button("Save");
  private Button cancel = new Button("Cancel");
  private Button delete = new Button("Delete");

  private HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

  private Binder<Student> binder = new Binder<>(Student.class);

  @Setter
  private ChangeHandler changeHandler;

  public interface ChangeHandler {

    void onChange();
  }

  @Autowired
  public StudentEditor(StudentRepo studentRepo, GrpRepo grpRepo) {
    this.studentRepo = studentRepo;
    this.grpRepo = grpRepo;

    HorizontalLayout fields = new HorizontalLayout(firstName, lastName, patronymic, birthday, grpId);

    add(fields, buttons);

    binder.forField(firstName)
        .withValidator(
            fName -> fName.length() >= 3,
            "FirstName must contain at least three characters")
        .bind(Student::getFirstName, Student::setFirstName);

    binder.forField(lastName)
        .withValidator(
            lName -> lName.length() >= 3,
            "LastName must contain at least three characters")
        .bind(Student::getLastName, Student::setLastName);

    binder.forField(patronymic)
        .withValidator(
            ptrnm -> ptrnm.length() >= 3,
            "Patronymic must contain at least three characters")
        .bind(Student::getPatronymic, Student::setPatronymic);


      binder.forField(birthday)
          .withValidator(
              bDay -> bDay != null && bDay.toEpochDay() <= LocalDate.now().minusYears(15).toEpochDay(),
              "Student must be older then 15 y.o.")
          .bind(Student::getBirthday, Student::setBirthday);


    setSpacing(true);

    save.getElement().getThemeList().add("primary");
    delete.getElement().getThemeList().add("error");

    save.addClickListener(e -> {
      if (!validateFields(student)) {
         Notification.show("Fields are filled incorrectly ");
      } else {
        save();
      }
    });


//    addKeyPressListener(Key.ENTER, e -> save());

    delete.addClickListener(e ->

        delete());
    cancel.addClickListener(e ->

        setVisible(false));

    setVisible(false);

  }

  private void save() {
    studentRepo.save(student);
    changeHandler.onChange();
  }

  private void delete() {
    studentRepo.delete(student);
    changeHandler.onChange();
  }

  public void editStudent(Student stdnt) {
    if (stdnt == null) {
      setVisible(false);
      return;
    }

    if (stdnt.getId() != null) {
      this.student = studentRepo.findById(stdnt.getId()).orElse(stdnt);
    } else {
      this.student = stdnt;
    }

    binder.setBean(this.student);
    setVisible(true);
    firstName.focus();
  }


  public List<Long> groupIdList() {
    List<Long> groupIdList = new ArrayList<>();

    grpRepo.findAll().forEach(grp -> {
      groupIdList.add(grp.getId());
    });
    return groupIdList;

  }


  public boolean validateFields(Student student) {
    boolean isValid = false;
    if (student != null) {
      if (student.getLastName() != null || student.getPatronymic() != null || student.getBirthday() != null) {

        isValid = student.getLastName().length() > 2 ||
            student.getPatronymic().length() > 2 ||
            student.getBirthday().toEpochDay() <= LocalDate.now().minusYears(15).toEpochDay();
      }
    }
    return isValid;


  }
}
