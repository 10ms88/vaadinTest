package letscode.crowd.domain.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

import letscode.crowd.domain.Student;

@Data
@Builder
public class StudentDto {

  private Long id;
  private String fullName;
  private LocalDate birthday;
  private String faculty;
  private Integer groupNumber;


  public static StudentDto of(Student student) {
    return StudentDto.builder()
        .id(student.getId())
        .fullName(student.getFirstName() + " " + student.getPatronymic() + " " + student.getLastName())
        .birthday(student.getBirthday())
        .faculty(student.getGrp().getFaculty())
        .groupNumber(student.getGrp().getGroupNumber())
        .build();
  }


}
