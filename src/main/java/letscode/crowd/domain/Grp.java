package letscode.crowd.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "grps")
public class Grp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String faculty;
  private String groupNumber;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "grpId", cascade = CascadeType.ALL)
  private List<Student> studentList;
}
