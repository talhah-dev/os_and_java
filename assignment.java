/**
 * Date: 07-Oct-2025
 *
 * Class: IU_Mark_Sheet
 * Purpose:
 * - Store a student's info (name, registration number)
 * - Store subject names
 * - Store maximum marks per subject
 * - Store scored marks per subject
 * - Calculate GPA (4.0 scale) based on percentage per subject
 */
public class IU_Mark_Sheet {

    // =========================
    // 1) DATA / FIELDS (STATE)
    // =========================

    // Student basic info
    private String studentName;
    private String registrationNumber;

    // Arrays for subjects and marks
    private String[] subjects;        // Example: {"OOP", "DSA", "DB"}
    private double[] maxMarks;        // Example: {100, 100, 50}
    private double[] scoredMarks;     // Example: {85, 90, 40}

    // =========================
    // 2) CONSTRUCTOR
    // =========================

    /**
     * Constructor:
     * This runs when you create object like:
     * IU_Mark_Sheet ms = new IU_Mark_Sheet("Talha", "REG-123");
     */
    public IU_Mark_Sheet(String Student, String RegistrationNumber) {
        // Store the values into object fields
        this.studentName = Student;
        this.registrationNumber = RegistrationNumber;
    }

    // =========================
    // 3) METHODS YOU REQUESTED
    // =========================

    /**
     * Store subject names into the object.
     * Example input:
     * Subject_Name(new String[]{"OOP", "DSA", "DB"});
     */
    public void Subject_Name(String Subject[]) {
        // Save reference (array) into class field
        this.subjects = Subject;
    }

    /**
     * Store subject max marks into the object.
     * Example input:
     * Subject_Max_Mark(new double[]{100, 100, 50});
     */
    public void Subject_Max_Mark(double MaxMark[]) {
        this.maxMarks = MaxMark;
    }

    /**
     * Store subject scored marks into the object.
     * Example input:
     * Subject_Scored_Mark(new double[]{85, 90, 40});
     */
    public void Subject_Scored_Mark(double ScoredMark[]) {
        this.scoredMarks = ScoredMark;
    }

    // =========================
    // 4) GPA CALCULATION METHOD
    // =========================

    /**
     * Calculate overall GPA (4.0 scale).
     *
     * Logic used (common mapping):
     * - Convert each subject score into percentage:
     *      percent = (scored / max) * 100
     * - Convert percent into grade points (0.0 to 4.0)
     * - Average grade points of all subjects
     *
     * NOTE:
     * - If arrays are missing or lengths mismatch, we handle safely.
     */
    public Double StudentGPA() {

        // -------------------------
        // A) VALIDATION CHECKS
        // -------------------------

        // If any array is not provided yet, GPA can't be calculated
        if (subjects == null || maxMarks == null || scoredMarks == null) {
            // Return 0.0 instead of crashing
            return 0.0;
        }

        // We need all arrays to represent the same number of subjects.
        // If they don't match, we will only compute up to the smallest length
        int n = Math.min(subjects.length, Math.min(maxMarks.length, scoredMarks.length));

        // If n is 0, no subjects exist
        if (n == 0) {
            return 0.0;
        }

        // -------------------------
        // B) CALCULATE GPA
        // -------------------------

        double totalGradePoints = 0.0;

        // Loop over each subject
        for (int i = 0; i < n; i++) {

            double max = maxMarks[i];
            double scored = scoredMarks[i];

            // If max is 0, percent calculation would divide by 0 -> error
            // So we skip or treat it as 0 grade points
            if (max <= 0) {
                continue;
            }

            // If scored is greater than max, we can clamp it to max (optional safety)
            if (scored > max) {
                scored = max;
            }

            // If scored is negative, clamp to 0
            if (scored < 0) {
                scored = 0;
            }

            // Convert to percentage
            double percent = (scored / max) * 100.0;

            // Convert percent to grade points (0.0 to 4.0)
            double gp = percentageToGPA(percent);

            // Add this subject's grade points to total
            totalGradePoints += gp;
        }

        // Average GPA across subjects
        double gpa = totalGradePoints / n;

        // Return GPA
        return gpa;
    }

    // =========================
    // 5) HELPER METHOD (PRIVATE)
    // =========================

    /**
     * Convert percentage to GPA (4.0 scale).
     * You can change this mapping according to your university policy.
     */
    private double percentageToGPA(double percent) {

        // A common GPA mapping (example):
        // 90+  -> 4.0
        // 85-89 -> 3.7
        // 80-84 -> 3.3
        // 75-79 -> 3.0
        // 70-74 -> 2.7
        // 65-69 -> 2.3
        // 60-64 -> 2.0
        // 55-59 -> 1.7
        // 50-54 -> 1.0
        // <50 -> 0.0

        if (percent >= 90) return 4.0;
        if (percent >= 85) return 3.7;
        if (percent >= 80) return 3.3;
        if (percent >= 75) return 3.0;
        if (percent >= 70) return 2.7;
        if (percent >= 65) return 2.3;
        if (percent >= 60) return 2.0;
        if (percent >= 55) return 1.7;
        if (percent >= 50) return 1.0;
        return 0.0;
    }

    // =========================
    // 6) OPTIONAL: DISPLAY METHOD
    // (Not asked, but helpful for testing)
    // =========================
    public void printMarkSheet() {
        System.out.println("Student: " + studentName);
        System.out.println("Reg No : " + registrationNumber);

        if (subjects == null || maxMarks == null || scoredMarks == null) {
            System.out.println("Marks data not complete yet.");
            return;
        }

        int n = Math.min(subjects.length, Math.min(maxMarks.length, scoredMarks.length));

        System.out.println("\nSubjects:");
        for (int i = 0; i < n; i++) {
            System.out.println("- " + subjects[i] + " | Max: " + maxMarks[i] + " | Scored: " + scoredMarks[i]);
        }

        System.out.println("\nGPA: " + StudentGPA());
    }
}
