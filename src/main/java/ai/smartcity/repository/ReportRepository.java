package ai.smartcity.repository;
import ai.smartcity.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserEmailOrderByCreatedAtDesc(String email);
}
