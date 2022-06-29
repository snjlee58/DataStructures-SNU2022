import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	MyLinkedList<MyLinkedList<MovieDBItem>> movieLists; // ( (BATMAN BEGINS, ONG-BAK, THE MATRIX), (MILLION DOLLAR BABY, THE AVIATOR), (HELLRAISER) )
    public MovieDB() {
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
		movieLists = new MyLinkedList<>();
    }


	public void insert(MovieDBItem item) {
		// Insert the given item to the MovieDB.

		Iterator<MyLinkedList<MovieDBItem>> movieDBiterator = movieLists.iterator();
		while (movieDBiterator.hasNext()){
			MyLinkedList<MovieDBItem> currMovieList = movieDBiterator.next();
			String currGenre = currMovieList.first().getGenre();
			if (item.getGenre().equals(currGenre)){
				// add item to currMovieList only if it doesn't already exist
				Iterator<MovieDBItem> currMovieListIterator = currMovieList.iterator();
				while (currMovieListIterator.hasNext()){
					// if movie with the same title already exist, return without doing anything
					MovieDBItem currMovie = currMovieListIterator.next();
					if (currMovie.getTitle().equals(item.getTitle())){
						return;
					}
				}

				// find correct lexicographical location of new item
				MyLinkedListIterator<MovieDBItem> currListIterator = (MyLinkedListIterator<MovieDBItem>) currMovieList.iterator();
				while (currListIterator.hasNext()){
					MovieDBItem currMovie = currListIterator.next();
					if (currMovie.getTitle().compareTo(item.getTitle()) > 0){
						currListIterator.insertBefore(item);
						return;
					}
				}
				// if the order of the new item is at the end of the list, add (append) to list
				currMovieList.add(item);
				return;
			}
		}

		// if genre wasn't found, create new list for the genre
		MyLinkedList<MovieDBItem> newGenreMovieList = new MyLinkedList<>();
		newGenreMovieList.add(item);
		// find correct lexicographical location of new genre
		MyLinkedListIterator<MyLinkedList<MovieDBItem>> movieListsIterator =(MyLinkedListIterator<MyLinkedList<MovieDBItem>>) movieLists.iterator();
		while (movieListsIterator.hasNext()){
			MyLinkedList<MovieDBItem> currMovieList = movieListsIterator.next();
			String currGenre = currMovieList.first().getGenre();
			if (currGenre.compareTo(item.getGenre()) > 0){
				movieListsIterator.insertBefore(newGenreMovieList);
				return;
			}
		}
		// if the order of the new item is at the end of the list, add (append) to list
		movieLists.add(newGenreMovieList);
		return;
	}

    public void delete(MovieDBItem item) {
        // Remove the given item from the MovieDB.
		Iterator<MyLinkedList<MovieDBItem>> movieDBiterator = movieLists.iterator();
		while (movieDBiterator.hasNext()) {
			MyLinkedList<MovieDBItem> currMovieList = movieDBiterator.next();
			String currGenre = currMovieList.first().getGenre();
			if (item.getGenre().equals(currGenre)) {
				Iterator<MovieDBItem> movieDBItemIterator = currMovieList.iterator();
				while (movieDBItemIterator.hasNext()){
					MovieDBItem currMovieDBItem = movieDBItemIterator.next();
					if (currMovieDBItem.getTitle().equals(item.getTitle())){
						movieDBItemIterator.remove();

						// remove list if empty
						if (currMovieList.isEmpty()){
							movieDBiterator.remove();
						}
						break;
					}
				}
			}
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // Search the given term from the MovieDB.
		MyLinkedList<MovieDBItem> results = new MyLinkedList<>();

		Iterator<MovieDBItem> movieIterator = items().iterator();
		while (movieIterator.hasNext()){
			MovieDBItem currMovieDBItem = movieIterator.next();
			String title = currMovieDBItem.getTitle();
			if (title.contains(term)){
				results.add(currMovieDBItem);
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<>();

		Iterator<MyLinkedList<MovieDBItem>> movieDBiterator = movieLists.iterator();
		while (movieDBiterator.hasNext()) {
			MyLinkedList<MovieDBItem> currMovieList = movieDBiterator.next();
			Iterator<MovieDBItem> movieDBItemIterator = currMovieList.iterator();

			while (movieDBItemIterator.hasNext()){
				MovieDBItem currMovieDBItem = movieDBItemIterator.next();
				results.add(currMovieDBItem);
			}

		}
		return results;
    }


}





class Genre extends Node<String> implements Comparable<Genre> {
	public Genre(String name) {
		super(name);
	}

	@Override
	public int compareTo(Genre o) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getItem().equals(obj.toString())){
			return true;
		} else return false;
	}
}




class MovieList implements ListInterface<String> {
	public MovieList() {
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String first() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}