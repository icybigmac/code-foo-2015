import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class IGNWordSearchSolver {
	private List<List> wordSearch = new ArrayList<>();
	private List<String> words = new ArrayList<>();
	private Map<String, List<WordCoord>> solutions = new HashMap<String, List<WordCoord>>();
	
	IGNWordSearchSolver() {
		buildWordSearch("word-search.txt");
	}
	
	public void solve() {
		find();
		printWordSearch();
		System.out.println();
		printWords();
		System.out.println();
		printSolution();
	}

	private void buildWordSearch(String path) {
		File file = new File(path);
		try {
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				String line = s.nextLine();
				if (!line.equals("Words to find:") && !line.isEmpty()) {
					List<String> row = new ArrayList<>(Arrays.asList(line.split(" ")));
					wordSearch.add(row);
				} else {
					s.nextLine();
					s.nextLine();
					break;
				}
			}
			
			while (s.hasNextLine()) {
				String line = s.nextLine().trim();
				if (!line.isEmpty()) {
					words.add(line);
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void printWordSearch() {
		System.out.print("  ");
		for (int i = 1; i <= wordSearch.get(0).size(); i++) {
			if (i < 10) {
				System.out.print(i + " ");
			} else {
				System.out.print(i);
			}
		}
		System.out.println();
		
		for (int i = 0; i < wordSearch.size(); i++) {
			if (i + 1 < 10) {
				System.out.print(i + 1 + " ");
			} else {
				System.out.print(i + 1);
			}
			for (int j = 0; j < wordSearch.get(i).size(); j++) {
				System.out.print(wordSearch.get(i).get(j) + " ");
			}
			System.out.println("");
		}
	}
	
	private void printWords() {
		for (String word : words) {
			System.out.println(word);
		}
	}
	
	private void printSolution() {
		for (String word : words) {
			System.out.println(word);
			for (WordCoord c : solutions.get(word)) {
				if (solutions.get(word) == null) {
					System.out.println("Could not find word.");
				} else {
					c.print();
				}
			}
			System.out.println();
		}
	}
	
	private void find() {
		for (int i = 0; i < wordSearch.size(); i++) {
			for (int j = 0; j < wordSearch.get(i).size(); j++) {
				for (String word : words) {
					findRight(word, i, j);
					findLeft(word, i, j);
					findUp(word, i, j);
					findDown(word, i, j);
					findUpRight(word, i, j);
					findDownRight(word, i, j);
					findUpLeft(word, i, j);
					findDownLeft(word, i, j);
				}
			}
		}
	}

	private void findRight(String word, int x, int y) {
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		while (j < wordSearch.get(i).size() && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				j++;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i, j - 1, word);
		}
	}

	private void findLeft(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (j >= 0 && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				j--;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i, j + 1, word);
		}
	}

	private void findUp(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (i >= 0 && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				i--;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i + 1, j, word);
		}
	}

	private void findDown(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (i < wordSearch.size() && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				i++;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i - 1, j, word);
		}
	}

	private void findUpRight(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (i >= 0 && j < wordSearch.get(i).size() && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				i--;
				j++;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i + 1, j - 1, word);
		}
	}

	private void findDownRight(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (i < wordSearch.size() && j < wordSearch.get(i).size() && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				i++;
				j++;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i - 1, j - 1, word);
		}
	}

	private void findUpLeft(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (i >= 0 && j >= 0 && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				i--;
				j--;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i + 1, j + 1, word);
		}
	}

	private void findDownLeft(String word, int x, int y) {
		// TODO Auto-generated method stub
		boolean found = true;
		int a = 0;
		int i = x;
		int j = y;
		
		while (i < wordSearch.size() && j >= 0 && a < word.length()) {
			if (!isEqual(i, j, a, word)) {
				found = false;
				break;
			} else {
				i++;
				j--;
				a++;
			}
		}
		
		if (found && a == word.length()) {
			addSolution(x, y, i - 1, j + 1, word);
		}
	}
	
	private boolean isEqual(int x, int y, int a, String word) {
		return word.charAt(a) == wordSearch.get(x).get(y).toString().charAt(0);
	}
	
	private void addSolution(int x, int y, int i, int j, String word) {
		List <WordCoord> newList;
		if (solutions.get(word) == null) {
			newList = new ArrayList<WordCoord>();
		} else {
			newList = new ArrayList<WordCoord>(solutions.get(word));
		}
		newList.add(new WordCoord(x + 1, y + 1, i + 1,  j + 1));
		solutions.put(word, newList);
	}
	
	private class WordCoord {
		private int startX;
		private int endX;
		private int startY;
		private int endY;
		
		WordCoord(int x, int y, int a, int b) {
			startX = x;
			startY = y;
			endX = a;
			endY = b;
		}
		
		public void print() {
			System.out.println("Starts at [" + startX + ", " + startY + 
					"] and ends at [" + endX + ", " + endY + "].");
		}
	}
}
