import java.util.*;
import java.util.stream.Collectors;

/**
 * A class representing a schedule of academic jobs (courses).
 * This class allows for the creation, management, and balancing of academic workloads.
 */
public class Schedule {

    /**
     * Represents an academic job (course) within the schedule.
     */
    public static class Job {
        private final String name;
        private final int credits;
        private final int hoursPerWeek;
        private final List<Job> incoming;
        private final List<Job> outgoing;
        private boolean scheduled;

        /**
         * Constructs a new Job with the given parameters.
         *
         * @param name         The name of the job.
         * @param credits      The number of credits for the job.
         * @param hoursPerWeek The number of hours per week required for the job.
         */
        public Job(String name, int credits, int hoursPerWeek) {
            this.name = name;
            this.credits = credits;
            this.hoursPerWeek = hoursPerWeek;
            this.incoming = new ArrayList<>();
            this.outgoing = new ArrayList<>();
            this.scheduled = false;
        }

        /**
         * Adds a prerequisite job to this job.
         *
         * @param prerequisite The job that is a prerequisite for this job.
         */
        public void addPrerequisite(Job prerequisite) {
            incoming.add(prerequisite);
            prerequisite.outgoing.add(this);
        }

        // Getters
        public String getName() { return name; }
        public int getCredits() { return credits; }
        public int getHoursPerWeek() { return hoursPerWeek; }
        public List<Job> getIncoming() { return incoming; }
        public boolean isScheduled() { return scheduled; }

        // Setter
        public void setScheduled(boolean scheduled) { this.scheduled = scheduled; }
    }

    private final List<Job> jobs;

    /**
     * Constructs a new empty Schedule.
     */
    public Schedule() {
        jobs = new ArrayList<>();
    }

    /**
     * Inserts a new job into the schedule.
     *
     * @param name         The name of the job.
     * @param credits      The number of credits for the job.
     * @param hoursPerWeek The number of hours per week required for the job.
     * @return The newly created Job object.
     */
    public Job insertJob(String name, int credits, int hoursPerWeek) {
        Job newJob = new Job(name, credits, hoursPerWeek);
        jobs.add(newJob);
        return newJob;
    }

    /**
     * Retrieves a job at the specified index.
     *
     * @param idx The index of the job to retrieve.
     * @return The Job at the specified index.
     */
    public Job getJob(int idx) {
        return jobs.get(idx);
    }

    /**
     * Balances the workload across semesters based on the given constraints.
     *
     * @param targetCredits        The target number of credits per semester.
     * @param maxCredits           The maximum number of credits allowed per semester.
     * @param maxHoursPerSemester  The maximum number of hours allowed per semester.
     * @return A list of semesters, where each semester is a list of jobs.
     */
    public List<List<Job>> balanceWorkload(int targetCredits, int maxCredits, int maxHoursPerSemester) {
        List<List<Job>> semesters = new ArrayList<>();
        Set<Job> scheduledJobs = new HashSet<>();

        while (scheduledJobs.size() < jobs.size()) {
            List<Job> currentSemester = new ArrayList<>();
            int currentCredits = 0;
            int currentHours = 0;

            List<Job> availableJobs = getAvailableJobs(scheduledJobs);
            availableJobs.sort(Comparator.comparingInt(Job::getCredits).reversed());

            for (Job job : availableJobs) {
                if (currentCredits + job.getCredits() <= maxCredits && 
                    currentHours + job.getHoursPerWeek() <= maxHoursPerSemester) {
                    currentSemester.add(job);
                    currentCredits += job.getCredits();
                    currentHours += job.getHoursPerWeek();
                    job.setScheduled(true);
                    scheduledJobs.add(job);

                    if (currentCredits >= targetCredits) {
                        break;
                    }
                }
            }

            if (!currentSemester.isEmpty()) {
                semesters.add(currentSemester);
            } else {
                // If we can't schedule any more jobs, break to avoid infinite loop
                break;
            }
        }

        return semesters;
    }

    /**
     * Prints a balanced schedule based on the given constraints.
     *
     * @param targetCredits        The target number of credits per semester.
     * @param maxCredits           The maximum number of credits allowed per semester.
     * @param maxHoursPerSemester  The maximum number of hours allowed per semester.
     */
    public void printBalancedSchedule(int targetCredits, int maxCredits, int maxHoursPerSemester) {
        List<List<Job>> balancedSchedule = balanceWorkload(targetCredits, maxCredits, maxHoursPerSemester);
        for (int i = 0; i < balancedSchedule.size(); i++) {
            System.out.println("Semester " + (i + 1) + ":");
            int totalCredits = 0;
            int totalHours = 0;
            for (Job job : balancedSchedule.get(i)) {
                System.out.printf("  %s (%d credits, %d hours/week)%n", 
                                  job.getName(), job.getCredits(), job.getHoursPerWeek());
                totalCredits += job.getCredits();
                totalHours += job.getHoursPerWeek();
            }
            System.out.println("  Total credits: " + totalCredits);
            System.out.println("  Total hours: " + totalHours);
            System.out.println();
        }
    }

