import java.util.*;


public interface StudentRepository {
    void save(StudentRecord record);
    int count();
    List<StudentRecord> all();
}
