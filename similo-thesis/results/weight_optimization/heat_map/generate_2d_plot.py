import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.interpolate import griddata
import matplotlib.colors as mcolors

# Load data
data = pd.read_csv('2d_heat_map.csv')

# Extracting data columns
x = data['tag']
y = data['class']
z = data['score']

# Creating grid to interpolate
xi = np.linspace(x.min(), x.max(), 100)
yi = np.linspace(y.min(), y.max(), 100)
xi, yi = np.meshgrid(xi, yi)
zi = griddata((x, y), z, (xi, yi), method='cubic')

# Creating a 3D plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Surface plot with custom colors
surf = ax.plot_surface(xi, yi, zi, cmap='viridis')

# Adding labels with increased font size
label_font_size = 16
ax.set_xlabel('Tag weight', fontsize=label_font_size)
ax.set_ylabel('Class weight', fontsize=label_font_size)
ax.set_zlabel('Fitness score', fontsize=label_font_size)

# Remove the tick labels for the Output axis
ax.set_zticklabels([])

# Adding a color bar below the plot
cbar = plt.colorbar(surf, ax=ax, orientation='horizontal', shrink=0.5, aspect=20, pad=0.1)
cbar.ax.tick_params(labelsize=label_font_size)

# Show plot
plt.show()
