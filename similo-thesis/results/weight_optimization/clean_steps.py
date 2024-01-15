import pandas as pd

# Load the CSV file
file_path = 'similo_max_score_eliminated_four_month_frist_run.csv'  # Replace with your CSV file path

with open(file_path, 'r') as f:
    content = f.readlines()[1:]

    clean_content = []
    for row in content:

        row = row.split(',')
        clean_row = row[:2]

        phenotypes = row[2:]
        for pyh in phenotypes:
            # remove all [ and ] characters and multiply by 0.05
            replaced = pyh.replace('[', '').replace(']', '')
            clean_row.append(str(float(replaced) * 0.05)[:4])

        clean_content.append(clean_row)

    # combine to csv string and print
    csv_string = ('generation,fitness,tag,class,name,id,href,alt,type,aria-label,xpath.chrome,'
                  'xpath.idxpath,isButton,location,dimension,visible_text,neighbour_text,attributes\n')
    for row in clean_content:
        csv_string += ','.join(row) + '\n'

    # write to file
    with open(file_path, 'w') as f:
        f.write(csv_string)
