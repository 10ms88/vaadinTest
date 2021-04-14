package letscode.crowd.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import letscode.crowd.domain.Grp;
import letscode.crowd.domain.Student;
import letscode.crowd.repo.GrpRepo;

@SpringComponent
@UIScope
public class GrpEditor extends VerticalLayout implements KeyNotifier {
  private final GrpRepo grpRepo;

  private Grp grp;

  private TextField faculty = new TextField("Faculty");
  private TextField groupNumber = new TextField("GroupNumber");

  private Button save = new Button("Save");
  private Button cancel = new Button("Cancel");

  private HorizontalLayout buttons = new HorizontalLayout(save, cancel);

  private Binder<Grp> binder = new Binder<>(Grp.class);


  @Setter
  private ChangeHandler changeHandler;

  public interface ChangeHandler {
    void onChange();
  }


  @Autowired
  public GrpEditor(GrpRepo grpRepo) {
    this.grpRepo = grpRepo;

    add(faculty, groupNumber, buttons);

//    binder.forField(groupNumber).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
//        .bind("groupNumber");

    binder.bindInstanceFields(this);

    setSpacing(true);

    save.getElement().getThemeList().add("primary");

    addKeyPressListener(Key.ENTER, e -> save());

    save.addClickListener(e -> save());
    cancel.addClickListener(e -> setVisible(false));
    setVisible(false);
  }


  private void save() {
    grpRepo.save(grp);
    changeHandler.onChange();
  }

  public void editGrp(Grp grp) {
    if (grp == null) {
      setVisible(false);
      return;
    }

    if (grp.getId() != null) {
      this.grp = grpRepo.findById(grp.getId()).orElse(grp);
    } else {
      this.grp = grp;
    }

    binder.setBean(this.grp);
    setVisible(true);
    faculty.focus();
  }

}
