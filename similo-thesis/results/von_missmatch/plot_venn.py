import matplotlib.pyplot as plt
import pandas as pd
from matplotlib_venn import venn3
from BioVenn import *

file_path = 'matches.csv'
df = pd.read_csv(file_path)

found_by_similo = []
found_by_von_similo = []
overlap_found_by_von_similo = []

for row in df.itertuples():
    if row[2] == 1 and row[3] == 1 and row[4] == 1:
        continue

    if row[2] == 1:
        found_by_similo.append(row[1])
    if row[3] == 1:
        found_by_von_similo.append(row[1])
    if row[4] == 1:
        overlap_found_by_von_similo.append(row[1])

draw_venn(found_by_similo, found_by_von_similo, overlap_found_by_von_similo, subtitle='Von Similo', nrtype='int', output='png')

