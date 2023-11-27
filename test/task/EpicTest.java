package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void emptySubTaskList() {
        Epic ep = new Epic("new", "descriprion");
        List<SubTask> expected = new ArrayList<>();
        assertArrayEquals(expected.toArray(), ep.getSubTaskForEpic().toArray());
    }

    @Test
    public void allEpicSubTasksStatusNew() {
        Epic ep = new Epic("new", "descriprion");
        List<SubTask> expected = new ArrayList<>();
        expected.add(new SubTask("n1", "d1", "NEW", ep.getId(),"11-11_11.12.2023", 20));
        expected.add(new SubTask("n2", "d2", "NEW", ep.getId(),"11-11_11.12.2023", 20));
        String exp = "NEW";
        for (SubTask s : expected) {
            if (!s.status.equals(exp)) {
                exp = s.status;
                break;
            }
        }
        assertEquals("NEW", exp);
    }

    @Test
    public void allEpicSubTasksStatusDone() {
        Epic ep = new Epic("new", "descriprion");
        List<SubTask> expected = new ArrayList<>();
        expected.add(new SubTask("n1", "d1", "DONE", ep.getId(),"22-22_22.12.2023", 25));
        expected.add(new SubTask("n2", "d2", "DONE", ep.getId(),"22-22_22.12.2023", 25));
        String exp = "DONE";
        for (SubTask s : expected) {
            if (!s.status.equals(exp)) {
                exp = s.status;
                break;
            }
        }
        assertEquals("DONE", exp);
    }


    @Test
    public void allEpicSubTasksStatusNewAndDone() {
        Epic ep = new Epic("new", "descriprion");

        List<SubTask> expected = new ArrayList<>();
        expected.add(new SubTask("n1", "d1", "NEW", ep.getId(),"22-22_22.12.2023", 25));
        expected.add(new SubTask("n2", "d2", "DONE", ep.getId(),"22-22_22.12.2023", 25));

        String exp = "ERROR";
        int counter1 = 0;
        int counter2 = 0;

        for (SubTask s : expected) {
            if (s.status.equals("NEW")) {
                counter1++;
            } else if (s.status.equals("DONE")) {
                counter2++;
            }
        }
        if (counter1 >= 1 && counter2 >= 1) {
            exp = "IN_PROGRESS";
        }
        assertEquals("IN_PROGRESS", exp);
    }

    @Test
    public void allEpicSubTasksStatusInProgress() {
        Epic ep = new Epic("new", "descriprion");

        List<SubTask> expected = new ArrayList<>();
        expected.add(new SubTask("n1", "d1", "IN_PROGRESS", ep.getId(),"08-09_09.05.2024", 56));
        expected.add(new SubTask("n2", "d2", "IN_PROGRESS", ep.getId(),"09-29_09.07.2024", 26));

        String exp = "IN_PROGRESS";
        for (SubTask s : expected) {
            if (!s.status.equals(exp)) {
                exp = s.status;
                break;
            }
        }
        assertEquals("IN_PROGRESS", exp);
    }


}