import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;

public class Blackboard extends JPanel implements MouseListener {

	private Vector<JLabel> J_grids = new Vector<JLabel>();
	private Vector<Integer> vec_grids = new Vector<Integer>();
	private int start = -1;
	private int end = -1;
	private int rows;
	private int cols;
	private volatile boolean isSearching = false;

	public Blackboard(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		setLayout(new GridLayout(rows, cols));

		int num_grids = rows * cols;
		for (int i = 0; i < num_grids; i++) {
			vec_grids.add(-1);
			J_grids.add(new JLabel());
			J_grids.get(i).addMouseListener(this);
			J_grids.get(i).setOpaque(true);

			J_grids.get(i).setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
			J_grids.get(i).setBackground(new Color(0xFFFFFF));

			add(J_grids.get(i));
			J_grids.get(i).addMouseListener(this);
		}
	}

	public void colorStart(JLabel box, int index) {
		if (start < 0) {
			box.setBackground(new Color(0xFFF000));
			start = index;
		}
	}
	public void colorEnd(JLabel box, int index){
		if (end < 0) {
			box.setBackground(new Color(0xAAAAAA));
			end = index;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int clicks = e.getClickCount();
		Object box_clicked = e.getSource();
		int index = 0;
		for(JLabel box : J_grids) {
			if(box == box_clicked) {
				if(clicks == 3){
					colorObstacle(box, index);
				}
				if(clicks == 1) {
					colorStart(box, index);
				}
				if(clicks == 2){
					colorEnd(box, index);
				}

			}
			index++;
		}

	}

	public void colorObstacle(JLabel box, int index) {
		// Allow obstacle placement if not start or end
		if (index != start && index != end) {
			box.setBackground(new Color(0x000000)); // Black for obstacle
		}
	}
	public void reset(int numGrids){
		rows = numGrids;
		cols = numGrids;
		vec_grids.clear();
		J_grids.clear();

		start = -1;
		end = -1;

		removeAll();
		setLayout(new GridLayout(rows, cols));

		int num_grids = rows * cols;
		for (int i = 0; i < num_grids; i++) {
			vec_grids.add(-1);
			J_grids.add(new JLabel());
			J_grids.get(i).addMouseListener(this);
			J_grids.get(i).setOpaque(true);

			J_grids.get(i).setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
			J_grids.get(i).setBackground(new Color(0xFFFFFF));

			add(J_grids.get(i));
			J_grids.get(i).addMouseListener(this);
		}
		revalidate();
		repaint();
	}

	// Convert 1D index to 2D coordinates
	private int[] indexTo2D(int index) {
		int row = index / cols;
		int col = index % cols;
		return new int[]{row, col};
	}

	// Convert 2D coordinates to 1D index
	private int coordsTo1D(int row, int col) {
		return row * cols + col;
	}

	// Check if a cell is an obstacle (black color)
	private boolean isObstacle(int index) {
		Color cellColor = J_grids.get(index).getBackground();
		// Check if color is black (0, 0, 0)
		return cellColor.getRed() == 0 && cellColor.getGreen() == 0 && cellColor.getBlue() == 0;
	}

	// BFS pathfinding algorithm with parallel neighbor exploration
	public Vector<Integer> findPathBFSParallel() {
		if (start == -1 || end == -1) {
			System.out.println("Start or End not set!");
			return null;
		}

		isSearching = true;
		Vector<Integer> path = new Vector<>();
		Queue<Integer> queue = new LinkedList<>();
		Vector<Integer> visited = new Vector<>();
		Vector<Integer> parent = new Vector<>();
		Object queueLock = new Object();
		Object visitedLock = new Object();

		// Initialize parent array and mark obstacles as visited
		for (int i = 0; i < rows * cols; i++) {
			parent.add(-1);
			// Check if cell is an obstacle (black color)
			if (isObstacle(i)) {
				visited.add(i);
			}
		}

		queue.add(start);
		visited.add(start);
		parent.set(start, -2);

		// BFS with parallel neighbor exploration
		while (!queue.isEmpty() && isSearching) {
			int current;

			synchronized (queueLock) {
				if (queue.isEmpty()) break;
				current = queue.poll();
			}

			// Visualize visited cell (blue) - but not start or end
			if (current != start && current != end) {
				J_grids.get(current).setBackground(new Color(0x0000FF));
				repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (current == end) {
				// Reconstruct path
				int node = end;
				while (node != -2) {
					path.add(0, node);
					node = parent.get(node);
				}
				isSearching = false;
				return path;
			}

			// Get 2D coordinates of current cell
			int[] coords = indexTo2D(current);
			int row = coords[0];
			int col = coords[1];

			// Check all 4 neighbors (up, down, left, right) in parallel
			int[][] neighbors = {
					{row - 1, col},
					{row + 1, col},
					{row, col - 1},
					{row, col + 1}
			};

			// Create threads for each neighbor exploration
			Thread[] neighborThreads = new Thread[neighbors.length];

			for (int i = 0; i < neighbors.length; i++) {
				final int neighborIdx = i;
				neighborThreads[i] = new Thread(() -> {
					int nRow = neighbors[neighborIdx][0];
					int nCol = neighbors[neighborIdx][1];

					if (nRow >= 0 && nRow < rows && nCol >= 0 && nCol < cols) {
						int neighborIndex = coordsTo1D(nRow, nCol);

						synchronized (visitedLock) {
							if (!visited.contains(neighborIndex)) {
								visited.add(neighborIndex);
								parent.set(neighborIndex, current);
								synchronized (queueLock) {
									queue.add(neighborIndex);
								}
							}
						}
					}
				});
				neighborThreads[i].start();
			}

			// Wait for all neighbor threads to complete
			for (Thread thread : neighborThreads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		isSearching = false;
		System.out.println("No path found!");
		return null;
	}

	// Run BFS with parallel thread distribution
	public void findPathMultiThread() {
		stopSearch();

		// BFS Thread with parallel neighbor exploration
		Thread bfsThread = new Thread(() -> {
			System.out.println("BFS Algorithm started (Blue) - with parallel neighbor exploration");
			Vector<Integer> bfsPath = findPathBFSParallel();
			if (bfsPath != null) {
				System.out.println("BFS Path found with " + bfsPath.size() + " cells");
				colorPathBFS(bfsPath);
				repaint();
			}
		});

		bfsThread.start();
	}

	// Color BFS path in cyan
	public void colorPathBFS(Vector<Integer> path) {
		if (path == null) return;

		for (Integer index : path) {
			if (index != start && index != end) {
				J_grids.get(index).setBackground(new Color(0x00FFFF));
			}
		}
	}

	// Clear the path and reset start/end
	public void clearPath() {
		// Reset all cells to white
		for (JLabel box : J_grids) {
			box.setBackground(new Color(0xFFFFFF));
		}
		// Clear start and end markers
		start = -1;
		end = -1;
		repaint();
	}

	// Getter for grid dimensions
	public int getRows() {
		return rows;
	}

	// Stop the current search
	public void stopSearch() {
		isSearching = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}