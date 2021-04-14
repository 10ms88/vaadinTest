package letscode.crowd.domain.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

import letscode.crowd.domain.Grp;
import letscode.crowd.domain.Student;
import letscode.crowd.domain.request.StudentRequest;

@Data
@Builder
public class StudentDto {

  private Long id;
  private String fullName;
  private LocalDate birthday;
  private String faculty;
  private String groupNumber;


  public static StudentDto of(Student student, Grp grp) {
    return StudentDto.builder()
        .id(student.getId())
        .fullName(student.getFirstName() + " " + student.getPatronymic() + " " + student.getLastName())
        .birthday(student.getBirthday())
        .faculty(grp.getFaculty())
        .groupNumber(grp.getGroupNumber())
        .build();
  }
}
