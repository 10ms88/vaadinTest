package letscode.crowd.component;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import letscode.crowd.domain.Student;
import letscode.crowd.repo.GrpRepo;
import letscode.crowd.repo.StudentRepo;


@SpringComponent
@UIScope
public class StudentEditor extends VerticalLayout {

  private final StudentRepo studentRepo;
  private final GrpRepo grpRepo;
  private Student student;

  private final TextField firstName = new TextField("First name");
  private final TextField lastName = new TextField("Last name");
  private final TextField patronymic = new TextField("Patronymic");
  private final TextField grpId = new TextField("Group ID");
  private final DatePicker birthday = new DatePicker("Birthday");

  private final Button save = new Button("Save");
  private final Button cancel = new Button("Cancel");
  private final Button delete = new Button("Delete");
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
    validateBinder();
    setSpacing(true);

    save.getElement().getThemeList().add("primary");
    delete.getElement().getThemeList().add("error");

    save.addClickListener(e -> {
      if (!binder.isValid()) {
        Notification.show("Fields are filled incorrectly!");
      } else {
        save();
      }
    });

    delete.addClickListener(e -> delete());
    cancel.addClickListener(e -> setVisible(false));
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


  public void validateBinder() {
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

    binder.forField(grpId)
        .withValidator(
            this::groupIdCheck, "Group ID incorrect or not exist")
        .bind(Student::getGrpId, Student::setGrpId);
  }

  private boolean groupIdCheck(String groupId) {
    boolean isCorrect = false;
    if (isLong(groupId)) {
      if (groupIdList().contains(Long.valueOf(groupId))) {
        isCorrect = true;
      }
    }
    return isCorrect;
  }

  public boolean isLong(String string) {
    Pattern pattern = Pattern.compile("^[0-9]+$");
    Matcher matcher = pattern.matcher(string);
    return matcher.matches();
  }

  public List<Long> groupIdList() {
    List<Long> groupIdList = new ArrayList<>();
    grpRepo.findAll().forEach(grp -> {
      groupIdList.add(grp.getId());
    });
    return groupIdList;
  }

}
