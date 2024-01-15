import pandas as pd

# Step 1: Read the CSV file without headers
file_path = 'eliminating_brute_force_four_month.csv'  # Replace with your CSV file path
df = pd.read_csv(file_path, header=None)

# Step 2: Add a new column with the count of 0.0 values per row
# Assuming that all columns should be checked for the value 0.0
df['Zero_Count'] = (df == 0.0).sum(axis=1)

# Step 3: Write the DataFrame back to the same CSV file without headers
df.to_csv(file_path, index=False, header=False)
