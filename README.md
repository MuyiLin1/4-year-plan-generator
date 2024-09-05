# Academic Schedule Planner

## Description
This project implements an Academic Schedule Planner designed to help students organize their course load across multiple semesters. It takes into account course prerequisites, credit limits, and workload balancing to create an optimal academic schedule.

## Features
- Course (Job) creation with credits and weekly hour requirements
- Prerequisite management for courses
- Automatic schedule balancing across semesters
- Customizable target credits and maximum workload per semester
- Supports both general education and major-specific courses

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 18 or later

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/yourusername/academic-schedule-planner.git
   ```
2. Navigate to the project directory:
   ```
   cd academic-schedule-planner
   ```
3. Compile the Java files:
   ```
   javac com/github/yourusername/scheduleplanner/*.java
   ```

### Usage
Run the main class to see a sample schedule:
```
java com.github.yourusername.scheduleplanner.Schedule
```

To use the scheduler in your own project, you can create a new `Schedule` object and add jobs:

```java
Schedule schedule = new Schedule();
Job course1 = schedule.insertJob("Course 1", 3, 9);
Job course2 = schedule.insertJob("Course 2", 4, 12);
course2.addPrerequisite(course1);

schedule.printBalancedSchedule(15, 18, 40);
```

## Customization
You can customize the schedule by modifying the following parameters in the `printBalancedSchedule` method call:
- Target credits per semester
- Maximum credits per semester
- Maximum hours per semester

## Contributing
Contributions to improve the Academic Schedule Planner are welcome. Please feel free to submit a Pull Request.

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments
- Inspired by the challenges of academic planning in computer science programs.
- Thanks to all contributors and testers who have provided valuable feedback.
