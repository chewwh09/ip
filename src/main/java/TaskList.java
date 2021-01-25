import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void list() {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    public Task addTask(String type, String taskDescription)
            throws InvalidDescriptionException, DateTimeParseException {
        Task task;
        if (taskDescription.length() == 0) {
            throw new InvalidDescriptionException("☹ OOPS!!! The description of " + type + " cannot be empty.");
        } else if (type.equals("todo")) {
            task = new ToDo(false, taskDescription);
        } else if ((type.equals("deadline") && !taskDescription.contains("/by"))
                || (type.equals("event")) && !taskDescription.contains("/at")) {
            throw new InvalidDescriptionException("☹ OOPS!!! The description format of " + type + " is wrong.");
        } else {
            int index = type.equals("deadline") ? taskDescription.indexOf("/by") : taskDescription.indexOf("/at");
            String taskName = taskDescription.substring(0, index);
            String dateTimeString = taskDescription.substring(index + 4).strip().replace("/", "-");
            LocalDate dateTime = LocalDate.parse(dateTimeString);

            if (type.equals("deadline")) {
                task = new Deadline(false, taskName, dateTime);
            } else {
                task = new Event(false, taskName, dateTime);
            }
        }
        tasks.add(task);
        System.out.println("     Got it. I've added this task:");
        System.out.println("     " + task);
        System.out.println("     Now you have " + tasks.size() + " task(s) in the list");
        return task;
    }

    public ArrayList<Task> delete(String taskDescription) throws InvalidDescriptionException{
        try {
            int index = Integer.parseInt(taskDescription.substring(0, 1)) - 1;
            Task task = tasks.get(index);
            tasks.remove(index);
            System.out.println("     Noted. I've removed this task: ");
            System.out.println("     " + task);
            System.out.println("     Now you have " + tasks.size() + " task(s) in the list");
            return tasks;
        } catch (NumberFormatException ex) {
            throw new InvalidDescriptionException("☹ OOPS!!! The task description is wrong");
        } catch (IndexOutOfBoundsException ex) {
            throw new InvalidDescriptionException("☹ OOPS!!! The number you entered is either too big " +
                    "or smaller than 0. There are currently " + tasks.size() + " tasks");
        }
    }

    public ArrayList<Task> done(String taskDescription) throws InvalidDescriptionException {
        try {
            int index = Integer.parseInt(taskDescription.substring(0, 1)) - 1;
            Task task = tasks.get(index);
            task.completeTask();
            System.out.println("     Nice! I've marked this task as done:\n     " + task);
            return tasks;
        } catch (NumberFormatException ex) {
            throw new InvalidDescriptionException("☹ OOPS!!! The task description is wrong");
        } catch (IndexOutOfBoundsException ex) {
            throw new InvalidDescriptionException("☹ OOPS!!! The number you entered is either too big " +
                    "or smaller than 0. There are currently " + tasks.size() + " tasks");
        }
    }
}
