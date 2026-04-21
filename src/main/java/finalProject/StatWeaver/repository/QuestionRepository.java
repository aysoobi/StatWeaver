package finalProject.StatWeaver.repository;

import finalProject.StatWeaver.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}

