/**
 * 
 */
package ServerSockets;

/**
 * @author sm22898
 *
 */
public abstract class AbstractServerComponent {

	public abstract void handleMessagesFromClient(String msg, ClientManager clientmgr);
	public abstract void sendMessageToClient(String msg, ClientManager clientmgr);
	
}
