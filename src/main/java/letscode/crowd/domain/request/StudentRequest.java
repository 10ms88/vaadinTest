package letscode.crowd.domain.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentRequest {

  private String firstName;
  private String lastName;
  private String patronymic;
  private LocalDate birthday;
  private String groupId;

}
