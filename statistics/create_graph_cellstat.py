import sys
import matplotlib.pyplot as plt

type_color = {
	1 : 'green',
	2 : 'yellow',
	3 : 'blue',
	4 : 'cyan',
	5 : 'orange',
	6 : 'magenta',
	7 : 'red',
	8 : 'purple',
	9 : 'black'
}

def read_stat(stat_file):
	with open(stat_file, "r") as f:
		
		steps = []
		
		alive_stats = {}
		dead_stats = {}
		dna_stats = {}
		
		for line in f:
			if ":" in line:
				next
			else:
				line_parts = line.split(",")
				cell_type = int(line_parts[0])
				alive = int(line_parts[1])
				dead = int(line_parts[2])
				dna_variation = float(line_parts[3])
				
				if not cell_type in alive_stats:
					alive_stats[cell_type] = []
				if not cell_type in dead_stats:
					dead_stats[cell_type] = []
				if not cell_type in dna_stats:
					dna_stats[cell_type] = []
				
				alive_stats[cell_type].append(alive)
				dead_stats[cell_type].append(dead)
				dna_stats[cell_type].append(dna_variation)
				
		return alive_stats, dead_stats, dna_stats

def plot_alive_dead(alive, dead, dna, type):
	plt.plot(range(0, len(alive[type])), alive[type], 'blue')
	plt.plot(range(0, len(dead[type])), dead[type], 'red')
	plt.plot(range(0, len(dna[type])), dna[type], 'green')
	plt.gcf().autofmt_xdate()
	plt.show()
	
def plot_types(alive):
	for i in alive:
		plt.plot(range(0, len(alive[i])), alive[i], type_color[i])
		
	plt.gcf().autofmt_xdate()
	plt.show()

		
if __name__ == "__main__":
	filename = sys.argv[1]
	
	alive_stats, dead_stats, dna_stats = read_stat(filename)
	
	
	
	
			
		
	