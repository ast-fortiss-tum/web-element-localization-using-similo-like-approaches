import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

# Load data
data = pd.read_csv('3d_heat_map.csv')

# Extracting data columns
input1 = data['name']
input2 = data['id']
input3 = data['href']
output = data['score']

# Normalizing the output for color mapping
# Adjust the range for normalization to enhance color variation
min_val, max_val = np.percentile(output, [5, 95])  # Using percentile to avoid outliers
norm = plt.Normalize(min_val, max_val)

# Creating a 3D plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# 3D scatter plot with enhanced color variation
# Choose a colormap with distinct colors, like 'plasma' or 'rainbow'
sc = ax.scatter(input1, input2, input3, c=output, cmap='viridis', norm=norm, alpha=0.4)
# Highlighting dots with the highest scores
# Define a threshold, e.g., top 5% scores
threshold = np.percentile(output, 99.9)
high_score_mask = output >= threshold

# Overlay these high-score dots in a different color and/or size
ax.scatter(input1[high_score_mask], input2[high_score_mask], input3[high_score_mask],
           color='red', s=100, edgecolor='red', alpha=0.7)  # Adjust size (s) and other parameters as needed

# Adding labels with increased font size
label_font_size = 16  # You can adjust this value as needed
ax.set_xlabel('Name weight', fontsize=label_font_size)
ax.set_ylabel('Id weight', fontsize=label_font_size)
ax.set_zlabel('Href weight', fontsize=label_font_size)

# Adding a color bar below the plot
cbar = plt.colorbar(sc, ax=ax, orientation='horizontal', shrink=0.5, aspect=20, pad=0.1)
cbar.ax.tick_params(labelsize=label_font_size)

# Show plot
plt.show()
