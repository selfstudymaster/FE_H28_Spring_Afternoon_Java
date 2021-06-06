package main;
// 基本情報 H28 春季 午後 Java

import java.util.UUID; // プログラム1
import java.util.ArrayList; // プログラム2
import java.util.List; // プログラム2

public class Main {

}

// [プログラム 1]
/*public*/ class ToDo {
    public enum Priority { LOW, MIDDLE, HIGH }
    public enum State { NOT_YET_STARTED, STARTED, DONE }

    private static final String DEADLINE_PATTERN = "\\d{8}|\\d{12}";

    private final String id;
    private String subject;
    private String deadline;
    private Priority priority;
    private State state;

    private ToDo(String subject, String deadline, Priority priority, String id, State state) {
        if (!deadline.matches(DEADLINE_PATTERN)) {
            throw new IllegalArgumentException();
        }
        this.subject = subject;
        this.deadline = deadline;
        this.priority = priority;
        this.id = id;
        this.state = state;
    }

    public ToDo(String subject, String deadline, Priority priority) {
        this(subject, deadline, priority, UUID.randomUUID().toString(), State.NOT_YET_STARTED);
    }

    public ToDo(ToDo todo) {
        this(todo.subject, todo.deadline, todo.priority, todo.id, todo.state);
    }

    public String getSubject() { return subject; }
    public String getDeadline() { return deadline; }
    public Priority getPriority() { return priority; }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public int hashCode() { return id.hashCode(); }

    public boolean equals(Object o) {
        return o instanceof ToDo && ((ToDo) o).id.equals(id); /* return o instanceof ToDo && (a); */
    }

    public String toString() {
        return String.format("主題: %s, 期限: %s, 優先度: %s, 状態: %s", subject, deadline, priority, state);
    }

}

// [プログラム 2]
//import java.util.ArrayList; // 先頭に記述済
// import java.util.List; // 先頭に記述済

/*public*/ class ToDoList {
    private List<ToDo> todoList = new ArrayList<ToDo>();

    public void add(ToDo todo) {
        if (!todoList.contains(todo)) { // if (/*(b)*/) {
            todoList.add(new ToDo(todo));
        }
    }

    public void update(ToDo todo) {
        int index = todoList.indexOf(todo);
        if (index != -1) { // if (index /*(c)*/) {
            todoList.set(index, todo);
        }
    }

    public List<ToDo> select(Condition... conditions) {
        List<ToDo> result = new ArrayList<ToDo>();
        for (ToDo todo : todoList) {
            boolean selected = true; // /*(d)*/;
            for (Condition condition : conditions) {
                selected = condition.test(todo); // selected /*(e)*/ condition.test(todo);
                if (!selected) break;
            }
            if (selected) {
                result.add(new ToDo(todo));
            }
        }
        return result;
    }
}


// [プログラム 3]
/*public*/ interface Condition {
    boolean test(ToDo todo);
}

// [プログラム 4]
/*public*/ class ToDoListTester {
    public static void main(String[] args) {
        ToDoList list = new ToDoList();
        list.add(new ToDo("メール送信", "201604181500", ToDo.Priority.HIGH));
        list.add(new ToDo("ホテル予約", "20160420", ToDo.Priority.LOW));
        list.add(new ToDo("チケット購入", "20160430", ToDo.Priority.MIDDLE));
        list.add(new ToDo("報告書作成", "20160428", ToDo.Priority.HIGH));
        list.add(new ToDo("会議室予約", "20160530", ToDo.Priority.HIGH));
        list.update(new ToDo("PC購入", "20160531", ToDo.Priority.HIGH));
        for (ToDo todo : list.select()) {
            todo.setState(ToDo.State.STARTED);
            list.update(todo);
        }
        Condition condition1 = new Condition() {
            public boolean test(ToDo todo) {
                return todo.getDeadline().compareTo("20160501") < 0;
            }
        };
        Condition condition2 = new Condition() {
            public boolean test(ToDo todo) {
                return todo.getPriority().equals(ToDo.Priority.HIGH);
            }
        };
        for (ToDo todo : list.select(condition1, condition2)) {
            System.out.println(todo);
        }
    }
}
// 実行結果
// 主題: メール送信, 期限: 201604181500, 優先度: HIGH, 状態: STARTED
// 主題: 報告書作成, 期限: 20160428, 優先度: HIGH, 状態: STARTED