    private List<Job> getAvailableJobs(Set<Job> scheduledJobs) {
        return jobs.stream()
            .filter(job -> !job.isScheduled() && scheduledJobs.containsAll(job.getIncoming()))
            .collect(Collectors.toList()); // This returns a mutable ArrayList
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule();

        // General Education (GE) courses
        Job oralComm = schedule.insertJob("COMM 20 (Oral Communication)", 3, 3);
        Job englishComp = schedule.insertJob("ENGL 1A (Written Communication)", 3, 3);
        Job criticalThinking = schedule.insertJob("PHIL 57 (Logic and Critical Thinking)", 3, 3);
        Job calculus1 = schedule.insertJob("MATH 30 (Calculus I)", 4, 10);
        Job physics1 = schedule.insertJob("PHYS 50 (General Physics I)", 4, 10);
        Job bio = schedule.insertJob("BIOL 10 (The Living World)", 3, 3);
        Job artsGE = schedule.insertJob("ART 13 (Intro to Ceramics)", 3, 3);
        Job humanitiesGE = schedule.insertJob("HUM 1A (World Cultures & Civilizations)", 3, 3);
        Job socialSciences = schedule.insertJob("POLS 15 (US Government)", 3, 3);
        Job humanDevGE = schedule.insertJob("KIN 69 (Health & Wellness)", 3, 3);
        Job ethnicStudiesGE = schedule.insertJob("AFAM 10A (Intro to African American Studies)", 3, 3);
        Job pe1 = schedule.insertJob("PE 1A (Physical Activity I)", 1, 1);
        Job pe2 = schedule.insertJob("PE 2A (Physical Activity II)", 1, 1);

        // Major Prep and Core CS classes
        Job cs46A = schedule.insertJob("CS 46A (Intro to Programming)", 4, 7);
        Job cs46B = schedule.insertJob("CS 46B (Data Structures)", 4, 8);
        Job cs47 = schedule.insertJob("CS 47 (Discrete Structures)", 3, 5);
        Job cs100W = schedule.insertJob("CS 100W (Technical Writing)", 3, 6);
        Job cs146 = schedule.insertJob("CS 146 (Data Structures & Algorithms)", 3, 10);
        Job cs151 = schedule.insertJob("CS 151 (Object-Oriented Design)", 3, 8);
        Job cs152 = schedule.insertJob("CS 152 (Programming Languages Paradigms)", 3, 8);
        Job cs154 = schedule.insertJob("CS 154 (Theory of Computation)", 3, 10);
        Job cs160 = schedule.insertJob("CS 160 (Software Engineering)", 3, 8);
        Job cs149 = schedule.insertJob("CS 149 (Operating Systems)", 3, 10);
        Job cs157A = schedule.insertJob("CS 157A (Database Management Systems)", 3, 6);
        Job cs166 = schedule.insertJob("CS 166 (Information Security)", 3, 6);

        // Math and Science Requirements
        Job math31 = schedule.insertJob("MATH 31 (Calculus II)", 4, 8);
        Job math32 = schedule.insertJob("MATH 32 (Calculus III)", 3, 6);
        Job math42 = schedule.insertJob("MATH 42 (Discrete Math)", 3, 5);
        Job phys51 = schedule.insertJob("PHYS 51 (General Physics II)", 4, 10);

        // Upper-Division Electives
        Job cs157B = schedule.insertJob("CS 157B (Advanced Database Systems)", 3, 7);
        Job cs158A = schedule.insertJob("CS 158A (Computer Networks)", 3, 8);
        Job cs165 = schedule.insertJob("CS 165 (Artificial Intelligence)", 3, 8);
        Job cs156 = schedule.insertJob("CS 156 (Compiler Design)", 3, 9);

        // Set up prerequisites
        cs46B.addPrerequisite(cs46A);
        cs47.addPrerequisite(cs46A);
        cs146.addPrerequisite(cs46B);
        cs151.addPrerequisite(cs146);
        cs152.addPrerequisite(cs151);
        cs154.addPrerequisite(cs47);
        cs160.addPrerequisite(cs151);
        cs149.addPrerequisite(cs146);
        cs157A.addPrerequisite(cs46B);
        cs166.addPrerequisite(cs151);
        cs157B.addPrerequisite(cs157A);
        cs158A.addPrerequisite(cs149);
        cs165.addPrerequisite(cs146);
        cs156.addPrerequisite(cs152);
        math31.addPrerequisite(calculus1);
        math32.addPrerequisite(math31);
        phys51.addPrerequisite(physics1);

        // Generate and print the balanced schedule
        schedule.printBalancedSchedule(15, 18, 40);
    }
}