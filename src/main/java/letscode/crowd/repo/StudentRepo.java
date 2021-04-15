package letscode.crowd.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import letscode.crowd.domain.Student;


public interface StudentRepo extends JpaRepository<Student, Long> {

  @Query(value = "SELECT *\n"
      + "FROM Students\n"
      + "WHERE concat(last_name, ' ', patronymic, ' ', first_name) like concat('%', :name, '%')", nativeQuery = true)
  List<Student> findByName(@Param("name") String name);


  @Query(value = "SELECT *\n"
      + "FROM students\n"
      + "JOIN grps ON grps.id = students.grp_id\n"
      + "WHERE group_number like concat('%', :number, '%')", nativeQuery = true)
  List<Student> findByGroupNumber(@Param("number") String number);


}
