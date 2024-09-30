import java.util.*;

public class ShamirSecretSharing {
    // Function to decode y-values based on their base
    public static int decodeYValue(int base, String value) {
        return Integer.parseInt(value, base);
    }

    // Function to perform Lagrange interpolation and find the constant term 'c'
    public static int lagrangeInterpolation(List<int[]> points) {
        double total = 0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            int xi = points.get(i)[0];
            int yi = points.get(i)[1];
            double li = 1;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    li *= (0.0 - xj) / (xi - xj);
                }
            }
            total += yi * li;
        }

        return (int) Math.round(total);
    }

    // Function to parse the input data and extract the (x, y) points
    public static Map<String, Object> parseInputData(Map<String, Object> inputData) {
        @SuppressWarnings("unchecked")
        Map<String, Object> keys = (Map<String, Object>) inputData.get("keys");
        int n = (int) keys.get("n");
        int k = (int) keys.get("k");

        List<int[]> points = new ArrayList<>();
        for (String key : inputData.keySet()) {
            if (!key.equals("keys")) {
                @SuppressWarnings("unchecked")
                Map<String, String> valueMap = (Map<String, String>) inputData.get(key);
                int x = Integer.parseInt(key); // The key itself is the x-coordinate
                int base = Integer.parseInt(valueMap.get("base"));
                int y = decodeYValue(base, valueMap.get("value"));
                points.add(new int[] { x, y });
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("points", points);
        result.put("n", n);
        result.put("k", k);

        return result;
    }

    // Function to find invalid points (wrong points) that don't lie on the
    // polynomial curve
    public static List<int[]> findWrongPoints(List<int[]> points, int k) {
        List<int[]> wrongPoints = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            List<int[]> subset = new ArrayList<>(points);
            subset.remove(i);
            if (subset.size() >= k) {
                try {
                    lagrangeInterpolation(subset.subList(0, k)); // Ensure subset has k points for interpolation
                } catch (Exception e) {
                    wrongPoints.add(points.get(i));
                }
            }
        }

        return wrongPoints;
    }

    // Main function to process the input test cases
    public static void processTestCases(List<Map<String, Object>> testCases) {
        List<String> results = new ArrayList<>();

        // Iterate over both test cases
        for (int testCaseIndex = 0; testCaseIndex < testCases.size(); testCaseIndex++) {
            Map<String, Object> inputData = testCases.get(testCaseIndex);

      
            Map<String, Object> parsedData = parseInputData(inputData);
            @SuppressWarnings("unchecked")
            List<int[]> points = (List<int[]>) parsedData.get("points");
            int k = (int) parsedData.get("k");

            int secret = lagrangeInterpolation(points.subList(0, k)); // Use first k points to find secret
            results.add("Test Case " + (testCaseIndex + 1) + " Secret: " + secret);

            if (testCaseIndex == 1) {
                List<int[]> wrongPoints = findWrongPoints(points, k);
                if (!wrongPoints.isEmpty()) {
                    results.add("Test Case 2 Wrong Points: " + Arrays.deepToString(wrongPoints.toArray()));
                } else {
                    results.add("Test Case 2 Wrong Points: No wrong points found.");
                }
            }
        }

        for (String result : results) {
            System.out.println(result);
        }
    }

    // Sample JSON input for two test cases
    public static void main(String[] args) {
        // Test cases in a similar format as JSON
        List<Map<String, Object>> testCases = new ArrayList<>();

        Map<String, Object> testCase1 = new HashMap<>();

        Map<String, Object> testCase2 = new HashMap<>();

        // First test case
        Map<String, Object> keys1 = new HashMap<>();
        keys1.put("n", 4);
        keys1.put("k", 3);
        testCase1.put("keys", keys1);

        testCase1.put("1", Map.of("base", "10", "value", "4"));
        testCase1.put("2", Map.of("base", "2", "value", "111"));
        testCase1.put("3", Map.of("base", "10", "value", "12"));
        testCase1.put("6", Map.of("base", "4", "value", "213"));

        // Second test case
        Map<String, Object> keys2 = new HashMap<>();
        keys2.put("n", 4);
        keys2.put("k", 3);
        testCase2.put("keys", keys2);

        testCase2.put("1", Map.of("base", "10", "value", "5"));
        testCase2.put("2", Map.of("base", "2", "value", "101"));
        testCase2.put("3", Map.of("base", "10", "value", "15"));
        testCase2.put("6", Map.of("base", "4", "value", "201"));

        testCases.add(testCase1);

        testCases.add(testCase2);

        

        // the main function to process test cases
        processTestCases(testCases);
    }
}
