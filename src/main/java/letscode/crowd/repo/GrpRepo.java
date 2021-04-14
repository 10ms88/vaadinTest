package letscode.crowd.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import letscode.crowd.domain.Grp;

public interface GrpRepo extends JpaRepository<Grp, Long> {

  @Query(value = "select * from Grps  " +
      "where faculty like concat('%', :faculty, '%')", nativeQuery = true)
  Optional<Grp> findByFaculty(@Param("faculty") String faculty);

}
