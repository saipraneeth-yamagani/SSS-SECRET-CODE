import json

# Function to decode y-values based on their base
def decode_y_value(base, value):
    return int(value, base)

# Function to perform Lagrange interpolation and find the constant term 'c'
def lagrange_interpolation(points, prime=None):
    
    total = 0
    n = len(points)
    
    for i in range(n):
        xi, yi = points[i]
        li = 1
        for j in range(n):
            if i != j:
                xj = points[j][0]
                li *= (0 - xj) / (xi - xj)
        
        total += yi * li
    return round(total)

# Function to parse the JSON input and extract the (x, y) points
def parse_json(input_data):
    keys = input_data["keys"]
    n = keys["n"]
    k = keys["k"]
    
    points = []
    for key, value in input_data.items():
        if key != "keys":
            x = int(key)  # The key itself is the x-coordinate
            base = int(value["base"])
            y = decode_y_value(base, value["value"])
            points.append((x, y))
    
    return points, n, k

# Function to find invalid points
def find_wrong_points(points):
    valid_points = []
    wrong_points = []
    
    # Perform Lagrange interpolation on subsets of points to check validity
    for i in range(len(points)):
        subset = points[:i] + points[i+1:]  # Remove one point at a time
        try:
            secret = lagrange_interpolation(subset)
            valid_points.append(points[i])
        except Exception as e:
            wrong_points.append(points[i])
    
    return wrong_points

if __name__ == "__main__":
    # Load the input JSON data (assuming input is in 'input.json')
    with open("input2.json") as f:
        input_data = json.load(f)
        # with open("input2.json") as f:
        #  input_data = json.load(f)


    # Parse the points from the input data
    points, n, k = parse_json(input_data)
    
    # Step 3: Use Lagrange interpolation to find the secret (constant term 'c')
    secret = lagrange_interpolation(points[:k])  # Use first k points to find secret
    
    print(f"Secret: {secret}")
    
    # Step 4: For second test case, find wrong points (if applicable)
    if len(points) > k:
        wrong_points = find_wrong_points(points)
        if wrong_points:
            print(f"Wrong Points: {wrong_points}")
        else:
            print("No wrong points found.")
