package letscode.crowd.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import letscode.crowd.domain.Student;
import letscode.crowd.repo.StudentRepo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class StudentEditor extends VerticalLayout implements KeyNotifier {
    private final StudentRepo studentRepo;

    private Student student;

    private TextField firstName = new TextField("", "First name");
    private TextField lastName = new TextField("", "Last name");
    private TextField patronymic = new TextField("", "Patronymic");


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
    public StudentEditor(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;

        add(lastName, firstName, patronymic, buttons);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editStudent(student));
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

    public void editStudent(Student emp) {
        if (emp == null) {
            setVisible(false);
            return;
        }

        if (emp.getId() != null) {
            this.student = studentRepo.findById(emp.getId()).orElse(emp);
        } else {
            this.student = emp;
        }

        binder.setBean(this.student);
        setVisible(true);
        lastName.focus();
    }
}
