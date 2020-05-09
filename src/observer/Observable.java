/**
 * 
 */
package observer;

/**
 * @author Thanuja
 *
 */
public interface Observable {
	
	public abstract void addObserver(Observer obs);
	public abstract void removeObserver(Observer obs);
	public abstract void notifyObservers();
	public abstract Object getUpdate();
	
}
