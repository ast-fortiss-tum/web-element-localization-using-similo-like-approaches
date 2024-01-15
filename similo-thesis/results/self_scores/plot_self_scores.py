import matplotlib.pyplot as plt
import pandas as pd
import numpy as np


# Adjust the default font sizes
fontsize_big = 24
fontsize = 20
plt.rcParams.update({'font.size': fontsize_big,
                     'axes.labelsize': fontsize_big,
                     'axes.titlesize': fontsize_big,
                     'xtick.labelsize': fontsize,
                     'ytick.labelsize': fontsize})

# Replace 'self_scores_next.csv' with your actual file path
df = pd.read_csv('self_scores_next.csv')

# Exclude specific tags
excluded_tags = ['I', 'TEXTAREA', 'IMG']
df_filtered = df[~df['tag'].isin(excluded_tags)]

# Group by 'tag' and prepare the data for plotting
grouped_data = df_filtered.groupby('tag')['score'].apply(list).reset_index(name='scores')

# Calculate the third quartile (75th percentile) for each group to sort by
grouped_data['third_quartile'] = grouped_data['scores'].apply(lambda x: np.percentile(x, 50))

# Sort the data and labels by the third quartile
sorted_data = grouped_data.sort_values(by='third_quartile', ascending=True)['scores'].tolist()
labels = grouped_data.sort_values(by='third_quartile', ascending=True)['tag'].tolist()

# Set up the matplotlib figure and axis
fig, ax = plt.subplots(figsize=(12, 6))

# Create a boxplot without outliers and whiskers extending to the 5th and 95th percentiles
bplot = ax.boxplot(sorted_data,
                   vert=True,
                   patch_artist=True,
                   labels=labels,
                   showfliers=False,  # Do not show outliers
                   whis=[5, 95])  # Set whiskers to the 5th and 95th percentiles

# Fill with colors
colors = ['pink', 'lightblue', 'lightgreen', 'lavender', 'beige', 'cyan', 'peachpuff', 'palegoldenrod']
for patch, color in zip(bplot['boxes'], colors * (len(bplot['boxes']) // len(colors) + 1)):
    patch.set_facecolor(color)

# Change the color of the median line to black
for median in bplot['medians']:
    median.set_color('black')

# Adding horizontal grid lines
ax.yaxis.grid(True)
ax.set_ylabel('Score')

# Show the plot
plt.show()
