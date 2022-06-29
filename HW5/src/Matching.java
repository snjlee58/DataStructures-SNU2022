import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Matching
{

	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		File file = new File("");
		MyHashTable<StringForHash, AVLTree<StringForHash, LinkedList<String>>> hashTable = new MyHashTable<>(100);

		while (true)
		{
			try
			{
				String input = br.readLine();

				if (input.charAt(0) == '<'){
					file = getFile(input);
					hashTable = buildHashTable(file);
				} else if(input.charAt(0) == '@'){
					int index = Integer.valueOf(input.substring(1).trim());
					AVLTree<StringForHash, LinkedList<String>> searchedAVLTree = hashTable.getSlot(index);
					if (searchedAVLTree == null){
						System.out.println("EMPTY");
					} else{
						searchedAVLTree.printPreorder(searchedAVLTree.getRoot());
					}
				} else if (input.charAt(0) == '?'){
					searchPattern(hashTable, input);

				} else if (input.compareTo("QUIT") == 0)
					break;
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static File getFile(String input){
		String filePath = "";

		filePath = input.substring(2);
//		System.out.println(filePath);

//		String textDir = "./testset/text/";
//		if (input.contains(textDir)){
//			int pathStartIdx = input.lastIndexOf(textDir);
//			filePath = input.substring(pathStartIdx);
//		} else {
//			String fileName = input.substring(1).trim();
//			filePath = textDir + fileName;
//		}
		File file = new File(filePath);
		return file;
	}

	private static LinkedList<StringForHash> convertToSubstringList(String pattern){
		LinkedList<StringForHash> substringList = new LinkedList<>();
		int m = pattern.length(), k = 6;
		for (int i = 0; i < m-k+1; i++){
			StringForHash substring = new StringForHash(pattern.substring(i, i+k));
			substringList.add(substring);
		}
		return substringList;
	}

	private static MyHashTable buildHashTable(File file){
		MyHashTable<StringForHash, AVLTree<StringForHash, LinkedList<String>>> hashTable = new MyHashTable<>(100);
		try {
			Scanner fileText = new Scanner(file);
			int stringNum = 0;
			while (fileText.hasNext()) { // Iterate through each line (Si, where i=1~n)
				stringNum++;
				String currLine = fileText.nextLine();
				int m = currLine.length(), k = 6;
				for (int i = 0; i < m-k+1; i++){
					// Parse substring and coord for the substring
					StringForHash substring = new StringForHash(currLine.substring(i, i+k));
					String coord = "(" + stringNum + ", " + Integer.toString(i+1) + ")";

					// Insert new AVLTree in hashSlot if it is empty
					boolean newTreeCreated = hashTable.insert(substring, new AVLTree<StringForHash, LinkedList<String>>());
					AVLTree<StringForHash, LinkedList<String>> searchedAVLTree = hashTable.getSlot(substring);

					// Insert new AVLNode in the AVLTree if the substring doesn't already exist
					if (newTreeCreated){
						searchedAVLTree.insert(new MyAVLNode<StringForHash, LinkedList<String>>(substring, new LinkedList<String>()));
					} else {
						MyAVLNode<StringForHash, LinkedList<String>> substringNode = searchedAVLTree.search(substring);
						if (substringNode == AVLTree.NIL){
							searchedAVLTree.insert(new MyAVLNode<StringForHash, LinkedList<String>>(substring, new LinkedList<String>()));
						}
					}

					// Append coord to corresponding AVLNode's linkedList
					MyAVLNode<StringForHash, LinkedList<String>> substringNode = searchedAVLTree.search(substring);
					substringNode.item.add(coord);

				}
			}
			fileText.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return hashTable;
	}

	private static void searchPattern(MyHashTable<StringForHash, AVLTree<StringForHash, LinkedList<String>>> hashTable, String input){
		try{
			String pattern = input.substring(2);
			LinkedList<StringForHash> patternSubstringList = convertToSubstringList(pattern);

			// Get coordinate list for the first 6-letter segment of the searched pattern
			StringForHash frontOfPattern = patternSubstringList.getFirst();
			LinkedList<String> potentialCoords = hashTable.getSlot(frontOfPattern).search(frontOfPattern).item;
			if (potentialCoords == null) throw new NullPointerException();

			// Filter only the valid coordinates (containing the remaining substrings of the pattern
			LinkedList<String> resultCoordList = new LinkedList<>();
			for (String coord : potentialCoords){
				int lineNum = Integer.valueOf(coord.substring(1, coord.indexOf(',')));
				int positionNum = Integer.valueOf(coord.substring(coord.indexOf(' ')+1, coord.indexOf(')')));
				String foundCoord = "(" + lineNum + ", " + positionNum + ")";
				boolean match = true;

				for (StringForHash substring : patternSubstringList){
					String expectedCoord = "(" + lineNum + ", " + positionNum + ")";
					LinkedList<String> substringCoordList = hashTable.getSlot(substring).search(substring).item;
					if (!substringCoordList.contains(expectedCoord)){
						match = false;
						break;
					}
					positionNum++;
				}
				if (match) resultCoordList.add(foundCoord);
			}

			if (resultCoordList.size() <= 0) throw new NullPointerException();

			// Print answers
			for (int i = 0; i < resultCoordList.size()-1; i++){
				System.out.print(resultCoordList.get(i) + " ");
			}
			System.out.println(resultCoordList.getLast());

		} catch (NullPointerException e){
			System.out.println("(0, 0)");
		}
	}

}
