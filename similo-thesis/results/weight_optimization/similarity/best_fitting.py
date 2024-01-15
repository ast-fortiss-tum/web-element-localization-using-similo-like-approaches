import csv
from itertools import product


def read_csv(file_path):
    """
    Read a CSV file and return a list of lists, where each inner list represents a row of numerical values.
    """
    with open(file_path, newline='') as csvfile:
        data_reader = csv.reader(csvfile)
        return [list(map(float, row)) for row in data_reader]


def calculate_difference(row1, row2):
    """
    Calculate the sum of absolute differences between two rows.
    """
    return sum(abs(a - b) for a, b in zip(row1, row2))


def find_closest_rows(file1, file2):
    """
    Find the two closest rows, one from each file.
    """
    data1 = read_csv(file1)
    data2 = read_csv(file2)

    # Generate all possible pairs of rows (one from each file)
    all_pairs = product(data1, data2)

    # Find the pair with the smallest sum of differences
    closest_pair = min(all_pairs, key=lambda pair: calculate_difference(*pair))

    return closest_pair


closest_rows = find_closest_rows('4_months_best.csv', '5_years_best.csv')
print("Closest rows are:", closest_rows)
