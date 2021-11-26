package domain.model;

public interface Permission {
    boolean isAdminPermission();
    boolean isProfessorPermission();
    boolean isStudentPermission();
}
