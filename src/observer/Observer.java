/**
 * 
 */
package observer;


public interface Observer {

	public abstract void update();
	public abstract void setObservable(Observable obs);
}
