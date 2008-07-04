
public abstract class Task {
    private int ticks; // The number of ticks until this task will be fired
    private int timer; // This timer variable will count down on every tick
    private int counter; // This counter will increment on every repeat of the task
    private int repeats; // The number of times this task will be repeated
    private Object result; // This holds any information that the task will need to hold
                           // onto and/or pass on between repeats.
    private boolean on; //This determines whether the task will execute (or count down to execution)
                        // in the tick() method
    
    /**
     * This is the more complicated constructor which lets you set all of the options.
     * @param ticks - the number of time ticks between repeats of execution
     * @param repeats - the number of times the task will be repeated
     * @param first_result - an object containing any state that you want the task to have
     * @param on - the initial on/off state for the task
     */
    public Task(int ticks, int repeats, Object first_result, boolean on) {
        this.ticks = ticks;
        this.timer = ticks;
        this.counter = 0;
        this.repeats = repeats;
        this.result = first_result;
        this.on = on;
    }

    /**
     * This simpler constructor is for tasks that need to be executed at every tick with an infinite number
     * of repeats.
     * @param first_result - an object containing any state you want the task to have
     * @param on - the initial on/off state for the task
     */
    public Task(Object first_result, boolean on) {
        this.ticks = 1;
        this.timer = ticks;
        this.counter = 0;
        this.repeats = -1; //Setting repeats to -1 makes sure the task never gets deleted from the timer
        this.result = first_result;
        this.on = on;
    }
    
    /**
     * This is a simple constructor for tasks that only need to execute one time after a certain number of ticks.
     * @param ticks - the number of time ticks before execution
     * @param first_result - an object containing any state you want the task to have
     */
    public Task(int ticks, Object first_result) {
        this.ticks = ticks;
        this.timer = ticks;
        this.counter = 0;
        this.repeats = 1;
        this.result = first_result;
        this.on = true;
    }
    
    public boolean on() {
        return on;
    }
    
    public boolean off() {
        return !on;
    }
    
    public void turnOn() {
        on = true;
    }
    
    public void turnOff() {
        on = false;
    }
    
    /**
     * Counts one tick of time. If the time for this task has run out, then the run() method will be fired.
     * @return false if this task has no more repeats left, true otherwise
     */
    public boolean tick() {
        if (!on) return true;
        if (--timer <= 0) {
            result = run(counter++, result);
            repeats--;
            timer = ticks; //reset timer
        }
        
        return (repeats != 0);
    }
    
    /**
     * This is the main method for the task. Anything that the task needs to do 
     * will be implemented in this method.
     * @param counter an incremented number representing the number of times this task has been 
     * fired previously (starting with 0)
     * @param result holds any state or information that the task may need
     * @return an Object containing any information that the next repeat of the task may need
     */
    public abstract Object run(int counter, Object result);
}
