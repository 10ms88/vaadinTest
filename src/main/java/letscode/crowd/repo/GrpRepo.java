package letscode.crowd.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import letscode.crowd.domain.Grp;
import letscode.crowd.domain.Student;

public interface GrpRepo extends JpaRepository<Grp, Long> {

  @Query(value = "SELECT grps.id,\n"
      + "       grps.faculty,\n"
      + "       grps.group_number\n"
      + "FROM grps\n"
      + "JOIN students ON grp_id =grps.id\n"
      + "GROUP BY grps.id", nativeQuery = true)
  List<Grp> findNotDeletedGroups();
}
