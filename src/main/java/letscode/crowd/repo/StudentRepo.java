package letscode.crowd.repo;

import letscode.crowd.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StudentRepo extends JpaRepository<Student, Long> {
    @Query(value =  "select * from Students  " +
            "where " +
            "   concat(lastName, ' ', patronymic, ' ', firstName) like concat('%', ?1, '%')", nativeQuery = true)
    List<Student> findByName(String name);



    @Query(value =  "select * from Students where grp_id = ?1", nativeQuery = true)
    List<Student> findByGroupId(Long grpId);


}
