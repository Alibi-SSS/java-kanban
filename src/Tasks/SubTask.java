package Tasks;

import Managers.Task;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId; // ID эпика, к которому относится подзадача

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;

    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        return getId() == ((SubTask) o).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}