import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Timer {
    private LinkedHashMap<String, Task> taskmap;
    
    public Timer() {
        taskmap = new LinkedHashMap<String, Task>();
    }
    
    /**
     * Add a task to the task list.
     * @param task the task to be added to the task list.
     */
    public void schedule(String name, Task task) {
        taskmap.put(name, task);
    }
    
    /**
     * This method is used to access tasks by their name.
     * @param name - the name of the task to get
     * @return the task associated with the given name
     */
    public Task get(String name) {
        return taskmap.get(name);
    }
    
    /**
     * This method tells the timer to turn off all of the tasks that aren't in the specified list.
     * @param names - the list of tasks that will be on (with all of the other tasks being turned off)
     * @return the list of names of tasks that were "on" before this method was called
     */
    public ArrayList<String> keepOnly(ArrayList<String> names) {
        ArrayList<String> previous_state = new ArrayList<String>();
        for (String name : taskmap.keySet()) {
            if (taskmap.get(name).on()) {
                previous_state.add(name);
            }
            if (names.contains(name)) {
                taskmap.get(name).turnOn();
            }
            else {
                taskmap.get(name).turnOff();
            }
        }
        return previous_state;
    }
    
    /**
     * Counts one tick of time by calling the tick() method on every task in the task list.
     * If a particular task's scheduled time has come, then it will run itself during it's tick() method.
     */
    public void tick() {
        LinkedHashMap<String, Task> new_map = new LinkedHashMap<String, Task>(taskmap);
        
        for (String name : taskmap.keySet()) {
            if (!taskmap.get(name).tick()) {
                new_map.remove(name);
            }
        }

        taskmap = new_map;
    }
}
