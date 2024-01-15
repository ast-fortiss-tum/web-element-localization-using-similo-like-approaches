import matplotlib.pyplot as plt
import numpy as np
import csv
from collections import defaultdict

# Constants for easy modification and better code readability
ALGORITHM_REPLACEMENTS = {
    "equalignorecase": "equals ignore case",
    "distancesmalldecay": "distance small decay",
    "distancelargedecay": "distance large decay",
    "distancemediumdecay": "distance medium decay",
    "aspectratio": "aspect ratio",
    "jarowinkler": "jaro winkler"
}

PROPERTY_REPLACEMENTS = {
    "className": "class",
    "xpath.chrome": "xpath",
    "xpath.idxpath": "id xpath",
    "css_selector": "css selector",
    "neighbour_text": "neighbour text",
    "visible_text": "text"
}

ORDER_PROPERTIES = [
    "tag", "class", "name", "id", "href", "alt", "type", "aria-label", "xpath",
    "id xpath", "location", "dimension", "text", "neighbour text"
]

ORDER_COMPARISONS = [
    "equal", "equals ignore case", "levenshtein", "jaro winkler", "jaccard",
    "area", "perimeter", "aspect ratio", "distancelinear", "distance small decay",
    "distance medium decay", "distance large decay"
]

# Further increase the text size for publication quality figures
fontsize = 22
plt.rcParams.update({'font.size': fontsize, 'axes.labelsize': fontsize,
                     'axes.titlesize': fontsize, 'legend.title_fontsize': fontsize})


def clean_and_extract(row):
    property_, algorithm, score, *_ = row
    algorithm = algorithm.lower().replace(" (option)", "")
    algorithm = ALGORITHM_REPLACEMENTS.get(algorithm, algorithm)
    property_ = PROPERTY_REPLACEMENTS.get(property_, property_)
    return property_, algorithm, float(score)

def read_data(file_path):
    counts = defaultdict(lambda: defaultdict(int))
    with open(file_path, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        next(reader, None)  # Skip the header row
        for row in reader:
            prop, alg, _ = clean_and_extract(row)
            counts[prop][alg] += 1
    return counts


def sort_algorithms(property_algorithm_counts):
    """Sorts algorithms by the total number of uses."""
    total_counts = defaultdict(int)
    for prop, alg_counts in property_algorithm_counts.items():
        for alg, count in alg_counts.items():
            total_counts[alg] += count
    # Return sorted algorithms by total count in descending order
    return sorted(total_counts, key=total_counts.get, reverse=True)


def plot_stacked_bars(property_algorithm_counts, order_comparisons):
    # Initialize a figure
    plt.figure(figsize=(20, 12))  # Increased figure size

    # Set bottom for the stacking
    bottoms = np.zeros(len(ORDER_PROPERTIES))

    # Define a set of unique colors
    colors = plt.cm.get_cmap('tab20c', len(order_comparisons)).colors

    # Find the highest value for each property
    max_values = {prop: max(alg_counts.values()) for prop, alg_counts in property_algorithm_counts.items()}

    # Store information to replot bars with black borders
    replot_info = []

    # Loop over each algorithm
    for alg_index, alg in enumerate(order_comparisons):
        counts = [property_algorithm_counts[prop][alg] for prop in ORDER_PROPERTIES]
        bars = plt.bar(ORDER_PROPERTIES, counts, bottom=bottoms, label=alg, color=colors[alg_index], edgecolor='gray')
        bottoms += np.array(counts)

        # Check and store bar properties if this bar has the highest value for the current property
        for bar, count, prop in zip(bars, counts, ORDER_PROPERTIES):
            if count > 0 and count == max_values[prop]:
                # Store the bar properties
                replot_info.append((ORDER_PROPERTIES.index(prop), count, bottoms[ORDER_PROPERTIES.index(prop)] - count, alg_index))

            # Add count labels
            height = bar.get_height()
            if height > 5:
                plt.text(bar.get_x() + bar.get_width() / 2, bar.get_y() + height / 2,
                         str(int(count)), ha='center', va='center', color='black', fontsize=fontsize)

    # Replot bars with the black borders
    for prop_index, count, bottom, alg_index in replot_info:
        plt.bar(ORDER_PROPERTIES[prop_index], count, bottom=bottom,
                edgecolor='black', linewidth=2, color=colors[alg_index])

    # Add labels and adjust axes
    plt.xticks(rotation=45)
    plt.ylabel('Times algorithm was optimal for property')
    plt.ylim(0, max(bottoms) * 1.05)

    # Add a three-row legend above the figure with increased distance
    ncol_legend = int(len(order_comparisons) / 3) + (len(order_comparisons) % 3 > 0)
    plt.legend(loc='upper center', bbox_to_anchor=(0.5, 1.3), ncol=ncol_legend)

    plt.tight_layout()

def plot_bar_graphs(file_path):
    property_algorithm_counts = read_data(file_path)
    order_comparisons = sort_algorithms(property_algorithm_counts)
    plot_stacked_bars(property_algorithm_counts, order_comparisons)
    plt.show()


# Example usage
file_path = 'similo_oat_four_month_fitness.csv'
plot_bar_graphs(file_path)
