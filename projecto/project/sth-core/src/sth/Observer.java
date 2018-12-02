package sth;

import java.util.List;

public interface Observer {

	public void receiveNotification(String notification);
	public List<String> seeAllNotifications();
	public void disableNotifications(String discName);

}